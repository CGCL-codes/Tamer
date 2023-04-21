package ogreimport;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Class to load and use animated objects created and Blender and exported as
 * OGRE XML files.
 *
 * @author christian_kuper
 * @version 0.1.20111123
 */
public class OgreObject {

    /**
     * The coordinates of the shape displayed. These are the coorinates of the
     * shape after deformation
     */
    private float fCoords[];

    /**
     * The original coordinates of the shape, i.e. the coordinates of the
     * shpe directly after loading. These coordinates are not changed and used
     * as a static reference for animation calculations.
     */
    private float fCoordsOrg[];

    /**
     * The norms of the shape displaed. These are the norms of the
     * shape after deformation
     */
    private float fNorms[];

    /**
     * The original norms of the shape, i.e. the norms of the
     * shape directly after loading. These norms are not changed and used
     * as a static reference for animation calculations.
     */
    private float fNormsOrg[];

    /**
     * The indexes of the IndexedTriangleArrays used for creating the Sub Shape3D
     */
    private ArrayList<int[]> indexes = new ArrayList<int[]>();

    /**
     * The texture coordinates. Currently the mapping doese not seem to be correct,
     * so trial and error is required for correct texture coordinates.
     */
    private float fTex[];

    /**
     * The GeometryArray for the Shape3D
     */
    private IndexedTriangleArray geom;

    /**
     * The Sub Shapes (Shape3D) created from the OGRE XML file.
     */
    private ArrayList<Shape3D> shapes;

    /**
     * A BranchGroup containing all the sub Shapes
     */
    private BranchGroup shapeBG;

    /**
     * A map with the bone name as key and the Bone object as value.
     */
    private HashMap<String, Bone> boneMap;

    /**
     * An array of bones without parent. This is used for the future when
     * the import of multiple root skeletons should be possible
     */
    private ArrayList<String> rootBones;

    /**
     * The head position of the root bone, i.e. the bone without parent. It is
     * recommeded to set this to (0f, 0f, 0f) to avoid strange effects when
     * scaling the shape.
     */
    private Vector3f skeletonBase;

    /**
     * Map for storing animation information
     */
    private HashMap animMap;

    /**
     * Constructor which just sets the skeleton base. No data is loaded, the
     * loading of the XML file needs to be done by calling the relevant method.
     * @param skeletonBase The head position of the root bone, i.e. the bone without parent. It is
     * recommeded to set this to (0f, 0f, 0f) to avoid strange effects when
     * scaling the shape.
     */
    public OgreObject(Vector3f skeletonBase) {
        this.skeletonBase = skeletonBase;
        shapeBG = new BranchGroup();
    }

    /**
     * Returns the coordinates of the shape
     * @return The coordinates of the shape displayed. These are the coorinates of the
     * shape after deformation
     */
    public float[] getfCoords() {
        return fCoords;
    }

    /**
     * Sets the coordinates of the shape. Should not be use and is just included
     * for future development
     * @param fCoords The coordinates of the shape displayed. These are the coorinates of the
     * shape after deformation
     */
    public void setfCoords(float[] fCoords) {
        this.fCoords = fCoords;
    }

    /**
     * Returns the normals of the Shape3D.
     * @return The norms of the shape displayed. These are the norms of the
     * shape after deformation
     */
    public float[] getfNorms() {
        return fNorms;
    }

    /**
     * Sets the normals of the Shape3D. Should not be use and is just included
     * for future development
     * @param fNorms The norms of the shape displayed. These are the norms of the
     * shape after deformation
     */
    public void setfNorms(float[] fNorms) {
        this.fNorms = fNorms;
    }

    /**
     * Returns the BranchGroup containing the Shape3Ds created from the
     * OGRE XML file data.
     * @return Shape3D created from the OGRE XML file data.
     */
    public BranchGroup getShapeBG() {
        return shapeBG;
    }

    /**
     * Map for storing information
     * @return
     */
    public HashMap getAnimMap() {
        return animMap;
    }

    /**
     * Loads the data from the OGRE XML files. Loads the data from the
     * mesh, the material and the skeleton file. Loading of the XML file is
     * done with the help of SAX with special content handlers for the Mesh and
     * the skeleton file. Material is extracted form the material file using a
     * specific wrapper, the Material file is not a XML file.
     * @param dir The directory containing the files. All files mus be in the same directory
     * @param fileName The name of the Mesh file. As the mesh file contains the
     * name of the skeleton file (if used), the name of the skeleton file does not
     * need to be passed.
     */
    public void load(String dir, String fileName) {
        OgreMeshHandler oh = new OgreMeshHandler();
        loadMesh(oh, dir + "/" + fileName);
        this.fCoords = oh.getCoords();
        this.fCoordsOrg = fCoords.clone();
        this.indexes = oh.getIndexes();
        this.fNorms = oh.getNorms();
        this.fNormsOrg = fNorms.clone();
        this.fTex = oh.getTexCoords();
        OgreMaterialReader omr = new OgreMaterialReader();
        for (int i = 0; i < oh.getIndexes().size(); i++) {
            if ((oh.getMaterialName() != null) && (!oh.getMaterialName().isEmpty())) {
                omr.read(dir + "/" + oh.getMaterialName().get(i) + ".material", oh.getMaterialName().get(i));
            }
            if ((oh.getSkeletonName() != null) && (!oh.getSkeletonName().isEmpty()) && (i == 0)) {
                OgreSkelHandler osh = new OgreSkelHandler(fCoordsOrg, fCoords, fNormsOrg, fNorms, oh.getWeightMap(), skeletonBase);
                loadSkeleton(osh, dir + "/" + oh.getSkeletonName());
                this.boneMap = osh.getBoneMap();
                this.rootBones = osh.getRootNames();
                this.animMap = osh.getAnimMap();
            }
            createShape(oh, omr, i);
        }
    }

    /**
     * Creates the Shape3D from the data in the OGRE XML file. Called by the
     * load() method. The method first creates and IndexEdTriangleArray with
     * ByRef property. The data is then read from the OgreMeshHandler and the
     * OgreMaterialReader were the data is stored after reading from the file.
     * Finally the Appearance is created and applied.
     * @param oh SAX ContenHandler for the mesh file which stores all mesh data
     * read from the XML file
     * @param omr A reader for the OGRE Material file holding all the data read
     * from the material file.
     */
    private void createShape(OgreMeshHandler oh, OgreMaterialReader omr, int subShpIdx) {
        javax.media.j3d.IndexedTriangleArray mesh = new javax.media.j3d.IndexedTriangleArray(oh.getVertexCount(), IndexedTriangleArray.COORDINATES | IndexedTriangleArray.NORMALS | IndexedTriangleArray.BY_REFERENCE | IndexedTriangleArray.BY_REFERENCE_INDICES | IndexedTriangleArray.USE_COORD_INDEX_ONLY | IndexedTriangleArray.TEXTURE_COORDINATE_2, oh.getIndexes().get(subShpIdx).length);
        mesh.setCoordRefFloat(fCoords);
        mesh.setCoordIndicesRef(indexes.get(subShpIdx));
        mesh.setNormalRefFloat(fNorms);
        mesh.setTexCoordRefFloat(0, fTex);
        Shape3D shape = new Shape3D(mesh);
        Appearance app = omr.getAppearance();
        shape.setAppearance(app);
        if (subShpIdx == 0) {
            geom = (IndexedTriangleArray) shape.getGeometry();
            geom.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
            geom.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        }
        shapeBG.addChild(shape);
    }

    /**
     * Reads the data from the mesh file and is called by the load() method.
     * Loading an parsing is done with SAX.
     * @param oh SAX ContenHandler for the mesh file which stores all mesh data
     * read from the XML file
     * @param fileName Full path and filename of the mesh file
     */
    private void loadMesh(OgreMeshHandler oh, String fileName) {
        XMLReader xmlReader = null;
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
        } catch (SAXException ex) {
            System.err.println("Error: Coud not create XMLReader for " + fileName + ". Terminating");
            System.exit(1);
        }
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.err.println("Error: Coud not create FileReader for " + fileName + ". Terminating. Reason: " + ex.getLocalizedMessage());
            System.exit(1);
        }
        InputSource inputSource = new InputSource(reader);
        xmlReader.setContentHandler(oh);
        try {
            xmlReader.parse(inputSource);
        } catch (IOException ex) {
            System.err.println("Error parsing " + fileName + ". Terminating");
            System.exit(1);
        } catch (SAXException ex) {
            System.err.println("Error parsing " + fileName + ". Terminating");
            System.exit(1);
        }
    }

    /**
     * Reads the data from the skeleton file and is called by the load() method.
     * Loading an parsing is done with SAX.
     * @param oh SAX ContenHandler for the skeleton file which stores all skeleton data
     * read from the XML file
     * @param fileName Full path and filename of the skeleton file
     */
    private void loadSkeleton(OgreSkelHandler oh, String fileName) {
        XMLReader xmlReader = null;
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
        } catch (SAXException ex) {
            System.err.println("Error: Coud not create XMLReader for " + fileName + ". Terminating");
            System.exit(1);
        }
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.err.println("Error: Coud not create FileReader for " + fileName + ". Terminating. Reason: " + ex.getLocalizedMessage());
            System.exit(1);
        }
        InputSource inputSource = new InputSource(reader);
        xmlReader.setContentHandler(oh);
        try {
            xmlReader.parse(inputSource);
        } catch (IOException ex) {
            System.err.println("Error parsing " + fileName + ". Terminating");
            System.exit(1);
        } catch (SAXException ex) {
            System.err.println("Error parsing " + fileName + ". Terminating");
            System.exit(1);
        }
    }

    /**
     * Rotates a specific Bone of the skeleton during animation. The mesh is
     * not updated yet, this must be done by calling the appropriate method.
     * The roation parameter are relative to the local world of the bone
     * @param name The name of the bone to rotate.
     * @param axis The axis for the axis angle
     * @param angle The roation angle
     */
    public void rotBone(String name, Vector3f axis, float angle) {
        Bone b = boneMap.get(name);
        if (b == null) {
            return;
        }
        b.rotBone(axis, angle);
    }

    /**
     * Rotates a specific Bone of the skeleton during animation. The mesh is
     * not updated yet, this must be done by calling the appropriate method.
     * The roation parameter are relative to the local world of the bone
     * @param name The name of the bone to rotate.
     * @param aa The AxisAngle4f for rotation
     */
    public void rotBone(String name, AxisAngle4f aa) {
        Bone b = boneMap.get(name);
        if (b == null) {
            return;
        }
        b.rotBone(aa);
    }

    /**
     * Rotates a specific Bone of the skeleton during animation. The mesh is
     * not updated yet, this must be done by calling the appropriate method.
     * The roation parameter are relative to the local world of the bone. However,
     * in contrast to rotBone() the movement is not relative to the current
     * position but relative to the original position of the bone
     * @param name
     * @param aa
     */
    public void rotBoneAbs(String name, AxisAngle4f aa) {
        Bone b = boneMap.get(name);
        if (b == null) {
            return;
        }
        b.rotBoneAbs(aa);
    }

    /**
     * Called by the GeometryUpdater for updating the mesh.
     */
    private void update() {
        boolean clear = true;
        for (String bName : rootBones) {
            Bone bone = boneMap.get(bName);
            updateMesh(bone, clear);
            clear = false;
        }
    }

    /**
     * The method to update the mesh. The actual updating is implemented in
     * the Bone object. This is a recursive method iterrating through the
     * complete skeleton.
     * @param bone Bone object influencing the mesh
     * @param clear Parameter to indicate whether coords and norms should be
     * cleared before updating. The first call of an update cycle must set this
     * parameter to true, all subsequent calls must use false.
     */
    private void updateMesh(Bone bone, boolean clear) {
        bone.updateMesh(clear);
        clear = false;
        Enumeration en = bone.getAllChildren();
        while (en.hasMoreElements()) {
            Bone b = (Bone) en.nextElement();
            updateMesh(b, false);
        }
    }

    /**
     * Called when mesh should be updated. The GeometryUpdater
     */
    public void updateMesh() {
        geom.updateData(new GeometryUpdater() {

            public void updateData(Geometry gmtr) {
                update();
            }
        });
    }
}
