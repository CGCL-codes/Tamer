package espresso3d.engine.window.viewport.image;

import espresso3d.engine.E3DEngine;
import espresso3d.engine.lowlevel.geometry.E3DQuad;
import espresso3d.engine.lowlevel.geometry.E3DTriangle;
import espresso3d.engine.lowlevel.tree.base.IE3DHashableNode;
import espresso3d.engine.lowlevel.vector.E3DVector2F;
import espresso3d.engine.lowlevel.vector.E3DVector2I;
import espresso3d.engine.lowlevel.vector.E3DVector3F;
import espresso3d.engine.lowlevel.vector.E3DVector4F;
import espresso3d.engine.renderer.base.E3DAnimatedTextureRenderable;
import espresso3d.engine.renderer.base.E3DBlendMode;
import espresso3d.engine.renderer.base.E3DRenderMode;
import espresso3d.engine.renderer.rendertree.E3DRenderTree;
import espresso3d.engine.renderer.texture.E3DAnimatedTextureFrame;
import espresso3d.engine.renderer.texture.E3DTexture;
import espresso3d.engine.window.viewport.E3DViewport;

/**
 * @author cmoxley
 *
 * Abstract class that allows you to create a 2D image that is displayed in a viewport - essentially blit an image to the screen.
 *
 * This class cannot be directly used - it must be extended.  The engine provides you with E3DImageFixedSize and 
 * E3DImageVariableSize implementations of this class which should be able to handle most image types you would want.
 * 
 * The image will always face the user and always be in the same position in the viewport.
 *
 * The image will also scale with the viewport as its size changes
 * 
 * After creating an E3DImage, add it to a viewport in the engine
 * to have it rendered.
 * 
 * E3DImages can be either solid (can't be seen through at all) or blended
 * which will allow transparent sections of an image to be seen through if the 
 * image used has alpha information (PNG image format supports this as well as others).
 * E3DImages must be loaded ahead of time via a definition in a textureset file and loaded
 * either with a map, actor, or explicitly calling world.loadTextureSet
 */
public abstract class E3DImage extends E3DAnimatedTextureRenderable implements IE3DHashableNode {

    private E3DViewport viewport;

    private E3DQuad quad;

    private String imageID;

    private E3DVector2I position;

    private double life;

    private double age;

    /**
     * Create a new viewport image.  This will be a 2D image that
     * is always at the same (x,y) coords in a viewport.  As a  viewport scales,
     * it will scale with it as well once added to the viewport.
     * 
     * Essentially, this is like a 2D image that
     * will overlay the window used for things like HUDS and menus.
     * 
     * @param engine
     * @param imageID  The ID this image will be.  This can be any string that uniquely identifies this image once added to the viewport.
     * @param textureName The name of the texture that will make up the graphic
     * @param position The position (X,Y) on the screen that the bottom left corner of the image is at.  (0,0) is at the bottom left corner of the viewport.
     */
    public E3DImage(E3DEngine engine, String imageID, String textureName, E3DVector2I position, int life) {
        this(engine, imageID, textureName, position, life, new E3DBlendMode(engine, E3DBlendMode.BLENDMODE_SOLID));
    }

    /**
     * Create a new viewport image.  This will be a 2D image that
     * is always at the same (x,y) coords in a viewport.  As a  viewport scales,
     * it will scale with it as well once added to the viewport.
     * 
     * Essentially, this is like a 2D image that
     * will overlay the window used for things like HUDS and menus.
     * 
     * @param engine
     * @param imageID  The ID this image will be.  This can be any string that uniquely identifies this image once added to the viewport.
     * @param textureName The name of the texture that will make up the graphic
     * @param position The position (X,Y) on the screen that the bottom left corner of the image is at.  (0,0) is at the bottom left corner of the viewport.
     * @param life How long the image should be rendered (in seconds).  -1 will render forever (or until removed programmatically from the viewport)
     * @param blendMode The blending mode used for the image during render time. Default is BLENDMODE_SOLID
     */
    public E3DImage(E3DEngine engine, String imageID, String textureName, E3DVector2I position, int life, E3DBlendMode blendMode) {
        super(engine, new E3DRenderMode(engine, E3DRenderMode.RENDERMODE_TEXTURED), blendMode, textureName);
        this.imageID = imageID;
        this.position = position;
        this.life = life;
        this.age = 0;
        quad = new E3DQuad(getEngine(), new E3DVector3F(-0.5, -0.5, 0), new E3DVector3F(-0.5, 0.5, 0), new E3DVector3F(0.5, 0.5, 0), new E3DVector3F(0.5, -0.5, 0), new E3DVector2F(0, 0), new E3DVector2F(0, 1), new E3DVector2F(1, 1), new E3DVector2F(1, 0), getAnimatedTexture().getTextureName());
    }

    /**
	 * Construct an image with an animated texture
	 * @param engine
	 * @param imageID
	 * @param animatedTextureFrames
	 * @param animationLoops
	 * @param position
	 * @param life
	 * @param blendMode
	 */
    public E3DImage(E3DEngine engine, String imageID, E3DAnimatedTextureFrame[] animatedTextureFrames, int animationLoops, E3DVector2I position, int life, E3DBlendMode blendMode) {
        super(engine, new E3DRenderMode(engine, E3DRenderMode.RENDERMODE_TEXTURED), blendMode, animatedTextureFrames, animationLoops);
        this.imageID = imageID;
        this.position = position;
        this.life = life;
        this.age = 0;
        quad = new E3DQuad(getEngine(), new E3DVector3F(-0.5, -0.5, 0), new E3DVector3F(-0.5, 0.5, 0), new E3DVector3F(0.5, 0.5, 0), new E3DVector3F(0.5, -0.5, 0), new E3DVector2F(0, 0), new E3DVector2F(0, 1), new E3DVector2F(1, 1), new E3DVector2F(1, 0), getAnimatedTexture().getTextureName());
    }

    /**
     * Construct an image with an animated texture
     * @param engine
     * @param imageID
     * @param animatedTextureFrames
     * @param animationLoops
     * @param position
     * @param life
     */
    public E3DImage(E3DEngine engine, String imageID, E3DAnimatedTextureFrame[] animatedTextureFrames, int animationLoops, E3DVector2I position, int life) {
        this(engine, imageID, animatedTextureFrames, animationLoops, position, life, new E3DBlendMode(engine, E3DBlendMode.BLENDMODE_SOLID));
    }

    /**
	 * Make a copy of an image
	 * @param toCopyImage
	 */
    public E3DImage(E3DImage toCopyImage, String imageID) {
        super(toCopyImage.getEngine(), new E3DRenderMode(toCopyImage.getRenderMode()), new E3DBlendMode(toCopyImage.getBlendMode()), toCopyImage.getAnimatedTexture().getTextureName());
        this.viewport = toCopyImage.getViewport();
        this.imageID = imageID;
        this.position = new E3DVector2I(toCopyImage.getPosition());
        this.life = toCopyImage.getLife();
        this.age = 0;
        quad = new E3DQuad(getEngine(), new E3DVector3F(0.5, -0.5, 0), new E3DVector3F(0.5, 0.5, 0), new E3DVector3F(-0.5, 0.5, 0), new E3DVector3F(-0.5, -0.5, 0), new E3DVector2F(1, 0), new E3DVector2F(1, 1), new E3DVector2F(0, 1), new E3DVector2F(0, 0), getAnimatedTexture().getTextureName());
    }

    public void render() {
        E3DRenderTree renderTree = new E3DRenderTree(getEngine());
        renderTree.getQuadHandler().add(getPositionedQuad());
        renderTree.render();
    }

    /**
	 * Implementing class must specify a way to scale itself
	 * @param scaleAmt
	 */
    public abstract void scale(double scaleAmt);

    /**
     * Return a copy of this quad and position and size the copy as necessary
     * @return
     */
    public abstract E3DQuad getPositionedQuad();

    /**
	 * The viewport this image is in.  This will be modified automatically if it is added to another viewport.a
	 * @return
	 */
    public E3DViewport getViewport() {
        return viewport;
    }

    /**
     * Return the ID of the image
     * @return
     */
    public String getImageID() {
        return imageID;
    }

    /**
     * Set the ID of the image
     * @param imageID
     */
    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    /**
     * Return the quad that makes up the image.  This is NOT positioned as it should be.  getPositionedQuad should be used for that.
     * @return
     */
    public E3DQuad getQuad() {
        return quad;
    }

    /**
     * Set a quad for hte image to use.
     * @param quad
     */
    public void setQuad(E3DQuad quad) {
        this.quad = quad;
    }

    /**
     * Translate the image
     * @param translationAmt
     */
    public void translate(E3DVector2I translationAmt) {
        position.addEqual(translationAmt);
    }

    /**
     * Set the color of the 4 vertices that make up the corners of the image
     * @param vertexColorA
     * @param vertexColorB
     * @param vertexColorC
     * @param vertexColorD
     */
    public void setVertexColor(E3DVector4F vertexColorA, E3DVector4F vertexColorB, E3DVector4F vertexColorC, E3DVector4F vertexColorD) {
        quad.setVertexColor(vertexColorA, vertexColorB, vertexColorC, vertexColorD);
    }

    /**
     * Get the 2D position of the image (Viewport coords with (0,0) in the bottom left)
     * @return
     */
    public E3DVector2I getPosition() {
        return position;
    }

    /**
     * Set the 2D position of the image (Viewport coords with (0,0) in the bottom left)
     * @param position
     */
    public void setPosition(E3DVector2I position) {
        this.position = position;
    }

    /**
     * This is handled automatically by the viewport when added
     * @param viewport
     */
    public void setViewport(E3DViewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Called by the printer to update its life
     *@return
     */
    public boolean update(double lastFrameTimeSeconds) {
        super.update(lastFrameTimeSeconds);
        if (life > 0) {
            this.age += lastFrameTimeSeconds;
            if (age > life) return false; else return true;
        }
        return true;
    }

    public void setTexture(E3DTexture texture) {
        super.setTexture(texture);
        quad.setTexture(getAnimatedTexture());
        if (viewport != null) viewport.getRenderTree().getImageHandler().rehash(this);
    }

    public void setRenderMode(E3DRenderMode renderMode) {
        super.setRenderMode(renderMode);
        quad.setRenderMode(renderMode);
        if (viewport != null) viewport.getRenderTree().getImageHandler().rehash(this);
    }

    public void setBlendMode(E3DBlendMode blendMode) {
        super.setBlendMode(blendMode);
        quad.setBlendMode(blendMode);
        if (viewport != null) viewport.getRenderTree().getImageHandler().rehash(this);
    }

    /**
     * Get the life of the image or length of time in secodns it will be renderered in the engine.
     * -1 is forever.
     * @return
     */
    public double getLife() {
        return life;
    }

    /**
     * Set the life of the image or length of time in secodns it will be renderered in the engine.
     * -1 is forever.
     * @param life
     */
    public void setLife(double life) {
        this.life = life;
    }

    /**
     * Returns the age of the image, or, how many seconds it has already been rendered.
     * @return
     */
    public double getAge() {
        return age;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null || !(arg0 instanceof E3DImage)) return false;
        return arg0 == this;
    }

    public int hashCode() {
        return super.hashCode();
    }
}
