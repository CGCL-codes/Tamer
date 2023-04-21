package net.cevn.gl;

import net.cevn.texture.TextureMap;
import net.cevn.util.AbstractBounds;
import org.lwjgl.opengl.GL11;

/**
 * The <code>GLQuad</code> class renders, or draws, a quad with a solid color or texture. Set the
 * texture map to <code>null</code> to render a solid color quad. When setting the texture map,
 * the quad takes on the size returned by the getSize() method of the <code>TextureMap</code> class.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 * @see TextureMap
 */
public class GLQuad extends AbstractBounds {

    /**
	 * The color.
	 */
    private GLColor color = GLColor.WHITE;

    /**
	 * The texture map.
	 */
    private TextureMap textureMap = null;

    /**
	 * Creates a new <code>GLQuad</code> instance.
	 */
    public GLQuad() {
        super();
    }

    /**
	 * Creates a new <code>GLQuad</code> with a texture. This sets the
	 * size of this quad to that returned by the getSize() method of
	 * the <code>TextureMap</code> class.
	 * 
	 * @param textureMap The texture map.
	 * @see TextureMap
	 */
    public GLQuad(final TextureMap textureMap) {
        super();
        setTextureMap(textureMap);
    }

    /**
	 * Creates a new <code>GLQuad</code> instance from an original.
	 * 
	 * @param original The original.
	 */
    public GLQuad(final GLQuad original) {
        super(original);
        setTextureMap(new TextureMap(original.getTextureMap()));
        setColor(new GLColor(original.getColor()));
    }

    /**
	 * Sets the texture map. Use <code>null</code> if a solid color quad
	 * is desired. This is the default. The quad takes the size returned
	 * by the getSize() method of the <code>TextureMap</code> class.
	 * 
	 * @param textureMap The texture map.
	 * @see TextureMap
	 */
    public void setTextureMap(final TextureMap textureMap) {
        this.textureMap = textureMap;
        setSize(textureMap.getSize());
    }

    /**
	 * Sets the color of this circle.
	 * 
	 * @param color The color.
	 */
    public void setColor(final GLColor color) {
        if (color == null) {
            throw new NullPointerException("The color of a circle cannot be null");
        }
        this.color = color;
    }

    /**
	 * Gets the texture map. Will return <code>null</code> if this quad does not
	 * use a texture and instead fills with a solid color.
	 * 
	 * @return The texture map, or <code>null</code> if a solid color is used instead.
	 */
    public TextureMap getTextureMap() {
        return textureMap;
    }

    /**
	 * Gets the color.
	 * 
	 * @return The color.
	 */
    public GLColor getColor() {
        return color;
    }

    /**
	 * Renders, or draws, the circle.
	 */
    public void render() {
        GLUtilities.saveGLState();
        GLUtilities.translate(position);
        final float width = size.getWidth();
        final float height = size.getHeight();
        if (textureMap == null) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            color.setGLColor();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(0, height);
            GL11.glVertex2f(width, height);
            GL11.glVertex2f(width, 0);
            GL11.glEnd();
        } else {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            textureMap.bind();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(textureMap.getTextureX(), textureMap.getTextureEndY());
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(textureMap.getTextureX(), textureMap.getTextureY());
            GL11.glVertex2f(0, height);
            GL11.glTexCoord2f(textureMap.getTextureEndX(), textureMap.getTextureY());
            GL11.glVertex2f(width, height);
            GL11.glTexCoord2f(textureMap.getTextureEndX(), textureMap.getTextureEndY());
            GL11.glVertex2f(width, 0);
            GL11.glEnd();
        }
        GLUtilities.restoreGLState();
    }
}
