package com.jme3.scene.plugins.blender.meshes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.jme3.asset.BlenderKey.FeaturesToLoad;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.scene.plugins.blender.AbstractBlenderHelper;
import com.jme3.scene.plugins.blender.BlenderContext;
import com.jme3.scene.plugins.blender.BlenderContext.LoadedFeatureDataType;
import com.jme3.scene.plugins.blender.exceptions.BlenderFileException;
import com.jme3.scene.plugins.blender.file.DynamicArray;
import com.jme3.scene.plugins.blender.file.Pointer;
import com.jme3.scene.plugins.blender.file.Structure;
import com.jme3.scene.plugins.blender.materials.MaterialContext;
import com.jme3.scene.plugins.blender.materials.MaterialHelper;
import com.jme3.scene.plugins.blender.objects.Properties;
import com.jme3.scene.plugins.blender.textures.TextureHelper;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

/**
 * A class that is used in mesh calculations.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
public class MeshHelper extends AbstractBlenderHelper {

    /**
     * This constructor parses the given blender version and stores the result. Some functionalities may differ in different blender
     * versions.
     * 
     * @param blenderVersion
     *            the version read from the blend file
     * @param fixUpAxis
     *        a variable that indicates if the Y asxis is the UP axis or not
     */
    public MeshHelper(String blenderVersion, boolean fixUpAxis) {
        super(blenderVersion, fixUpAxis);
    }

    /**
     * This method reads converts the given structure into mesh. The given structure needs to be filled with the appropriate data.
     * 
     * @param structure
     *            the structure we read the mesh from
     * @return the mesh feature
     * @throws BlenderFileException
     */
    @SuppressWarnings("unchecked")
    public List<Geometry> toMesh(Structure structure, BlenderContext blenderContext) throws BlenderFileException {
        List<Geometry> geometries = (List<Geometry>) blenderContext.getLoadedFeature(structure.getOldMemoryAddress(), LoadedFeatureDataType.LOADED_FEATURE);
        if (geometries != null) {
            List<Geometry> copiedGeometries = new ArrayList<Geometry>(geometries.size());
            for (Geometry geometry : geometries) {
                copiedGeometries.add(geometry.clone());
            }
            return copiedGeometries;
        }
        TextureHelper textureHelper = blenderContext.getHelper(TextureHelper.class);
        String name = structure.getName();
        MeshContext meshContext = new MeshContext();
        Vector3f[] vertices = this.getVertices(structure, blenderContext);
        int verticesAmount = vertices.length;
        List<byte[]> verticesColors = this.getVerticesColors(structure, blenderContext);
        Map<Integer, List<Integer>> meshesMap = new HashMap<Integer, List<Integer>>();
        Pointer pMFace = (Pointer) structure.getFieldValue("mface");
        List<Structure> mFaces = null;
        if (pMFace.isNotNull()) {
            mFaces = pMFace.fetchData(blenderContext.getInputStream());
            if (mFaces == null || mFaces.size() == 0) {
                return new ArrayList<Geometry>(0);
            }
        } else {
            mFaces = new ArrayList<Structure>(0);
        }
        Pointer pMTFace = (Pointer) structure.getFieldValue("mtface");
        Map<Integer, List<Vector2f>> uvCoordinates = new HashMap<Integer, List<Vector2f>>();
        List<Structure> mtFaces = null;
        if (pMTFace.isNotNull()) {
            mtFaces = pMTFace.fetchData(blenderContext.getInputStream());
            int facesAmount = ((Number) structure.getFieldValue("totface")).intValue();
            if (mtFaces.size() != facesAmount) {
                throw new BlenderFileException("The amount of faces uv coordinates is not equal to faces amount!");
            }
        }
        Map<Vector3f, Vector3f> normalMap = new HashMap<Vector3f, Vector3f>(verticesAmount);
        List<Vector3f> normalList = new ArrayList<Vector3f>();
        List<Vector3f> vertexList = new ArrayList<Vector3f>();
        Map<Integer, Texture> materialNumberToTexture = new HashMap<Integer, Texture>();
        Map<Integer, List<Integer>> vertexReferenceMap = new HashMap<Integer, List<Integer>>(verticesAmount);
        int vertexColorIndex = 0;
        for (int i = 0; i < mFaces.size(); ++i) {
            Structure mFace = mFaces.get(i);
            int matNr = ((Number) mFace.getFieldValue("mat_nr")).intValue();
            boolean smooth = (((Number) mFace.getFieldValue("flag")).byteValue() & 0x01) != 0x00;
            DynamicArray<Number> uvs = null;
            boolean materialWithoutTextures = false;
            Pointer pImage = null;
            List<Vector2f> uvCoordinatesList = uvCoordinates.get(Integer.valueOf(matNr));
            if (uvCoordinatesList == null) {
                uvCoordinatesList = new ArrayList<Vector2f>();
                uvCoordinates.put(Integer.valueOf(matNr), uvCoordinatesList);
            }
            if (mtFaces != null) {
                Structure mtFace = mtFaces.get(i);
                pImage = (Pointer) mtFace.getFieldValue("tpage");
                materialWithoutTextures = pImage.isNull();
                uvs = (DynamicArray<Number>) mtFace.getFieldValue("uv");
                uvCoordinatesList.add(new Vector2f(uvs.get(0, 0).floatValue(), uvs.get(0, 1).floatValue()));
                uvCoordinatesList.add(new Vector2f(uvs.get(1, 0).floatValue(), uvs.get(1, 1).floatValue()));
                uvCoordinatesList.add(new Vector2f(uvs.get(2, 0).floatValue(), uvs.get(2, 1).floatValue()));
            }
            Integer materialNumber = Integer.valueOf(materialWithoutTextures ? -1 * matNr - 1 : matNr);
            List<Integer> indexList = meshesMap.get(materialNumber);
            if (indexList == null) {
                indexList = new ArrayList<Integer>();
                meshesMap.put(materialNumber, indexList);
            }
            if (pImage != null && pImage.isNotNull() && !materialNumberToTexture.containsKey(materialNumber)) {
                Texture texture = textureHelper.getTextureFromImage(pImage.fetchData(blenderContext.getInputStream()).get(0), blenderContext);
                if (texture != null) {
                    materialNumberToTexture.put(materialNumber, texture);
                }
            }
            int v1 = ((Number) mFace.getFieldValue("v1")).intValue();
            int v2 = ((Number) mFace.getFieldValue("v2")).intValue();
            int v3 = ((Number) mFace.getFieldValue("v3")).intValue();
            int v4 = ((Number) mFace.getFieldValue("v4")).intValue();
            Vector3f n = FastMath.computeNormal(vertices[v1], vertices[v2], vertices[v3]);
            this.addNormal(n, normalMap, smooth, vertices[v1], vertices[v2], vertices[v3]);
            normalList.add(normalMap.get(vertices[v1]));
            normalList.add(normalMap.get(vertices[v2]));
            normalList.add(normalMap.get(vertices[v3]));
            this.appendVertexReference(v1, vertexList.size(), vertexReferenceMap);
            indexList.add(vertexList.size());
            vertexList.add(vertices[v1]);
            this.appendVertexReference(v2, vertexList.size(), vertexReferenceMap);
            indexList.add(vertexList.size());
            vertexList.add(vertices[v2]);
            this.appendVertexReference(v3, vertexList.size(), vertexReferenceMap);
            indexList.add(vertexList.size());
            vertexList.add(vertices[v3]);
            if (v4 > 0) {
                if (uvs != null) {
                    uvCoordinatesList.add(new Vector2f(uvs.get(0, 0).floatValue(), uvs.get(0, 1).floatValue()));
                    uvCoordinatesList.add(new Vector2f(uvs.get(2, 0).floatValue(), uvs.get(2, 1).floatValue()));
                    uvCoordinatesList.add(new Vector2f(uvs.get(3, 0).floatValue(), uvs.get(3, 1).floatValue()));
                }
                this.appendVertexReference(v1, vertexList.size(), vertexReferenceMap);
                indexList.add(vertexList.size());
                vertexList.add(vertices[v1]);
                this.appendVertexReference(v3, vertexList.size(), vertexReferenceMap);
                indexList.add(vertexList.size());
                vertexList.add(vertices[v3]);
                this.appendVertexReference(v4, vertexList.size(), vertexReferenceMap);
                indexList.add(vertexList.size());
                vertexList.add(vertices[v4]);
                this.addNormal(n, normalMap, smooth, vertices[v4]);
                normalList.add(normalMap.get(vertices[v1]));
                normalList.add(normalMap.get(vertices[v3]));
                normalList.add(normalMap.get(vertices[v4]));
                if (verticesColors != null) {
                    verticesColors.add(vertexColorIndex + 3, verticesColors.get(vertexColorIndex));
                    verticesColors.add(vertexColorIndex + 4, verticesColors.get(vertexColorIndex + 2));
                }
                vertexColorIndex += 6;
            } else {
                if (verticesColors != null) {
                    verticesColors.remove(vertexColorIndex + 3);
                    vertexColorIndex += 3;
                }
            }
        }
        meshContext.setVertexList(vertexList);
        meshContext.setVertexReferenceMap(vertexReferenceMap);
        Vector3f[] normals = normalList.toArray(new Vector3f[normalList.size()]);
        Structure parent = blenderContext.peekParent();
        Structure defbase = (Structure) parent.getFieldValue("defbase");
        List<Structure> defs = defbase.evaluateListBase(blenderContext);
        String[] verticesGroups = new String[defs.size()];
        int defIndex = 0;
        for (Structure def : defs) {
            verticesGroups[defIndex++] = def.getFieldValue("name").toString();
        }
        MaterialHelper materialHelper = blenderContext.getHelper(MaterialHelper.class);
        MaterialContext[] materials = null;
        if ((blenderContext.getBlenderKey().getFeaturesToLoad() & FeaturesToLoad.MATERIALS) != 0) {
            materials = materialHelper.getMaterials(structure, blenderContext);
        }
        geometries = new ArrayList<Geometry>(meshesMap.size());
        VertexBuffer verticesBuffer = new VertexBuffer(Type.Position);
        verticesBuffer.setupData(Usage.Stream, 3, Format.Float, BufferUtils.createFloatBuffer(vertexList.toArray(new Vector3f[vertexList.size()])));
        VertexBuffer verticesBind = new VertexBuffer(Type.BindPosePosition);
        verticesBind.setupData(Usage.CpuOnly, 3, Format.Float, BufferUtils.clone(verticesBuffer.getData()));
        VertexBuffer normalsBuffer = new VertexBuffer(Type.Normal);
        normalsBuffer.setupData(Usage.Stream, 3, Format.Float, BufferUtils.createFloatBuffer(normals));
        VertexBuffer normalsBind = new VertexBuffer(Type.BindPoseNormal);
        normalsBind.setupData(Usage.CpuOnly, 3, Format.Float, BufferUtils.clone(normalsBuffer.getData()));
        Properties properties = this.loadProperties(structure, blenderContext);
        ByteBuffer verticesColorsBuffer = this.createByteBuffer(verticesColors);
        verticesAmount = vertexList.size();
        Map<Mesh, Integer> meshToMAterialMap = new HashMap<Mesh, Integer>(meshesMap.size());
        for (Entry<Integer, List<Integer>> meshEntry : meshesMap.entrySet()) {
            Mesh mesh = new Mesh();
            meshToMAterialMap.put(mesh, meshEntry.getKey());
            List<Integer> indexList = meshEntry.getValue();
            if (verticesAmount <= Short.MAX_VALUE) {
                short[] indices = new short[indexList.size()];
                for (int i = 0; i < indexList.size(); ++i) {
                    indices[i] = indexList.get(i).shortValue();
                }
                mesh.setBuffer(Type.Index, 1, indices);
            } else {
                int[] indices = new int[indexList.size()];
                for (int i = 0; i < indexList.size(); ++i) {
                    indices[i] = indexList.get(i).intValue();
                }
                mesh.setBuffer(Type.Index, 1, indices);
            }
            mesh.setBuffer(verticesBuffer);
            mesh.setBuffer(verticesBind);
            if (verticesColorsBuffer != null) {
                mesh.setBuffer(Type.Color, 4, verticesColorsBuffer);
                mesh.getBuffer(Type.Color).setNormalized(true);
            }
            mesh.setBuffer(normalsBuffer);
            mesh.setBuffer(normalsBind);
            Geometry geometry = new Geometry(name + (geometries.size() + 1), mesh);
            if (properties != null && properties.getValue() != null) {
                geometry.setUserData("properties", properties);
            }
            geometries.add(geometry);
        }
        blenderContext.addLoadedFeatures(structure.getOldMemoryAddress(), structure.getName(), structure, geometries);
        blenderContext.setMeshContext(structure.getOldMemoryAddress(), meshContext);
        if (materials != null) {
            for (Geometry geometry : geometries) {
                int materialNumber = meshToMAterialMap.get(geometry.getMesh()).intValue();
                boolean noTextures = false;
                if (materialNumber < 0) {
                    materialNumber = -1 * (materialNumber + 1);
                    noTextures = true;
                }
                MaterialContext materialContext = materials[materialNumber];
                materialContext.applyMaterial(geometry, structure.getOldMemoryAddress(), noTextures, uvCoordinates.get(Integer.valueOf(materialNumber)), blenderContext);
            }
        } else {
            for (Geometry geometry : geometries) {
                geometry.setMaterial(blenderContext.getDefaultMaterial());
            }
        }
        if (geometries.size() > 1) {
            for (Geometry geom : geometries) {
                geom.getMesh().extractVertexData(geom.getMesh());
            }
        }
        return geometries;
    }

    /**
     * This method adds a normal to a normals' map. This map is used to merge normals of a vertor that should be rendered smooth.
     * 
     * @param normalToAdd
     *            a normal to be added
     * @param normalMap
     *            merges normals of faces that will be rendered smooth; the key is the vertex and the value - its normal vector
     * @param smooth
     *            the variable that indicates wheather to merge normals (creating the smooth mesh) or not
     * @param vertices
     *            a list of vertices read from the blender file
     */
    public void addNormal(Vector3f normalToAdd, Map<Vector3f, Vector3f> normalMap, boolean smooth, Vector3f... vertices) {
        for (Vector3f v : vertices) {
            Vector3f n = normalMap.get(v);
            if (!smooth || n == null) {
                normalMap.put(v, normalToAdd.clone());
            } else {
                n.addLocal(normalToAdd).normalizeLocal();
            }
        }
    }

    /**
     * This method fills the vertex reference map. The vertices are loaded once and referenced many times in the model. This map is created
     * to tell where the basic vertices are referenced in the result vertex lists. The key of the map is the basic vertex index, and its key
     * - the reference indices list.
     * 
     * @param basicVertexIndex
     *            the index of the vertex from its basic table
     * @param resultIndex
     *            the index of the vertex in its result vertex list
     * @param vertexReferenceMap
     *            the reference map
     */
    protected void appendVertexReference(int basicVertexIndex, int resultIndex, Map<Integer, List<Integer>> vertexReferenceMap) {
        List<Integer> referenceList = vertexReferenceMap.get(Integer.valueOf(basicVertexIndex));
        if (referenceList == null) {
            referenceList = new ArrayList<Integer>();
            vertexReferenceMap.put(Integer.valueOf(basicVertexIndex), referenceList);
        }
        referenceList.add(Integer.valueOf(resultIndex));
    }

    /**
     * This method returns the vertices colors. Each vertex is stored in byte[4] array.
     * 
     * @param meshStructure
     *            the structure containing the mesh data
     * @param blenderContext
     *            the blender context
     * @return a list of vertices colors, each color belongs to a single vertex
     * @throws BlenderFileException
     *             this exception is thrown when the blend file structure is somehow invalid or corrupted
     */
    public List<byte[]> getVerticesColors(Structure meshStructure, BlenderContext blenderContext) throws BlenderFileException {
        Pointer pMCol = (Pointer) meshStructure.getFieldValue("mcol");
        List<byte[]> verticesColors = null;
        List<Structure> mCol = null;
        if (pMCol.isNotNull()) {
            verticesColors = new LinkedList<byte[]>();
            mCol = pMCol.fetchData(blenderContext.getInputStream());
            for (Structure color : mCol) {
                byte r = ((Number) color.getFieldValue("r")).byteValue();
                byte g = ((Number) color.getFieldValue("g")).byteValue();
                byte b = ((Number) color.getFieldValue("b")).byteValue();
                byte a = ((Number) color.getFieldValue("a")).byteValue();
                verticesColors.add(new byte[] { b, g, r, a });
            }
        }
        return verticesColors;
    }

    /**
     * This method returns the vertices.
     * 
     * @param meshStructure
     *            the structure containing the mesh data
     * @param blenderContext
     *            the blender context
     * @return a list of vertices colors, each color belongs to a single vertex
     * @throws BlenderFileException
     *             this exception is thrown when the blend file structure is somehow invalid or corrupted
     */
    @SuppressWarnings("unchecked")
    private Vector3f[] getVertices(Structure meshStructure, BlenderContext blenderContext) throws BlenderFileException {
        int verticesAmount = ((Number) meshStructure.getFieldValue("totvert")).intValue();
        Vector3f[] vertices = new Vector3f[verticesAmount];
        if (verticesAmount == 0) {
            return vertices;
        }
        Pointer pMVert = (Pointer) meshStructure.getFieldValue("mvert");
        List<Structure> mVerts = pMVert.fetchData(blenderContext.getInputStream());
        if (this.fixUpAxis) {
            for (int i = 0; i < verticesAmount; ++i) {
                DynamicArray<Number> coordinates = (DynamicArray<Number>) mVerts.get(i).getFieldValue("co");
                vertices[i] = new Vector3f(coordinates.get(0).floatValue(), coordinates.get(2).floatValue(), -coordinates.get(1).floatValue());
            }
        } else {
            for (int i = 0; i < verticesAmount; ++i) {
                DynamicArray<Number> coordinates = (DynamicArray<Number>) mVerts.get(i).getFieldValue("co");
                vertices[i] = new Vector3f(coordinates.get(0).floatValue(), coordinates.get(1).floatValue(), coordinates.get(2).floatValue());
            }
        }
        return vertices;
    }

    @Override
    public boolean shouldBeLoaded(Structure structure, BlenderContext blenderContext) {
        return true;
    }
}
