package org.xith3d.geometry;

import java.awt.Font;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import org.xith3d.loaders.texture.TextureCreator;
import org.xith3d.scenegraph.Billboard;
import org.xith3d.scenegraph.Texture;

/**
 * The TextBillboard is a Rectangle with a text-Texture on it, that's always
 * facing the camera.<br>
 * The Texture is created by TextureCreator.createTexture(String, Color3f, java.awt.Font, int).
 * 
 * @see TextureCreator#createTexture(String, Color3f, java.awt.Font, int)
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class TextBillboard extends TextRectangle implements Billboard {

    private long lastFrameId = -Long.MAX_VALUE;

    protected Tuple3f[] zeroVertices;

    protected Point3f[] transformedVertices = new Point3f[] { new Point3f(), new Point3f(), new Point3f(), new Point3f() };

    /**
     * {@inheritDoc}
     */
    public void updateFaceToCamera(Matrix3f viewRotation, long frameId) {
        if (frameId <= lastFrameId) return;
        for (int i = 0; i < 4; i++) {
            transformedVertices[i].set(zeroVertices[i]);
            viewRotation.transform(transformedVertices[i]);
        }
        this.getGeometry().setCoordinates(0, transformedVertices);
        lastFrameId = frameId;
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param texture the text-texture to apply
     * @param width width of the Rectangle
     * @param height height of the Rectangle
     * @param position relative Location of the Rectangle
     */
    protected TextBillboard(Texture texture, float width, float height, Tuple3f position) {
        super(texture, width, height, position);
        this.zeroVertices = createVertices(width, height, position);
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param texture the text-texture to apply
     * @param width width of the Rectangle
     * @param height height of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     * @param zOffset relative z-Location of the Rectangle
     */
    protected TextBillboard(Texture texture, float width, float height, ZeroPointLocation zpl, float zOffset) {
        super(texture, width, height, zpl, zOffset);
        this.zeroVertices = createVertices(width, height, createPosition(width, height, zpl, zOffset));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param width (fixed) width of the Rectangle
     * @param position relative Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, int alignment, float width, Tuple3f position) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, position);
        return (rect);
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param width (fixed) width of the Rectangle
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, int alignment, float width, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, new Vector3f(0f, 0f, zOffset));
        return (rect);
    }

    /**
     * Creates a TextRectangle.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param width (fixed) width of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, int alignment, float width) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, (Tuple3f) null);
        return (rect);
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param width (fixed) width of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, int alignment, float width, ZeroPointLocation zpl, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float height = getHeightFromFixedWidth(texture, width);
        return (new TextBillboard(texture, width, height, zpl, zOffset));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param width (fixed) width of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, int alignment, float width, ZeroPointLocation zpl) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float height = getHeightFromFixedWidth(texture, width);
        return (new TextBillboard(texture, width, height, zpl, 0.0f));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param width (fixed) width of the Rectangle
     * @param position relative Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, float width, Tuple3f position) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, position);
        return (rect);
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param width (fixed) width of the Rectangle
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, float width, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, new Vector3f(0f, 0f, zOffset));
        return (rect);
    }

    /**
     * Creates a TextRectangle.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param width (fixed) width of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, float width) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float height = getHeightFromFixedWidth(texture, width);
        final TextBillboard rect = new TextBillboard(texture, width, height, (Tuple3f) null);
        return (rect);
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param width (fixed) width of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, float width, ZeroPointLocation zpl, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float height = getHeightFromFixedWidth(texture, width);
        return (new TextBillboard(texture, width, height, zpl, zOffset));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param width (fixed) width of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     */
    public static TextBillboard createFixedWidth(String text, Color3f color, Font font, float width, ZeroPointLocation zpl) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float height = getHeightFromFixedWidth(texture, width);
        return (new TextBillboard(texture, width, height, zpl, 0.0f));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param height (fixed) height of the Rectangle
     * @param position relative Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, int alignment, float height, Tuple3f position) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, position));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param height (fixed) height of the Rectangle
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, int alignment, float height, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, new Vector3f(0f, 0f, zOffset)));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param height (fixed) height of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, int alignment, float height) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, (Tuple3f) null));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param height (fixed) height of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, int alignment, float height, ZeroPointLocation zpl, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, zpl, zOffset));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param alignment the text horizontal alignment
     * @param height (fixed) height of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, int alignment, float height, ZeroPointLocation zpl) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, alignment);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, zpl, 0.0f));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param height (fixed) height of the Rectangle
     * @param position relative Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, float height, Tuple3f position) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, position));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param height (fixed) height of the Rectangle
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, float height, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, new Vector3f(0f, 0f, zOffset)));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param height (fixed) height of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, float height) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, (Tuple3f) null));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param height (fixed) height of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     * @param zOffset relative z-Location of the Rectangle
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, float height, ZeroPointLocation zpl, float zOffset) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, zpl, zOffset));
    }

    /**
     * Creates a TextBillboard.
     * 
     * @param text the text to render on the Rectangle
     * @param color the color to use for the text
     * @param font the Font to use for the text
     * @param height (fixed) height of the Rectangle
     * @param zpl the location of the point (0, 0, 0)
     */
    public static TextBillboard createFixedHeight(String text, Color3f color, Font font, float height, ZeroPointLocation zpl) {
        final Texture texture = TextureCreator.getInstance().createTexture(text, color, font, TEXT_ALIGNMENT_HORIZONTAL_LEFT);
        final float width = getWidthFromFixedHeight(texture, height);
        return (new TextBillboard(texture, width, height, zpl, 0.0f));
    }
}
