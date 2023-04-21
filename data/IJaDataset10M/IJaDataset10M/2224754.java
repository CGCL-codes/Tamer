package espresso3d.engine.lowlevel.geometry;

import java.nio.FloatBuffer;
import sun.security.provider.certpath.Vertex;
import espresso3d.engine.E3DEngine;
import espresso3d.engine.common.E3DConstants;
import espresso3d.engine.lowlevel.tree.base.IE3DHashableNode;
import espresso3d.engine.lowlevel.vector.E3DVector2F;
import espresso3d.engine.lowlevel.vector.E3DVector3F;
import espresso3d.engine.lowlevel.vector.E3DVector4F;
import espresso3d.engine.renderer.base.E3DBlendMode;
import espresso3d.engine.renderer.base.E3DRenderMode;
import espresso3d.engine.renderer.base.E3DRenderable;
import espresso3d.engine.renderer.base.E3DTexturedRenderable;
import espresso3d.engine.renderer.rendertree.E3DRenderTree;
import espresso3d.engine.renderer.texture.E3DTexture;
import espresso3d.engine.world.sector.actor.E3DActor;

/**
 * @author espresso3d
 *
 * Triangle class used in the engine.
 * 
 * Be sure to ONLY modify vertex position information through the triangle object itself -- IE: do NOT get the vertex and modify the vertex by hand.  
 * It will cause normals to NOT be recalculated if you do so and will cause lots of problems.
 */
public class E3DTriangle extends E3DTexturedRenderable implements IE3DHashableNode {

    private E3DRenderable parentObject;

    private E3DTexturedVertex[] vertices;

    private double alpha = 1.0;

    private E3DVector3F normal;

    private E3DVector4F planeEquationCoords;

    private boolean needNormalRecalc = false;

    private boolean needPlaneEquationRecalc = false;

    public E3DTriangle(E3DEngine engine) {
        super(engine, null);
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(engine, new E3DVector3F(0.0, 0.0, 0.0), new E3DVector4F(1.0, 1.0, 1.0, 1.0), new E3DVector2F(0.0, 0.0));
        vertices[1] = new E3DTexturedVertex(engine, new E3DVector3F(0.0, 0.0, 0.0), new E3DVector4F(1.0, 1.0, 1.0, 1.0), new E3DVector2F(0.0, 0.0));
        vertices[2] = new E3DTexturedVertex(engine, new E3DVector3F(0.0, 0.0, 0.0), new E3DVector4F(1.0, 1.0, 1.0, 1.0), new E3DVector2F(0.0, 0.0));
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public E3DTriangle(E3DTriangle toCopy, E3DRenderable newParentObject) {
        super(toCopy.getEngine(), new E3DRenderMode(toCopy.getRenderMode()), new E3DBlendMode(toCopy.getBlendMode()), toCopy.getTexture().getTextureName(), (toCopy.getTextureDetail0() == null ? null : toCopy.getTextureDetail0().getTextureName()), (toCopy.getTextureDetail1() == null ? null : toCopy.getTextureDetail1().getTextureName()));
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(toCopy.getEngine(), new E3DVector3F(toCopy.getVertexPosA()), new E3DVector4F(toCopy.getVertexColorA()), new E3DVector2F(toCopy.getTextureCoordA()));
        vertices[1] = new E3DTexturedVertex(toCopy.getEngine(), new E3DVector3F(toCopy.getVertexPosB()), new E3DVector4F(toCopy.getVertexColorB()), new E3DVector2F(toCopy.getTextureCoordB()));
        vertices[2] = new E3DTexturedVertex(toCopy.getEngine(), new E3DVector3F(toCopy.getVertexPosC()), new E3DVector4F(toCopy.getVertexColorC()), new E3DVector2F(toCopy.getTextureCoordC()));
        this.parentObject = newParentObject;
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * A triangle that doesn't care about texture coords/textures.  Be sure to never send this
	 * to the rendering pipeline!!  This is for vertexFunctions ONLY, NOT for rendering!
	 * @param engine
	 * @param vertexA
	 * @param vertexB
	 * @param vertexC
	 */
    public E3DTriangle(E3DEngine engine, E3DVector3F vertexPosA, E3DVector3F vertexPosB, E3DVector3F vertexPosC) {
        super(engine, null);
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(engine, vertexPosA, null, null);
        vertices[1] = new E3DTexturedVertex(engine, vertexPosB, null, null);
        vertices[2] = new E3DTexturedVertex(engine, vertexPosC, null, null);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public E3DTriangle(E3DEngine engine, E3DVector3F vertexPosA, E3DVector3F vertexPosB, E3DVector3F vertexPosC, E3DVector2F texCoordA, E3DVector2F texCoordB, E3DVector2F texCoordC, String textureName) {
        super(engine, textureName);
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(engine, vertexPosA, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordA);
        vertices[1] = new E3DTexturedVertex(engine, vertexPosB, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordB);
        vertices[2] = new E3DTexturedVertex(engine, vertexPosC, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordC);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public E3DTriangle(E3DEngine engine, E3DVector3F vertexPosA, E3DVector3F vertexPosB, E3DVector3F vertexPosC, E3DVector2F texCoordA, E3DVector2F texCoordB, E3DVector2F texCoordC, String textureName, E3DVector2F detail0TexCoordA, E3DVector2F detail0TexCoordB, E3DVector2F detail0TexCoordC, String detail0TextureName) {
        super(engine, new E3DRenderMode(engine, E3DRenderMode.RENDERMODE_TEXTURED), new E3DBlendMode(engine, E3DBlendMode.BLENDMODE_SOLID), textureName, detail0TextureName);
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(engine, vertexPosA, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordA, detail0TexCoordA);
        vertices[1] = new E3DTexturedVertex(engine, vertexPosB, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordB, detail0TexCoordB);
        vertices[2] = new E3DTexturedVertex(engine, vertexPosC, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordC, detail0TexCoordC);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public E3DTriangle(E3DEngine engine, E3DVector3F vertexPosA, E3DVector3F vertexPosB, E3DVector3F vertexPosC, E3DVector2F texCoordA, E3DVector2F texCoordB, E3DVector2F texCoordC, String textureName, E3DVector2F detail0TexCoordA, E3DVector2F detail0TexCoordB, E3DVector2F detail0TexCoordC, String detail0TextureName, E3DVector2F detail1TexCoordA, E3DVector2F detail1TexCoordB, E3DVector2F detail1TexCoordC, String detail1TextureName) {
        super(engine, new E3DRenderMode(engine, E3DRenderMode.RENDERMODE_TEXTURED), new E3DBlendMode(engine, E3DBlendMode.BLENDMODE_SOLID), textureName, detail0TextureName, detail1TextureName);
        vertices = new E3DTexturedVertex[3];
        vertices[0] = new E3DTexturedVertex(engine, vertexPosA, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordA, detail0TexCoordA, detail1TexCoordA);
        vertices[1] = new E3DTexturedVertex(engine, vertexPosB, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordB, detail0TexCoordB, detail1TexCoordB);
        vertices[2] = new E3DTexturedVertex(engine, vertexPosC, new E3DVector4F(1.0, 1.0, 1.0, 1.0), texCoordC, detail0TexCoordC, detail1TexCoordC);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Get the first vertex of the triangle
	 * @return
	 */
    private E3DTexturedVertex getVertexA() {
        return vertices[0];
    }

    /**
	 * Set the first vertex of the triangle
	 * @param vertex
	 */
    public void setVertexA(E3DTexturedVertex vertex) {
        this.vertices[0] = vertex;
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Set the second vertex of the triangle
	 * @param vertex
	 */
    public void setVertexB(E3DTexturedVertex vertex) {
        this.vertices[1] = vertex;
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Set the third vertex of the triangle
	 * @param vertex
	 */
    public void setVertexC(E3DTexturedVertex vertex) {
        this.vertices[2] = vertex;
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public E3DVector3F getVertexPos(int index) {
        if (index >= 0 && index < 3) return vertices[index].getVertexPos(); else return null;
    }

    /**
     * Be sure to NOT MODIFY VERTEX INFORMATION DIRECTLY! Use the triangle helpers!
     * @param index
     * @return
     */
    public E3DTexturedVertex getVertex(int index) {
        if (index >= 0 && index < 3) return vertices[index]; else return null;
    }

    /**
     * Get the position of the 1st vertex of the triangle
     * @return
     */
    public E3DVector3F getVertexPosA() {
        return vertices[0].getVertexPos();
    }

    /**
     * Set the position of the 1st vertex of the triangle
     * @param vertexPos
     */
    public void setVertexPosA(E3DVector3F vertexPos) {
        this.vertices[0].setVertexPos(vertexPos);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
     * Get the position of the 2nd vertex of the triangle
     * @return
     */
    public E3DVector3F getVertexPosB() {
        return vertices[1].getVertexPos();
    }

    /**
     * Set the position of the 2nd vertex of the triangle
     * @param vertexPos
     */
    public void setVertexPosB(E3DVector3F vertexPos) {
        this.vertices[1].setVertexPos(vertexPos);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
     * Get the position of the 3rd vertex of the triangle
     * @return
     */
    public E3DVector3F getVertexPosC() {
        return vertices[2].getVertexPos();
    }

    /**
     * Set the position of the 2nd vertex of the triangle
     * @param vertexPos
     */
    public void setVertexPosC(E3DVector3F vertexPos) {
        this.vertices[2].setVertexPos(vertexPos);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Get the color of the first vertex
	 * @return E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public E3DVector4F getVertexColorA() {
        return vertices[0].getVertexColor();
    }

    /**
	 * Set the color of the first vertex
	 * @param vertexColor E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public void setVertexColorA(E3DVector4F vertexColor) {
        this.vertices[0].setVertexColor(vertexColor);
    }

    /**
	 * Get the color of the second vertex
	 * @return E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public E3DVector4F getVertexColorB() {
        return vertices[1].getVertexColor();
    }

    /**
	 * Set the color of the second vertex
	 * @param vertexColor E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public void setVertexColorB(E3DVector4F vertexColor) {
        this.vertices[1].setVertexColor(vertexColor);
    }

    /**
	 * Get the color of the third vertex
	 * @return E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public E3DVector4F getVertexColorC() {
        return vertices[2].getVertexColor();
    }

    /**
	 * Set the color of the third vertex
	 * @param vertexColor E3DVector3F specifying RGBA components right  0.0 <= R.G.B. <= 1.0
	 */
    public void setVertexColorC(E3DVector4F vertexColor) {
        this.vertices[2].setVertexColor(vertexColor);
    }

    /**
	 * Get the texture coordinate of the first vertex
	 * @return
	 */
    public E3DVector2F getTextureCoordDetail1A() {
        return vertices[0].getTextureCoordDetail1();
    }

    /**
	 * Set the texture coordinate of the first vertex
	 * @param textureCoord
	 */
    public void setTextureCoordDetail1A(E3DVector2F textureCoord) {
        this.vertices[0].setTextureCoordDetail1(textureCoord);
    }

    /**
	 * Get the texture coordinate of the second vertex
	 * @return
	 */
    public E3DVector2F getTextureCoordDetail1B() {
        return vertices[1].getTextureCoordDetail1();
    }

    /**
	 * Set the texture coordinate of the second vertex
	 * @param textureCoord
	 */
    public void setTextureCoordDetail1B(E3DVector2F textureCoord) {
        this.vertices[1].setTextureCoordDetail1(textureCoord);
    }

    /**
	 * Get the texture coordinate of the third vertex
	 * @return
	 */
    public E3DVector2F getTextureCoordDetail1C() {
        return vertices[2].getTextureCoordDetail1();
    }

    /**
	 * Set the texture coordinate of the third vertex
	 * @param textureCoord
	 */
    public void setTextureCoordDetail1C(E3DVector2F textureCoord) {
        this.vertices[2].setTextureCoordDetail1(textureCoord);
    }

    /**
	 * @return Returns an array of textureCoords for each vertex of the triangle.
	 */
    public E3DVector2F[] getTextureCoordDetail1() {
        return new E3DVector2F[] { vertices[0].getTextureCoordDetail1(), vertices[1].getTextureCoordDetail1(), vertices[2].getTextureCoordDetail1() };
    }

    /**
	 * @param textureCoord The textureCoords to set for each vertex of the triangle.  This must be length 3
	 */
    public void setTextureCoordDetail1(E3DVector2F[] textureCoord) {
        vertices[0].setTextureCoordDetail1(textureCoord[0]);
        vertices[1].setTextureCoordDetail1(textureCoord[1]);
        vertices[2].setTextureCoordDetail1(textureCoord[2]);
    }

    /**
	 * Set the texture coords
	 * @param textureCoordA
	 * @param textureCoordB
	 * @param textureCoordC
	 */
    public void setTextureCoordDetail1(E3DVector2F textureCoordA, E3DVector2F textureCoordB, E3DVector2F textureCoordC) {
        vertices[0].setTextureCoordDetail1(textureCoordA);
        vertices[1].setTextureCoordDetail1(textureCoordB);
        vertices[2].setTextureCoordDetail1(textureCoordC);
    }

    /**
     * Get the texture coordinate of the first vertex
     * @return
     */
    public E3DVector2F getTextureCoordDetail0A() {
        return vertices[0].getTextureCoordDetail0();
    }

    /**
     * Set the texture coordinate of the first vertex
     * @param textureCoord
     */
    public void setTextureCoordDetail0A(E3DVector2F textureCoord) {
        this.vertices[0].setTextureCoordDetail0(textureCoord);
    }

    /**
     * Get the texture coordinate of the second vertex
     * @return
     */
    public E3DVector2F getTextureCoordDetail0B() {
        return vertices[1].getTextureCoordDetail0();
    }

    /**
     * Set the texture coordinate of the second vertex
     * @param textureCoord
     */
    public void setTextureCoordDetail0B(E3DVector2F textureCoord) {
        this.vertices[1].setTextureCoordDetail0(textureCoord);
    }

    /**
     * Get the texture coordinate of the third vertex
     * @return
     */
    public E3DVector2F getTextureCoordDetail0C() {
        return vertices[2].getTextureCoordDetail0();
    }

    /**
     * Set the texture coordinate of the third vertex
     * @param textureCoord
     */
    public void setTextureCoordDetail0C(E3DVector2F textureCoord) {
        this.vertices[2].setTextureCoordDetail0(textureCoord);
    }

    /**
     * @return Returns an array of textureCoords for each vertex of the triangle.
     */
    public E3DVector2F[] getTextureCoordDetail0() {
        return new E3DVector2F[] { vertices[0].getTextureCoordDetail0(), vertices[1].getTextureCoordDetail0(), vertices[2].getTextureCoordDetail0() };
    }

    /**
     * @param textureCoord The textureCoords to set for each vertex of the triangle.  This must be length 3
     */
    public void setTextureCoordDetail0(E3DVector2F[] textureCoord) {
        vertices[0].setTextureCoordDetail0(textureCoord[0]);
        vertices[1].setTextureCoordDetail0(textureCoord[1]);
        vertices[2].setTextureCoordDetail0(textureCoord[2]);
    }

    /**
     * Set the texture coords
     * @param textureCoordA
     * @param textureCoordB
     * @param textureCoordC
     */
    public void setTextureCoordDetail0(E3DVector2F textureCoordA, E3DVector2F textureCoordB, E3DVector2F textureCoordC) {
        vertices[0].setTextureCoordDetail0(textureCoordA);
        vertices[1].setTextureCoordDetail0(textureCoordB);
        vertices[2].setTextureCoordDetail0(textureCoordC);
    }

    /**
     * Get the texture coordinate of the first vertex
     * @return
     */
    public E3DVector2F getTextureCoordA() {
        return vertices[0].getTextureCoord();
    }

    /**
     * Set the texture coordinate of the first vertex
     * @param textureCoord
     */
    public void setTextureCoordA(E3DVector2F textureCoord) {
        this.vertices[0].setTextureCoord(textureCoord);
    }

    /**
     * Get the texture coordinate of the second vertex
     * @return
     */
    public E3DVector2F getTextureCoordB() {
        return vertices[1].getTextureCoord();
    }

    /**
     * Set the texture coordinate of the second vertex
     * @param textureCoord
     */
    public void setTextureCoordB(E3DVector2F textureCoord) {
        this.vertices[1].setTextureCoord(textureCoord);
    }

    /**
     * Get the texture coordinate of the third vertex
     * @return
     */
    public E3DVector2F getTextureCoordC() {
        return vertices[2].getTextureCoord();
    }

    /**
     * Set the texture coordinate of the third vertex
     * @param textureCoord
     */
    public void setTextureCoordC(E3DVector2F textureCoord) {
        this.vertices[2].setTextureCoord(textureCoord);
    }

    /**
     * @return Returns an array of textureCoords for each vertex of the triangle.
     */
    public E3DVector2F[] getTextureCoord() {
        return new E3DVector2F[] { vertices[0].getTextureCoord(), vertices[1].getTextureCoord(), vertices[2].getTextureCoord() };
    }

    /**
     * @param textureCoord The textureCoords to set for each vertex of the triangle.  This must be length 3
     */
    public void setTextureCoord(E3DVector2F[] textureCoord) {
        vertices[0].setTextureCoord(textureCoord[0]);
        vertices[1].setTextureCoord(textureCoord[1]);
        vertices[2].setTextureCoord(textureCoord[2]);
    }

    /**
     * Set the texture coords
     * @param textureCoordA
     * @param textureCoordB
     * @param textureCoordC
     */
    public void setTextureCoord(E3DVector2F textureCoordA, E3DVector2F textureCoordB, E3DVector2F textureCoordC) {
        vertices[0].setTextureCoord(textureCoordA);
        vertices[1].setTextureCoord(textureCoordB);
        vertices[2].setTextureCoord(textureCoordC);
    }

    /**
	 * Set the triangle's vertices
	 * @param vertex Array of the 3 vertices (E3DVector3F's) of the triangle. Must be length 3.
	 */
    public void setVertices(E3DTexturedVertex[] vertices) {
        this.vertices = vertices;
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Set the vertices
	 * @param vertexA
	 * @param vertexB
	 * @param vertexC
	 */
    public void setVertexPos(E3DVector3F vertexPosA, E3DVector3F vertexPosB, E3DVector3F vertexPosC) {
        vertices[0].setVertexPos(vertexPosA);
        vertices[1].setVertexPos(vertexPosB);
        vertices[2].setVertexPos(vertexPosC);
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    /**
	 * Get an array of colors for each of the triangles vertices
	 * @return Array of E3DVector3F's. R = A, G = B, B = C, A = D
	 */
    public E3DVector4F[] getVertexColor() {
        return new E3DVector4F[] { vertices[0].getVertexColor(), vertices[1].getVertexColor(), vertices[2].getVertexColor() };
    }

    /**
	 * Set the colors of each vertex of a triangle
	 * @param vertexColor Array of E3DVector4F's.  Must be length 3.
	 */
    public void setVertexColor(E3DVector4F[] vertexColor) {
        vertices[0].setVertexColor(vertexColor[0]);
        vertices[1].setVertexColor(vertexColor[1]);
        vertices[2].setVertexColor(vertexColor[2]);
    }

    /**
	 * Set the colors of each vertex of the triangle
	 * @param vertexColorA
	 * @param vertexColorB
	 * @param vertexColorC
	 */
    public void setVertexColor(E3DVector4F vertexColorA, E3DVector4F vertexColorB, E3DVector4F vertexColorC) {
        vertices[0].setVertexColor(vertexColorA);
        vertices[1].setVertexColor(vertexColorB);
        vertices[2].setVertexColor(vertexColorC);
    }

    /**
	 * Resets all the vertex colors to all be color.  Value between 0 and 1 should be used
	 *
	 */
    public void resetVertexColor(double color) {
        for (int i = 0; i < 3; i++) {
            vertices[i].getVertexColor().setA(color);
            vertices[i].getVertexColor().setB(color);
            vertices[i].getVertexColor().setC(color);
            vertices[i].getVertexColor().setD(color);
        }
    }

    /**
	 * Ensures all vertex colors are <= 1.0
	 *
	 */
    public void normaliseVertexColors() {
        for (int i = 0; i < 3; i++) vertices[i].normaliseVertexColor();
    }

    public void appendVertexBuffer(FloatBuffer buffer) {
        E3DVector3F vertexPosA = vertices[0].getVertexPos(), vertexPosB = vertices[1].getVertexPos(), vertexPosC = vertices[2].getVertexPos();
        buffer.put((float) vertexPosA.getX());
        buffer.put((float) vertexPosA.getY());
        buffer.put((float) vertexPosA.getZ());
        buffer.put((float) vertexPosB.getX());
        buffer.put((float) vertexPosB.getY());
        buffer.put((float) vertexPosB.getZ());
        buffer.put((float) vertexPosC.getX());
        buffer.put((float) vertexPosC.getY());
        buffer.put((float) vertexPosC.getZ());
    }

    public void appendVertexColorBuffer(FloatBuffer buffer) {
        E3DVector4F vertexColorA = vertices[0].getVertexColor(), vertexColorB = vertices[1].getVertexColor(), vertexColorC = vertices[2].getVertexColor();
        buffer.put((float) vertexColorA.getA());
        buffer.put((float) vertexColorA.getB());
        buffer.put((float) vertexColorA.getC());
        buffer.put((float) vertexColorA.getD());
        buffer.put((float) vertexColorB.getA());
        buffer.put((float) vertexColorB.getB());
        buffer.put((float) vertexColorB.getC());
        buffer.put((float) vertexColorB.getD());
        buffer.put((float) vertexColorC.getA());
        buffer.put((float) vertexColorC.getB());
        buffer.put((float) vertexColorC.getC());
        buffer.put((float) vertexColorC.getD());
    }

    public void appendTexCoordBuffer(FloatBuffer buffer) {
        E3DVector2F textureCoordA = vertices[0].getTextureCoord(), textureCoordB = vertices[1].getTextureCoord(), textureCoordC = vertices[2].getTextureCoord();
        buffer.put((float) textureCoordA.getX());
        buffer.put((float) textureCoordA.getY());
        buffer.put((float) textureCoordB.getX());
        buffer.put((float) textureCoordB.getY());
        buffer.put((float) textureCoordC.getX());
        buffer.put((float) textureCoordC.getY());
    }

    public void appendTexCoordDetail0Buffer(FloatBuffer buffer) {
        if (!isTextureDetail0Available()) return;
        E3DVector2F textureCoordA = vertices[0].getTextureCoordDetail0(), textureCoordB = vertices[1].getTextureCoordDetail0(), textureCoordC = vertices[2].getTextureCoordDetail0();
        buffer.put((float) textureCoordA.getX());
        buffer.put((float) textureCoordA.getY());
        buffer.put((float) textureCoordB.getX());
        buffer.put((float) textureCoordB.getY());
        buffer.put((float) textureCoordC.getX());
        buffer.put((float) textureCoordC.getY());
    }

    public void appendTexCoordDetail1Buffer(FloatBuffer buffer) {
        if (!isTextureDetail1Available()) return;
        E3DVector2F textureCoordA = vertices[0].getTextureCoordDetail1(), textureCoordB = vertices[1].getTextureCoordDetail1(), textureCoordC = vertices[2].getTextureCoordDetail1();
        buffer.put((float) textureCoordA.getX());
        buffer.put((float) textureCoordA.getY());
        buffer.put((float) textureCoordB.getX());
        buffer.put((float) textureCoordB.getY());
        buffer.put((float) textureCoordC.getX());
        buffer.put((float) textureCoordC.getY());
    }

    private void recalculateNormal() {
        E3DVector3F vertexPosA = vertices[0].getVertexPos(), vertexPosB = vertices[1].getVertexPos(), vertexPosC = vertices[2].getVertexPos();
        if (vertexPosA == null || vertexPosB == null || vertexPosC == null) return;
        E3DVector3F vec1 = vertexPosB.subtract(vertexPosA);
        E3DVector3F vec2 = vertexPosC.subtract(vertexPosA);
        normal = vec1.crossProduct(vec2);
        normal.normaliseEqual();
    }

    public E3DVector3F getCentroid() {
        E3DVector3F vertexPosA = vertices[0].getVertexPos(), vertexPosB = vertices[1].getVertexPos(), vertexPosC = vertices[2].getVertexPos();
        E3DVector3F retVec = new E3DVector3F(vertexPosA.getX() + vertexPosB.getX() + vertexPosC.getX(), vertexPosA.getY() + vertexPosB.getY() + vertexPosC.getY(), vertexPosA.getZ() + vertexPosB.getZ() + vertexPosC.getZ());
        retVec.scaleEqual(0.33333333);
        return retVec;
    }

    /**
	 * This checks if a point is in a triangle by seeing if the interior angles add up to 360Deg.
	 * This is slightly slower than isPointInTriangle, but left for testing sake.
	 * @param point
	 * @return
	 */
    public boolean isPointInTriangle(E3DVector3F point) {
        if (point == null) return false;
        E3DVector3F vertexPosA = vertices[0].getVertexPos(), vertexPosB = vertices[1].getVertexPos(), vertexPosC = vertices[2].getVertexPos();
        E3DVector3F a, b, c;
        a = vertexPosA.subtract(point);
        b = vertexPosB.subtract(point);
        c = vertexPosC.subtract(point);
        if (a.equals(0.0, 0.0, 0.0) || b.equals(0.0, 0.0, 0.0) || c.equals(0.0, 0.0, 0.0)) return true;
        a.normaliseEqual();
        b.normaliseEqual();
        c.normaliseEqual();
        double totalAngle = a.angleBetweenRads(b) + b.angleBetweenRads(c) + c.angleBetweenRads(a);
        if (totalAngle < E3DConstants.TWOPI + E3DConstants.DBL_PRECISION_ERROR && totalAngle > E3DConstants.TWOPI - E3DConstants.DBL_PRECISION_ERROR) return true; else return false;
    }

    /**
	 * Assumes a Z coordinate of 0.0  of point
	 * @param point 2D point is converted to 3D (Assuming Z coord of 0)
	 * @return
	 */
    public boolean isPointInTriangle(E3DVector2F point) {
        if (point == null) return false;
        E3DVector3F vertexPosA = vertices[0].getVertexPos(), vertexPosB = vertices[1].getVertexPos(), vertexPosC = vertices[2].getVertexPos();
        E3DVector3F a, b, c;
        a = vertexPosA.subtract(point);
        b = vertexPosB.subtract(point);
        c = vertexPosC.subtract(point);
        if (a.equals(0.0, 0.0, 0.0) || b.equals(0.0, 0.0, 0.0) || c.equals(0.0, 0.0, 0.0)) return true;
        a.normaliseEqual();
        b.normaliseEqual();
        c.normaliseEqual();
        double totalAngle = a.angleBetweenRads(b) + b.angleBetweenRads(c) + c.angleBetweenRads(a);
        if (totalAngle < E3DConstants.TWOPI + E3DConstants.DBL_PRECISION_ERROR && totalAngle > E3DConstants.TWOPI - E3DConstants.DBL_PRECISION_ERROR) return true; else return false;
    }

    /**
	 * To render outside of the normal rendering loop, this can be added
	 * to the external renderable list of the engine's because
	 * it implements render.
	 */
    public void render() {
        E3DRenderTree renderTree = new E3DRenderTree(getEngine());
        renderTree.getTriangleHandler().add(this);
        renderTree.render();
    }

    /**
	 * @param normal The normal to set.
	 */
    public void setNormal(E3DVector3F normal) {
        this.normal = normal;
    }

    /**
	 * @return Returns the normal.
	 */
    public E3DVector3F getNormal() {
        if (needNormalRecalc) {
            recalculateNormal();
            needNormalRecalc = false;
        }
        return normal;
    }

    private void recalculatePlaneEquationCoords() {
        E3DVector3F normal = getNormal();
        setPlaneEquationCoords(new E3DVector4F(normal.getX(), normal.getY(), normal.getZ(), -normal.dotProduct(getVertexA().getVertexPos())));
    }

    public E3DVector4F getPlaneEquationCoords() {
        if (needPlaneEquationRecalc) {
            recalculatePlaneEquationCoords();
            needPlaneEquationRecalc = false;
        }
        return planeEquationCoords;
    }

    public void setPlaneEquationCoords(E3DVector4F planeEq) {
        this.planeEquationCoords = planeEq;
    }

    /**
	 * Translate the triangle vertices
	 *
	 */
    public void translate(E3DVector3F translationAmt) {
        getVertexPosA().addEqual(translationAmt);
        getVertexPosB().addEqual(translationAmt);
        getVertexPosC().addEqual(translationAmt);
        needPlaneEquationRecalc = true;
    }

    public void scale(double scaleAmt) {
        getVertexPosA().scaleEqual(scaleAmt);
        getVertexPosB().scaleEqual(scaleAmt);
        getVertexPosC().scaleEqual(scaleAmt);
        needPlaneEquationRecalc = true;
    }

    public String toString() {
        return "Triangle - A: " + getVertexPosA() + ", B: " + getVertexPosB() + ", C: " + getVertexPosC();
    }

    /**
	 * Rotate the triangle around aroundVec.  Translated amt is how far the triangle has been translated from the origin (it needsd this so it can be centered around the up vec correctly and then rotate and moved back)
	 * @param angle
	 * @param upVec
	 * @param translatedAmt
	 */
    public void rotate(double angle, E3DVector3F upVec, E3DVector3F translatedAmt) {
        E3DVector3F vec = null;
        vec = getNormal();
        vec.rotateEqual(angle, upVec);
        vec.normaliseEqual();
        for (int a = 0; a < 3; a++) {
            vec = vertices[a].getVertexPos();
            vec.subtractEqual(translatedAmt);
            vec.rotateEqual(angle, upVec);
            vec.addEqual(translatedAmt);
        }
        needNormalRecalc = true;
        needPlaneEquationRecalc = true;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
        this.vertices[0].getVertexColor().setD(alpha);
        this.vertices[1].getVertexColor().setD(alpha);
        this.vertices[2].getVertexColor().setD(alpha);
    }

    public double getDistanceToPoint(E3DVector3F point) {
        double planeD = getPlaneEquationCoords().getD();
        return getNormal().dotProduct(point) + planeD;
    }

    public boolean doesSegmentCrossPlane(E3DVector3F startPos, E3DVector3F endPos) {
        double distanceBefore = getDistanceToPoint(startPos);
        double distanceAfter = getDistanceToPoint(endPos);
        if ((distanceBefore < 0 && distanceAfter > 0) || (distanceBefore > 0 && distanceAfter < 0)) return true; else return false;
    }

    /**
     * Gets the intersection point of the line segment formed between startPos and endPos and the triangle if it intersects
     * @param startPos
     * @param endPos
     * @param triangle
     * @return
     *  returns intersection point
     */
    public E3DVector3F getPlaneIntersectionPoint(E3DVector3F startPos, E3DVector3F endPos) {
        double a, b;
        E3DVector3F normal = getNormal();
        E3DVector3F direction = endPos.subtract(startPos);
        a = -normal.dotProduct(startPos.subtract(vertices[0].getVertexPos()));
        b = normal.dotProduct(direction);
        double r = a / b;
        if (r < 0.0) return null;
        direction.scaleEqual(r);
        direction = startPos.add(direction);
        return direction;
    }

    /**
     * Checks for full fledged collision between the line segment formed between startPos and endPos and the
     *  triangle and returns the intersection point (or null if no collision occurs)
     * @return
     *  Returns the intersection point vector if there is 
     *  a collision between the line segment (startPos, endPos) and triangle.
     *  Otherwise, returns null;
     */
    public E3DVector3F checkSegmentCollision(E3DVector3F startPos, E3DVector3F endPos) {
        if (doesSegmentCrossPlane(startPos, endPos)) {
            E3DVector3F intersectionPoint = getPlaneIntersectionPoint(startPos, endPos);
            if (isPointInTriangle(intersectionPoint)) return intersectionPoint;
        }
        return null;
    }

    /**
     * Checks for a collision between this and another triangle by checking to see if any of vertices when moved along a line segment
     *  of length endPos - startPos collide with the collideeTriangle. 
     * TODO:  This should also check the edges of the colliderTriangle...
     * @param colliderTriangle
     * @param startPos
     * @param endPos
     * @param collideeTriangle
     * @return
     */
    public static E3DVector3F checkTriangleCollision(E3DTriangle colliderTriangle, E3DVector3F startPos, E3DVector3F endPos, E3DTriangle collideeTriangle) {
        E3DVector3F translateAmt = endPos.subtract(startPos);
        E3DVector3F intersectionPt[] = new E3DVector3F[3];
        E3DVector3F colliderPts[] = new E3DVector3F[] { colliderTriangle.getVertexPosA(), colliderTriangle.getVertexPosB(), colliderTriangle.getVertexPosC() };
        int i = 0;
        for (i = 0; i < 3; i++) intersectionPt[i] = collideeTriangle.checkSegmentCollision(colliderPts[i], colliderPts[i].add(translateAmt));
        E3DVector3F closestIntersection = null;
        double closestDistance = 0.0, checkDistance = 0.0;
        for (i = 0; i < 3; i++) {
            if (intersectionPt[i] != null) {
                if (closestIntersection == null) {
                    closestIntersection = intersectionPt[i];
                    closestDistance = colliderPts[i].subtract(intersectionPt[i]).getLengthSquared();
                } else {
                    checkDistance = colliderPts[i].subtract(intersectionPt[i]).getLengthSquared();
                    if (checkDistance < closestDistance) {
                        closestDistance = checkDistance;
                        closestIntersection = intersectionPt[i];
                    }
                }
            }
        }
        return closestIntersection;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null || !(arg0 instanceof E3DTriangle)) return false;
        return arg0 == this;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void setRenderMode(E3DRenderMode renderMode) {
        super.setRenderMode(renderMode);
        if (parentObject != null && parentObject instanceof E3DActor) {
            E3DActor actor = (E3DActor) parentObject;
            actor.getRenderTree().getActorHandler().rehashTriangle(actor, this);
        }
    }

    public void setBlendMode(E3DBlendMode blendMode) {
        super.setBlendMode(blendMode);
        if (parentObject != null && parentObject instanceof E3DActor) {
            E3DActor actor = (E3DActor) parentObject;
            actor.getRenderTree().getActorHandler().rehashTriangle(actor, this);
        }
    }

    public void setTexture(E3DTexture texture) {
        super.setTexture(texture);
        if (parentObject != null && parentObject instanceof E3DActor) {
            E3DActor actor = (E3DActor) parentObject;
            actor.getRenderTree().getActorHandler().rehashTriangle(actor, this);
        }
    }

    public void setTextureDetail0(E3DTexture textureDetail0) {
        super.setTextureDetail0(textureDetail0);
        if (parentObject != null && parentObject instanceof E3DActor) {
            E3DActor actor = (E3DActor) parentObject;
            actor.getRenderTree().getActorHandler().rehashTriangle(actor, this);
        }
    }

    public void setTextureDetail1(E3DTexture textureDetail1) {
        super.setTextureDetail1(textureDetail1);
        if (parentObject != null && parentObject instanceof E3DActor) {
            E3DActor actor = (E3DActor) parentObject;
            actor.getRenderTree().getActorHandler().rehashTriangle(actor, this);
        }
    }

    public E3DRenderable getParentObject() {
        return parentObject;
    }

    public void setParentObject(E3DRenderable parentObject) {
        this.parentObject = parentObject;
    }
}
