package net.sourceforge.jetdog.gfx.layer;

import net.sourceforge.jetdog.gfx.Layer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * An ImageLayer is a Layer with an image drawn at the back.
 * Used for what we'd normally call a "background image." 
 * 
 * @author Jonathan Chung
 */
public class ImageLayer extends Layer {

    private Image image;

    /**
	 * Creates an image layer with the given image.
	 * Use the image size to determine the layer size.
	 * 
	 * @param image Image to use as background
	 */
    public ImageLayer(Image image) {
        this(image, image.getWidth(), image.getHeight());
    }

    /**
	 * Creates and image layer with the given image
	 * 
	 * @param image Image to use as background
	 * @param width Layer width
	 * @param height Layer height
	 */
    public ImageLayer(Image image, int width, int height) {
        super(width, height);
        viewport.setDimension(width, height);
        this.image = image;
    }

    /**
	 * Sets this layer's image.
	 * 
	 * @param image Image
	 */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
	 * Returns this layer's width.
	 * 
	 * @return Width of this layer
	 */
    public int getWidth() {
        return (int) getViewport().getClip().getWidth();
    }

    /**
	 * Returns this layer's height.
	 * 
	 * @return Height of this layer
	 */
    public int getHeight() {
        return (int) getViewport().getClip().getHeight();
    }

    /**
	 * Returns this layer's image.
	 * 
	 * @return This layer's image
	 */
    public Image getImage() {
        return image;
    }

    /**
	 * Render this Layer to the game's graphics context.
	 * 
	 * @param g The graphics context to render to.
	 */
    public void render(Graphics g) {
        Viewport viewport = getViewport();
        float x = viewport.getX();
        float y = viewport.getY();
        getSnippetManager().preRender(g, x, y);
        if (image != null) {
            float clipx = viewport.getClip().getX();
            float clipy = viewport.getClip().getY();
            float width = viewport.getClip().getWidth();
            float height = viewport.getClip().getHeight();
            Image sub = image.getSubImage((int) x, (int) y, (int) width, (int) height);
            sub.draw(clipx, clipy);
        }
        getSnippetManager().render(g, x, y);
    }
}
