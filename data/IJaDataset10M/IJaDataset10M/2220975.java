package org.xith3d.loaders.models.impl.cal3d;

import java.io.File;
import javax.vecmath.Color4f;
import org.xith3d.loaders.models.impl.cal3d.buffer.TexCoord2fBuffer;
import org.xith3d.loaders.models.impl.cal3d.buffer.Vector3fBuffer;
import org.xith3d.loaders.models.impl.cal3d.core.CalCoreMaterial;
import org.xith3d.loaders.models.impl.cal3d.core.CalModel;
import org.xith3d.loaders.models.impl.cal3d.core.CalSubmesh;
import org.xith3d.loaders.models.impl.cal3d.core.CalCoreSubmesh.Face;
import org.xith3d.loaders.models.impl.cal3d.util.BufferToArray;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loaders.texture.TextureStreamLocatorURL;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.GeometryArray;
import org.xith3d.scenegraph.GeometryUpdater;
import org.xith3d.scenegraph.IndexedTriangleArray;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Texture;

/**
 * @author Dave Lloyd
 * @author kman
 * @author Amos Wenger (aka BlueSky)
 */
public class Cal3dSubmesh extends Shape3D implements GeometryUpdater {

    protected IndexedTriangleArray ta;

    protected CalSubmesh subMesh;

    private CalModel calModel;

    private int oldNumVertices;

    /** Creates a new instance of Cal3dSubmesh */
    public Cal3dSubmesh() {
        this("");
    }

    public Cal3dSubmesh(String name) {
        super();
        setName(name);
        this.setAppearance(new Appearance());
    }

    public Cal3dSubmesh(String name, CalSubmesh submesh) {
        this(name);
        this.subMesh = submesh;
        constructSubMesh();
    }

    public CalSubmesh getSubMesh() {
        return subMesh;
    }

    public void setSubMesh(CalSubmesh subMesh) {
        this.subMesh = subMesh;
        constructSubMesh();
    }

    private void constructSubMesh() {
        if (subMesh.hasInternalData()) {
            Vector3fBuffer vertexBuffer2 = new Vector3fBuffer(subMesh.getVertexCount());
            Vector3fBuffer normalBuffer2 = new Vector3fBuffer(subMesh.getVertexCount());
            ta = new IndexedTriangleArray(vertexBuffer2.length, GeometryArray.COORDINATES | GeometryArray.NORMALS, subMesh.getFaceIndices().size());
            this.setGeometry(ta);
            ta.setCoordinates(0, BufferToArray.array(vertexBuffer2.getBuffer()));
            ta.setNormals(0, BufferToArray.array(normalBuffer2.getBuffer()));
            ta.setIndex(BufferToArray.array(subMesh.getFaceIndices().getBuffer()));
        } else {
            @SuppressWarnings("unused") int numVertices = subMesh.getCoreSubmesh().getVertexCount();
            Vector3fBuffer vertexBuffer2 = subMesh.getCoreSubmesh().getVertexPositions();
            Vector3fBuffer normalBuffer2 = subMesh.getCoreSubmesh().getVertexNormals();
            TexCoord2fBuffer[] textBuffer = subMesh.getCoreSubmesh().getTextureCoordinates();
            int[] faces = new int[subMesh.getCoreSubmesh().getFaceCount() * 3];
            int cc = 0;
            for (int p = 0; p < subMesh.getCoreSubmesh().getFaceCount(); p++) {
                Face aface = subMesh.getCoreSubmesh().getVectorFace()[p];
                faces[cc] = aface.vertexId[0];
                cc++;
                faces[cc] = aface.vertexId[1];
                cc++;
                faces[cc] = aface.vertexId[2];
                cc++;
            }
            if (textBuffer == null || textBuffer.length == 0) {
                ta = new IndexedTriangleArray(vertexBuffer2.length, GeometryArray.COORDINATES | GeometryArray.NORMALS, faces.length);
                this.setGeometry(ta);
                ta.setCoordinates(0, BufferToArray.array(vertexBuffer2.getBuffer()));
                ta.setNormals(0, BufferToArray.array(normalBuffer2.getBuffer()));
                ta.setIndex(faces);
            } else {
                ta = new IndexedTriangleArray(vertexBuffer2.length, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2, faces.length);
                this.setGeometry(ta);
                ta.setCoordinates(0, BufferToArray.array(vertexBuffer2.getBuffer()));
                ta.setNormals(0, BufferToArray.array(normalBuffer2.getBuffer()));
                for (int i = 0; i < textBuffer.length; i++) {
                    ta.setTextureCoordinates(i, 0, BufferToArray.array(textBuffer[i].getBuffer()));
                }
                ta.setIndex(faces);
            }
        }
        CalCoreMaterial mat = subMesh.getCoreMaterial();
        if (mat != null) {
            Color4f ambient = mat.getAmbientColor();
            Color4f diffuse = mat.getDiffuseColor();
            Color4f specular = mat.getSpecularColor();
            float shine = mat.getShininess();
            Material mstate = new Material();
            mstate.setShininess(shine);
            mstate.setAmbientColor(ambient.x, ambient.y, ambient.z);
            mstate.setDiffuseColor(diffuse.x, diffuse.y, diffuse.z);
            mstate.setSpecularColor(specular.x, specular.y, specular.z);
            getAppearance().setMaterial(mstate);
            if (mat.getMapCount() > 0) {
                String filename = mat.getMapFilename(0);
                while (filename.startsWith(File.separator)) {
                    filename = filename.substring(1);
                }
                Texture tex = null;
                File f = new File(filename);
                if (f.exists()) {
                    tex = TextureLoader.getInstance().getTexture(filename);
                }
                if (tex == null) {
                    System.out.println("Path = " + filename);
                    TextureStreamLocatorURL loc = new TextureStreamLocatorURL(mat.getBaseURL());
                    TextureLoader.getInstance().addTextureStreamLocator(loc);
                    tex = TextureLoader.getInstance().getTexture(filename);
                    TextureLoader.getInstance().removeTextureStreamLocator(loc);
                }
                getAppearance().setTexture(tex);
            }
        }
    }

    public void doUpdate(CalModel calModel) {
        this.calModel = calModel;
        ta.updateData(this);
    }

    public void updateData(Geometry geom) {
        if (subMesh.hasInternalData()) {
            Vector3fBuffer vertexBuffer2 = subMesh.getVertexPositions();
            Vector3fBuffer normalBuffer2 = subMesh.getVertexNormals();
            ta.setNormals(0, BufferToArray.array(normalBuffer2.getBuffer()));
            ta.setCoordinates(0, BufferToArray.array(vertexBuffer2.getBuffer()));
            setGeometry(ta);
            setBoundsDirty();
        } else {
            Vector3fBuffer vertexBuffer2 = new Vector3fBuffer(subMesh.getVertexCount());
            Vector3fBuffer normalBuffer2 = new Vector3fBuffer(subMesh.getVertexCount());
            int numVertices = calModel.getPhysique().calculateVertices(subMesh, vertexBuffer2);
            @SuppressWarnings("unused") int numNormals = calModel.getPhysique().calculateNormals(subMesh, normalBuffer2);
            if (numVertices != oldNumVertices) {
                ta = new IndexedTriangleArray(numVertices, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2, subMesh.getFaceIndices().size());
                oldNumVertices = numVertices;
            }
            ta.setCoordinates(0, BufferToArray.array(vertexBuffer2.getBuffer()));
            ta.setNormals(0, BufferToArray.array(normalBuffer2.getBuffer()));
            ta.setIndex(BufferToArray.array(subMesh.getFaceIndices().getBuffer()));
            for (int i = 0; i < subMesh.getCoreSubmesh().getTextureCoordinates().length; i++) {
                ta.setTextureCoordinates(i, 0, BufferToArray.array(subMesh.getCoreSubmesh().getTextureCoordinates()[i].getBuffer()));
            }
            setGeometry(ta);
            setBoundsDirty();
        }
    }

    /**
     * @return a Shape3D representing the current state
     */
    public Shape3D getShape3D() {
        IndexedTriangleArray geom = new IndexedTriangleArray(ta.getVertexCount(), ta.getVertexFormat(), ta.getIndex().length);
        geom.setCoordinates(0, ta.getCoordRefFloat());
        geom.setNormals(0, ta.getNormalRefFloat());
        for (int i = 0; i < geom.getTexCoordSetCount(); i++) {
            geom.setTextureCoordinates(i, 0, ta.getTexCoordRefFloat(i));
        }
        geom.setIndex(ta.getIndex());
        geom.calculateFaceNormals();
        Shape3D shape = new Shape3D(geom, getAppearance(), Types.STATIC);
        return shape;
    }
}
