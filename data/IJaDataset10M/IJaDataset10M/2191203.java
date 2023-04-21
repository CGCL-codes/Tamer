package org.jal3d;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jal3d.CoreSubmesh.Vertex;
import cat.arestorm.math.Vector3D;

public final class Renderer {

    private Model model;

    private Submesh selectedSubmesh;

    public Renderer(final Model model) {
        if (model == null) {
            throw new IllegalArgumentException("Renderer can not be " + "build from a null instance");
        }
        this.model = model;
        selectedSubmesh = null;
    }

    public Renderer(final Renderer renderer) {
        model = renderer.model;
        selectedSubmesh = renderer.selectedSubmesh;
    }

    /**
	 * Initializes the rendering query phase.
	 *
	 * This function initializes the rendering query phase. It must be called
	 * before any rendering queries are executed.
	 * 
	 * @return
	 */
    public boolean beginRendering() {
        Vector<Mesh> vectorMesh = model.getVectorMesh();
        if (vectorMesh.size() == 0) {
            return false;
        }
        selectedSubmesh = vectorMesh.get(0).getSubmesh(0);
        if (selectedSubmesh == null) {
            return false;
        }
        return true;
    }

    /**
	 * Finishes the rendering query phase.
	 *
	 * This function finishes the rendering query phase. It must be called
	 * after all rendering queries have been executed.
	 */
    public void endRendering() {
        selectedSubmesh = null;
    }

    /**
	 * Selects a mesh/submesh for rendering data queries.
	 *
	 * This function selects a mesh/submesh for further rendering data queries.
	 *
	 * @param meshId The ID of the mesh that should be used for further rendering
	 *               data queries.
	 * @param submeshId The ID of the submesh that should be used for further
	 *                  rendering data queries.
	 *
	 * @return One of the following values:<ul>
	 *         <li><strong>true</strong> if successful</li>
	 *         <li><strong>false</strong> if an error happened</li>
	 *         </ul>
	 */
    public boolean selectMeshSubmesh(int meshId, int submeshId) {
        Vector<Mesh> vectorMesh = model.getVectorMesh();
        if (meshId < 0 || meshId >= vectorMesh.size()) {
            return false;
        }
        selectedSubmesh = vectorMesh.get(meshId).getSubmesh(submeshId);
        if (selectedSubmesh == null) {
            return false;
        }
        return true;
    }

    /**
	 * Returns the number of attached meshes.
	 *
	 * This function returns the number of meshes attached to the renderer
	 * instance.
	 *
	 * @return The number of attached meshes.
	 */
    public int getMeshCount() {
        return model.getVectorMesh().size();
    }

    /**
	 * Returns the number of submeshes.
	 *
	 * This function returns the number of submeshes in a given mesh.
	 *
	 * @param meshId The ID of the mesh for which the number of submeshes should
	 *               be returned..
	 *
	 * @return The number of submeshes.
	 */
    public int getSubmeshCount(int meshId) {
        Vector<Mesh> vectorMesh = model.getVectorMesh();
        if ((meshId < 0) || (meshId >= vectorMesh.size())) {
            return 0;
        }
        return vectorMesh.get(meshId).getSubmeshCount();
    }

    /**
	 * Provides access to the ambient color.
	 *
	 * This function returns the ambient color of the material of the selected
	 * mesh/submesh.
	 * 
	 * @return the color data
	 */
    public CoreMaterial.Color getAmbientColor() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getAmbientColor\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            final short z = 0;
            return new CoreMaterial.Color(z, z, z, z);
        }
        return coreMaterial.getAmbientColor();
    }

    /**
	 * Provides access to the diffuse color.
	 *
	 * This function returns the diffuse color of the material of the selected
	 * mesh/submesh.
	 * 
	 * @return the color data
	 */
    public CoreMaterial.Color getDiffuseColor() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getDiffuseColor\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            final short z = 192;
            return new CoreMaterial.Color(z, z, z, z);
        }
        return coreMaterial.getDiffuseColor();
    }

    /**
	 * Provides access to the specular color.
	 *
	 * This function returns the specular color of the material of the selected
	 * mesh/submesh.
	 * 
	 * @return the color data
	 */
    public CoreMaterial.Color getSpecularColor() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getSpecularColor\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            final short z = 255;
            final short z2 = 0;
            return new CoreMaterial.Color(z, z, z, z2);
        }
        return coreMaterial.getSpecularColor();
    }

    /**
	 * Returns the shininess factor.
	 *
	 * This function returns the shininess factor of the material of the selected
	 * mesh/submesh..
	 *
	 * @return The shininess factor.
	 */
    public float getShininess() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            return 50.0f;
        }
        return coreMaterial.getShininess();
    }

    /**
	 * Returns the number of faces.
	 *
	 * This function returns the number of faces in the selected mesh/submesh.
	 *
	 * @return The number of faces.
	 */
    public int getFaceCount() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        return selectedSubmesh.getFaceCount();
    }

    /**
	 * Provides access to the face data.
	 *
	 * This function returns the face data (vertex indices) of the selected
	 * mesh/submesh. The LOD setting is taken into account.
	 * 
	 * @return a buffer with the face data.
	 */
    public int[] getFaces() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        return selectedSubmesh.getFaces();
    }

    /**
	 * Returns the number of maps.
	 *
	 * This function returns the number of maps in the selected mesh/submesh.
	 *
	 * @return The number of maps.
	 */
    public int getMapCount() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getMapCount\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            return 0;
        }
        return coreMaterial.getMapCount();
    }

    /**
	 * Provides access to a specified map user data.
	 *
	 * This function returns the user data stored in the specified map of the
	 * material of the selected mesh/submesh.
	 *
	 * @param mapId The ID of the map.
	 *
	 * @return One of the following values:<ul>
	 *         <li> the user data stored in the specified map</li>
	 *         <li><strong>null</strong> if an error happened</li>
	 *         </ul>
	 */
    public UserData getMapUserData(int mapId) {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getMapCount\"");
        }
        CoreMaterial coreMaterial;
        coreMaterial = model.getCoreModel().getCoreMaterial(selectedSubmesh.getCoreMaterialId());
        if (coreMaterial == null) {
            return null;
        }
        Vector<CoreMaterial.Map> vectorMap = coreMaterial.getVectorMap();
        if (mapId < 0 || mapId >= (int) vectorMap.size()) {
            return null;
        }
        return vectorMap.get(mapId).userData;
    }

    /**
	 * Provides access to the normal data.
	 *
	 * This function returns the normal data of the selected mesh/submesh.
	 *
	 * @param buffer 
	 *
	 * @param stride
	 * 
	 * @return The number of normals written to the buffer.
	 */
    public int getNormals(final FloatBuffer buffer, int stride) {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        if (selectedSubmesh.hasInternalData()) {
            Vector<Vector3D> vectorNormal = selectedSubmesh.getVectorNormal();
            if (stride <= 0 || stride == 3) {
                for (Vector3D normal : vectorNormal) {
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                }
            } else {
                final int espai = stride - 3;
                for (Vector3D normal : vectorNormal) {
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                    buffer.position(buffer.position() + espai);
                }
            }
            return vectorNormal.size();
        }
        return model.getPhysique().calculateNormals(selectedSubmesh, buffer, stride);
    }

    public int getNormals(final FloatBuffer buffer) {
        return getNormals(buffer, 0);
    }

    /**
	 * Provides access to the texture coordinate data.
	 *
	 * This function returns the texture coordinate data for a given map of the
	 * selected mesh/submesh.
	 *
	 * @param mapId The ID of the map to get the texture coordinate data from.
	 * @param buffer A pointer to the user-provided buffer where
	 *                    the texture coordinate data is written to.
	 * @param stride
	 *
	 * @return The number of texture coordinates written to the buffer.
	 */
    public int getTextureCoordinates(int mapId, final FloatBuffer buffer, int stride) {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        List<Vertex> vectorVertex = selectedSubmesh.getCoreSubmesh().getVectorVertex();
        if (mapId < 0 || mapId >= vectorVertex.get(0).textureCoordinates.size()) {
            return -1;
        }
        if (stride == 0 || stride == CoreSubmesh.TextureCoordinate.SIZE) {
            for (Vertex v : vectorVertex) {
                buffer.put(v.textureCoordinates.get(mapId).toBuffer());
            }
        } else {
            final int espai = stride - CoreSubmesh.TextureCoordinate.SIZE;
            for (Vertex v : vectorVertex) {
                buffer.put(v.textureCoordinates.get(mapId).toBuffer());
                buffer.position(buffer.position() + espai);
            }
        }
        return vectorVertex.size();
    }

    public int getTextureCoordinates(int mapId, final FloatBuffer buffer) {
        return getTextureCoordinates(mapId, buffer, 0);
    }

    /**
	 * Returns the number of vertices.
	 *
	 * This function returns the number of vertices in the selected mesh/submesh.
	 *
	 * @return The number of vertices.
	 */
    public int getVertexCount() {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        return selectedSubmesh.getVertexCount();
    }

    /**
	 * Provides access to the vertex data.
	 *
	 * This function returns the vertex data of the selected mesh/submesh.
	 *
	 * @param buffer A pointer to the user-provided buffer where the vertex
	 *                      data is written to.
	 *
	 * @param stride
	 * 
	 * @return The number of vertices written to the buffer.
	 * @return
	 */
    public int getVertices(final FloatBuffer buffer, int stride) {
        if (selectedSubmesh.hasInternalData()) {
            Vector<Vector3D> vectorVertex = selectedSubmesh.getVectorVertex();
            if (stride <= 0 || stride == 3) {
                for (Vector3D v : vectorVertex) {
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                }
            } else {
                final int espai = stride - 3;
                for (Vector3D v : vectorVertex) {
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                    buffer.position(buffer.position() + espai);
                }
            }
            return vectorVertex.size();
        }
        return model.getPhysique().calculateVertices(selectedSubmesh, buffer, stride);
    }

    public int getVertices(final FloatBuffer buffer) {
        return getVertices(buffer, 0);
    }

    /**
	 * Provides access to the tangent space data.
	 *
	 * This function returns the tangent space data of the selected mesh/submesh.
	 * 
	 * It is writted in the form of:
	 *  - #0 normal.x
	 *  - #1 normal.y
	 *  - #2 normal.z
	 *  - #3 crossFactor
	 *  
	 *  So, each tangent space occupies 4 floats.
	 *
	 * @param mapId
	 * @param buffer buffer where the information will be writted.
	 * @param stride number of floats between 2 tangentSpace information
	 * @return
	 */
    public int getTangentSpaces(int mapId, final FloatBuffer buffer, int stride) {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        List<Vector<Submesh.TangentSpace>> vectorvectorTangentSpace;
        vectorvectorTangentSpace = selectedSubmesh.getVectorVectorTangentSpace();
        if (mapId < 0 || mapId >= vectorvectorTangentSpace.size()) {
            return -1;
        }
        if (selectedSubmesh.hasInternalData()) {
            buffer.rewind();
            Vector<Submesh.TangentSpace> vectorTangentSpace;
            vectorTangentSpace = vectorvectorTangentSpace.get(mapId);
            if (stride <= 0 || stride == 4) {
                for (Submesh.TangentSpace v : vectorTangentSpace) {
                    buffer.put(v.tangent.getX());
                    buffer.put(v.tangent.getY());
                    buffer.put(v.tangent.getZ());
                    buffer.put(v.crossFactor);
                }
            } else {
                final int espai = stride - 4;
                for (Submesh.TangentSpace v : vectorTangentSpace) {
                    buffer.put(v.tangent.getX());
                    buffer.put(v.tangent.getY());
                    buffer.put(v.tangent.getZ());
                    buffer.put(v.crossFactor);
                    buffer.position(buffer.position() + espai);
                }
            }
            return vectorTangentSpace.size();
        }
        return model.getPhysique().calculateTangentSpaces(selectedSubmesh, mapId, buffer, stride);
    }

    public int getTangentSpaces(int mapId, final FloatBuffer buffer) {
        return getTangentSpaces(mapId, buffer, 0);
    }

    public int getVerticesAndNormals(final FloatBuffer buffer, int stride) {
        if (selectedSubmesh.hasInternalData()) {
            Iterator<Vector3D> iteratorVertex = selectedSubmesh.getVectorVertex().iterator();
            Iterator<Vector3D> iteratorNormal = selectedSubmesh.getVectorNormal().iterator();
            if (stride <= 0 || stride == 6) {
                while (iteratorVertex.hasNext()) {
                    Vector3D v = iteratorVertex.next();
                    Vector3D normal = iteratorNormal.next();
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                }
            } else {
                final int espai = stride - 6;
                while (iteratorVertex.hasNext()) {
                    Vector3D v = iteratorVertex.next();
                    Vector3D normal = iteratorNormal.next();
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                    buffer.position(buffer.position() + espai);
                }
            }
            return selectedSubmesh.getVectorVertex().size();
        }
        return model.getPhysique().calculateVerticesAndNormals(selectedSubmesh, buffer, stride);
    }

    public int getVerticesAndNormals(final FloatBuffer buffer) {
        return getVerticesAndNormals(buffer, 0);
    }

    public int getVerticesNormalsAndTexCoords(final FloatBuffer buffer, int numTexCoords, int stride) {
        if (selectedSubmesh.hasInternalData()) {
            Iterator<Vector3D> iteratorVertex = selectedSubmesh.getVectorVertex().iterator();
            Iterator<Vector3D> iteratorNormal = selectedSubmesh.getVectorNormal().iterator();
            Iterator<Vertex> iteratorVertexTex = selectedSubmesh.getCoreSubmesh().getVectorVertex().iterator();
            if (stride <= 0 || stride == 6 + numTexCoords * 2) {
                while (iteratorVertex.hasNext()) {
                    Vector3D v = iteratorVertex.next();
                    Vector3D normal = iteratorNormal.next();
                    Vector<CoreSubmesh.TextureCoordinate> texCoords = iteratorVertexTex.next().textureCoordinates;
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                    for (int i = 0; i < numTexCoords; i++) {
                        CoreSubmesh.TextureCoordinate t = texCoords.get(i);
                        buffer.put(t.u);
                        buffer.put(t.v);
                    }
                }
            } else {
                final int espai = stride - 6 - numTexCoords * 2;
                while (iteratorVertex.hasNext()) {
                    Vector3D v = iteratorVertex.next();
                    Vector3D normal = iteratorNormal.next();
                    Vector<CoreSubmesh.TextureCoordinate> texCoords = iteratorVertexTex.next().textureCoordinates;
                    buffer.put(v.getX());
                    buffer.put(v.getY());
                    buffer.put(v.getZ());
                    buffer.put(normal.getX());
                    buffer.put(normal.getY());
                    buffer.put(normal.getZ());
                    for (int i = 0; i < numTexCoords; i++) {
                        CoreSubmesh.TextureCoordinate t = texCoords.get(i);
                        buffer.put(t.u);
                        buffer.put(t.v);
                    }
                    buffer.position(buffer.position() + espai);
                }
            }
            return selectedSubmesh.getVectorVertex().size();
        }
        return model.getPhysique().calculateVerticesAndNormalsAndTexCoords(selectedSubmesh, buffer, numTexCoords, stride);
    }

    public int getVerticesNormalsAndTexCoords(final FloatBuffer buffer, int numTexCoords) {
        return getVerticesNormalsAndTexCoords(buffer, numTexCoords, 0);
    }

    public int getVerticesNormalsAndTexCoords(final FloatBuffer buffer) {
        return getVerticesNormalsAndTexCoords(buffer, 1, 0);
    }

    /**
	 * Returns if tangent are enabled.
	 *
	 * This function returns if tangent of the current submesh are enabled
	 *
	 * @param mapId
	 * 
	 * @return True is tangent is enabled.
	 */
    public boolean isTangentsEnabled(int mapId) {
        if (selectedSubmesh == null) {
            throw new IllegalStateException("beginRendering must be called " + "before \"getShininess\"");
        }
        return selectedSubmesh.isTangentsEnabled(mapId);
    }

    /**
	 * Sets the normalization flag to true or false.
	 *
	 * This function sets the normalization flag on or off. If off, the normals
	 * calculated by Cal3D will not be normalized. Instead, this transform is left
	 * up to the user.
	 * 
	 * @param normalize
	 */
    public void setNormalization(boolean normalize) {
        model.getPhysique().setNormalization(normalize);
    }
}
