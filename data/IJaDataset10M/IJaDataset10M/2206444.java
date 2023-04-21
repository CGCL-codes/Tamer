package skycastle.sculptor;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.VBOInfo;
import com.jme.scene.state.AlphaState;
import com.jme.util.geom.BufferUtils;
import skycastle.util.MathUtils;
import skycastle.util.NumberUtils;
import skycastle.jmeutils.VectorMathUtils;
import skycastle.util.texture.*;
import skycastle.jmeutils.TextureService;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * TODO: Have different tri meshes for different textures?
 *
 * @author Hans H�ggstr�m
 */
public class MeshImpl implements Mesh {

    private final List<com.jme.math.Vector3f> myVertexes = new ArrayList<com.jme.math.Vector3f>(INITIAL_VERTEX_CAPACITY);

    private final List<com.jme.math.Vector3f> myNormals = new ArrayList<com.jme.math.Vector3f>(INITIAL_VERTEX_CAPACITY);

    private final List<Vector2f> myTextureCoordinates = new ArrayList<Vector2f>(INITIAL_VERTEX_CAPACITY);

    private final List<ColorRGBA> myColors = new ArrayList<ColorRGBA>(INITIAL_VERTEX_CAPACITY);

    private final List<Integer> myIndexes = new ArrayList<Integer>(INITIAL_INDEX_CAPACITY);

    private final Vector3f myTempVertexLocation = new Vector3f();

    private final TextureTemplateService myTextureTemplateService;

    private final Vector3f myTempVertexNormal = new Vector3f();

    private TextureTemplateSection myCurrentTextureTemplateSection = null;

    private ResourceId myCurrentTextureId = new ResourceIdImpl("wall");

    private String myName = null;

    private boolean myOneBitTransparency;

    private Stack<Matrix4f> myTransformations = new Stack<Matrix4f>();

    private Matrix4f myCurrentTransformation = null;

    private static final com.jme.math.Vector3f[] VECTOR_ARRAY_TYPE_MARKER = new com.jme.math.Vector3f[0];

    private static final ColorRGBA[] COLOR_ARRAY_TYPE_MARKER = new ColorRGBA[0];

    private static final Vector2f[] VECTOR2F_ARRAY_TYPE_MARKER = new Vector2f[0];

    private static final int INITIAL_VERTEX_CAPACITY = 50;

    private static final int INITIAL_INDEX_CAPACITY = 100;

    @SuppressWarnings({ "MagicNumber" })
    private static final ColorRGBA DEFAULT_COLOR = new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f);

    private static final Vector2f DEFAULT_TEXTURE_COORDINATE = new Vector2f(0, 0);

    private static final double HALF_CIRCLE = Math.PI;

    private static final double FULL_CIRCLE = 2 * HALF_CIRCLE;

    private final Vector3f myTempNormal = new Vector3f();

    private final Point3f myTempPoint = new Point3f();

    /**
     * @param textureTemplateService a textureTemplateService, that can be used to retrieve TextureSections based on texture resource ID:s
     */
    public MeshImpl(final TextureTemplateService textureTemplateService) {
        myTextureTemplateService = textureTemplateService;
        updateTransformation();
    }

    /**
     * @return the name of this mesh, for debugging purposes.
     */
    public String getName() {
        return myName;
    }

    public String getIdentifier() {
        return myName + "_" + hashCode();
    }

    public void setName(String name) {
        myName = name;
    }

    @SuppressWarnings({ "ValueOfIncrementOrDecrementUsed" })
    public VertexRing createVertexRing(final Vector3f center, final float radius, final Vector3f directionVector, final int numberOfSides, final int numberOfTextureWraps, final float relativeTextureVCoordinate, final ColorRGBA color) {
        if (numberOfTextureWraps < 1) {
            throw new IllegalArgumentException("The number of texture wraps should be greater or equal to one, but it was " + numberOfTextureWraps);
        }
        final int numberOfVertexes = numberOfSides + numberOfTextureWraps - 1;
        final double textureUAdvancePerSide = (1.0 * numberOfTextureWraps) / (1.0f * numberOfSides);
        float relativeTextureUCoordinate = 0;
        int[] vertexes = new int[numberOfVertexes];
        int vertexIndex = 0;
        for (int i = 0; i < numberOfSides; i++) {
            double angle_radians = MathUtils.interpolate(i, 0, numberOfSides, 0, (float) FULL_CIRCLE);
            VectorMathUtils.calculateNormalAtAngleAroundDirectionvector(directionVector, new Vector3f(1, 0, 0), angle_radians, myTempVertexNormal);
            myTempVertexLocation.scaleAdd(-radius, myTempVertexNormal, center);
            vertexes[vertexIndex++] = addVertex(myTempVertexLocation, myTempVertexNormal, relativeTextureUCoordinate, relativeTextureVCoordinate, color);
            relativeTextureUCoordinate += textureUAdvancePerSide;
            if (relativeTextureUCoordinate - textureUAdvancePerSide * 0.5f > 1) {
                if (i == numberOfSides - 2) {
                    relativeTextureUCoordinate = 1;
                } else {
                    relativeTextureUCoordinate = 0;
                    vertexes[vertexIndex++] = addVertex(myTempVertexLocation, myTempVertexNormal, relativeTextureUCoordinate, relativeTextureVCoordinate, color);
                    relativeTextureUCoordinate += textureUAdvancePerSide;
                }
            }
        }
        if (vertexIndex != numberOfVertexes) {
            System.out.println("MeshImpl.createVertexRing  -- missmatching vertex numbers");
            System.out.println("vertexIndex = " + vertexIndex);
            System.out.println("numberOfVertexes = " + numberOfVertexes);
        }
        return new VertexRingImpl(vertexes);
    }

    public void buildQuad(final int index0, final int index1, final int index2, final int index3) {
        buildTriangle(index0, index1, index2);
        buildTriangle(index2, index3, index0);
    }

    public void buildQuad(final Vector3f topLeft, final Vector3f bottomLeft, final Vector3f topRight, final Vector3f bottomRight, float leftTextureU, float rightTextureU, float upperTextureV, float bottomTextureV, final boolean twoSided) {
        Vector3f leftNormal;
        Vector3f rightNormal;
        if (twoSided) {
            leftNormal = new Vector3f(topRight);
            leftNormal.sub(topLeft);
            leftNormal.normalize();
            rightNormal = new Vector3f(leftNormal);
            rightNormal.scale(-1);
        } else {
            leftNormal = VectorMathUtils.calculateNormal(topLeft, bottomLeft, bottomRight);
            rightNormal = leftNormal;
        }
        final int upperLeftIndex = addVertex(topLeft, leftNormal, leftTextureU, upperTextureV);
        final int lowerLeftIndex = addVertex(bottomLeft, leftNormal, leftTextureU, bottomTextureV);
        final int upperRightIndex = addVertex(topRight, rightNormal, rightTextureU, upperTextureV);
        final int lowerRightIndex = addVertex(bottomRight, rightNormal, rightTextureU, bottomTextureV);
        buildQuad(upperLeftIndex, lowerLeftIndex, lowerRightIndex, upperRightIndex);
        if (twoSided) {
            buildQuad(upperRightIndex, lowerRightIndex, lowerLeftIndex, upperLeftIndex);
        }
    }

    public void buildTriangle(final int index0, final int index1, final int index2) {
        myIndexes.add(index0);
        myIndexes.add(index1);
        myIndexes.add(index2);
    }

    public int addVertex(Vector3f pos, Vector3f normal, float textureUCoordinate, float textureVCoordinate) {
        return addVertex(pos, normal, textureUCoordinate, textureVCoordinate, ColorRGBA.lightGray);
    }

    public int addVertex(Vector3f pos, Vector3f normal, float textureUCoordinate, float textureVCoordinate, ColorRGBA color) {
        return addVertex(pos.x, pos.y, pos.z, normal.x, normal.y, normal.z, textureUCoordinate, textureVCoordinate, color);
    }

    public int addVertex(final float x, final float y, final float z, final float normalX, final float normalY, final float normalZ, float textureUCoordinate, float textureVCoordinate, final ColorRGBA color) {
        int index = myVertexes.size();
        com.jme.math.Vector3f normal = new com.jme.math.Vector3f(normalX, normalY, normalZ);
        transformNormal(normal);
        normal = normal.normalize();
        if (myCurrentTextureTemplateSection != null) {
            textureUCoordinate = myCurrentTextureTemplateSection.getTextureUCoordinate(textureUCoordinate);
            textureVCoordinate = myCurrentTextureTemplateSection.getTextureVCoordinate(textureVCoordinate);
        }
        final com.jme.math.Vector3f position = new com.jme.math.Vector3f(x, y, z);
        transformPoint(position);
        myVertexes.add(position);
        myNormals.add(normal);
        myTextureCoordinates.add(new Vector2f(textureUCoordinate, textureVCoordinate));
        myColors.add(color);
        return index;
    }

    /**
     * Transforms the specified position with the current transformation.
     */
    private void transformPoint(final com.jme.math.Vector3f point) {
        myTempPoint.set(point.x, point.y, point.z);
        myCurrentTransformation.transform(myTempPoint);
        point.set(myTempPoint.x, myTempPoint.y, myTempPoint.z);
    }

    /**
     * Transforms the specified normal with the current transformation.
     */
    private void transformNormal(final com.jme.math.Vector3f normal) {
        myTempNormal.set(normal.x, normal.y, normal.z);
        myCurrentTransformation.transform(myTempNormal);
        normal.set(myTempNormal.x, myTempNormal.y, myTempNormal.z);
    }

    public void connectVertexRings(VertexRing firstRing, VertexRing secondRing) {
        if (firstRing.getNumberOfIndexes() != secondRing.getNumberOfIndexes()) {
            throw new UnsupportedOperationException("Connecting two bvertex rings with different number of vertexes is not yet suported.");
        }
        final int numberOfIndexes = firstRing.getNumberOfIndexes();
        for (int i = 0; i < numberOfIndexes; i++) {
            if (firstRing.getConnectionFlagWithWraparound(i)) {
                buildQuad(firstRing.getIndexWithWraparound(i), firstRing.getIndexWithWraparound(i + 1), secondRing.getIndexWithWraparound(i + 1), secondRing.getIndexWithWraparound(i));
                if (firstRing.isTwoSided()) {
                    buildQuad(firstRing.getIndexWithWraparound(i + 1), firstRing.getIndexWithWraparound(i), secondRing.getIndexWithWraparound(i), secondRing.getIndexWithWraparound(i + 1));
                }
            }
        }
    }

    public void setCurrentTextureSection(final ResourceId textureSectionId) {
        setCurrentTextureSection(myTextureTemplateService.getTextureSection(textureSectionId));
    }

    public void setCurrentTextureSection(final String textureTemplateIdentifier, final String textureSectionidentifier, final String textureIdentifier) {
        myCurrentTextureId = myTextureTemplateService.getTextureId(textureTemplateIdentifier, textureSectionidentifier, textureIdentifier);
        myCurrentTextureTemplateSection = myTextureTemplateService.getTemplateSection(textureTemplateIdentifier, textureSectionidentifier);
    }

    public void setTransparency(boolean transparent) {
        myOneBitTransparency = transparent;
    }

    public void push(final Matrix4f transformation) {
        if (transformation == null) {
            throw new IllegalArgumentException("The parameter transformation should not be null.");
        }
        myTransformations.push(transformation);
        updateTransformation();
    }

    public void pop(final Matrix4f transformation) {
        if (myTransformations.pop() != transformation) {
            throw new IllegalStateException("Unexpected transformation on the transformation stack, check that the push and pop calls match.");
        }
        updateTransformation();
    }

    public Spatial createSpatial(TextureService textureService) {
        final TriMesh triMesh = new TriMesh(getName(), BufferUtils.createFloatBuffer(getVertexes()), BufferUtils.createFloatBuffer(getNormals()), BufferUtils.createFloatBuffer(getColors()), BufferUtils.createFloatBuffer(getTextureCoordinates()), BufferUtils.createIntBuffer(getIndices()));
        triMesh.setModelBound(new BoundingBox());
        triMesh.updateModelBound();
        triMesh.setRenderState(textureService.createTextureState(myCurrentTextureId.getIdentifier()));
        if (myOneBitTransparency) {
            final AlphaState alphaState = textureService.getDisplaySystem().getRenderer().createAlphaState();
            alphaState.setTestEnabled(true);
            alphaState.setTestFunction(AlphaState.TF_GREATER);
            alphaState.setEnabled(true);
            triMesh.setRenderState(alphaState);
        }
        triMesh.setVBOInfo(new VBOInfo(true));
        return triMesh;
    }

    /**
     * @param currentTextureTemplateSection the TextureTemplateSection to use for newly added geometry.
     */
    public void setCurrentTextureSection(TextureTemplateSection currentTextureTemplateSection) {
        myCurrentTextureTemplateSection = currentTextureTemplateSection;
    }

    /**
     * @return the TextureTemplateSection to use for newly added geometry.
     */
    public TextureTemplateSection getCurrentTextureSection() {
        return myCurrentTextureTemplateSection;
    }

    /**
     * @return the resource ID for the texture to use for this mesh.
     */
    public ResourceId getCurrentTextureId() {
        return myCurrentTextureId;
    }

    /**
     * @param textureId the resource ID for the texture to use for this mesh.
     */
    public void setCurrentTextureId(ResourceId textureId) {
        myCurrentTextureId = textureId;
    }

    /**
     * @return a textureSectionService, that can be used to retrieve TextureSections based on texture resource ID:s
     */
    public TextureTemplateService getTextureSectionService() {
        return myTextureTemplateService;
    }

    private void updateTransformation() {
        if (myCurrentTransformation == null) {
            myCurrentTransformation = new Matrix4f();
        }
        myCurrentTransformation.setIdentity();
        for (Matrix4f matrix4f : myTransformations) {
            myCurrentTransformation.mul(matrix4f);
        }
    }

    /**
     * @return the points in 3D space that the mesh is using.
     */
    private com.jme.math.Vector3f[] getVertexes() {
        return myVertexes.toArray(VECTOR_ARRAY_TYPE_MARKER);
    }

    /**
     * @return the normal vector for each vertex.  Used for gouraud shading, etc.
     */
    private com.jme.math.Vector3f[] getNormals() {
        return myNormals.toArray(VECTOR_ARRAY_TYPE_MARKER);
    }

    /**
     * @return the color of each vertex.
     */
    private ColorRGBA[] getColors() {
        return myColors.toArray(COLOR_ARRAY_TYPE_MARKER);
    }

    /**
     * @return UV coordinates of the used texture at each vertex.
     */
    private Vector2f[] getTextureCoordinates() {
        return myTextureCoordinates.toArray(VECTOR2F_ARRAY_TYPE_MARKER);
    }

    /**
     * @return a list of indexes into the vertex list, each consecutive three index numbers specify a triangle to be created.
     */
    private int[] getIndices() {
        return NumberUtils.convertIntegerListToIntArray(myIndexes);
    }
}
