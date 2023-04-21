package com.jmex.awt.swingui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.glu.GLU;
import com.jme.image.Texture;
import com.jme.system.DisplaySystem;

/**
 * LWJGL implementation of {@link ImageGraphics}.
 */
class LWJGLImageGraphics extends ImageGraphicsBaseImpl {

    private LWJGLImageGraphics(BufferedImage awtImage, byte[] data, Graphics2D delegate, com.jme.image.Image image, Rectangle dirty, int translationX, int translationY, float scaleX, float scaleY, int mipMapCount, ImageGraphicsBaseImpl mipMapChild, int mipMapLevel) {
        super(awtImage, data, delegate, image, dirty, translationX, translationY, scaleX, scaleY, mipMapCount, mipMapChild, mipMapLevel);
    }

    protected LWJGLImageGraphics(int width, int height, int paintedMipMapCount) {
        this(width, height, paintedMipMapCount, 0, 1);
    }

    private LWJGLImageGraphics(int width, int height, int paintedMipMapCount, int mipMapLevel, float scale) {
        super(width, height, paintedMipMapCount, mipMapLevel, scale);
        if (paintedMipMapCount > 0 && (width > 1 || height > 1)) {
            if (width < 2) {
                width = 2;
            }
            if (height < 2) {
                height = 2;
            }
            mipMapChild = new LWJGLImageGraphics(width / 2, height / 2, paintedMipMapCount, mipMapLevel + 1, scale * 0.5f);
        }
    }

    public void update(Texture texture, boolean clean) {
        boolean updateChildren = false;
        synchronized (dirty) {
            if (!tx.isIdentity()) {
                dirty.setBounds(getImageBounds());
            } else if (!dirty.isEmpty() && isExpandDirtyRegion()) {
                dirty.grow(2, 2);
            }
            Rectangle2D.intersect(dirty, getImageBounds(), dirty);
            if (!this.dirty.isEmpty()) {
                try {
                    Util.checkGLError();
                } catch (OpenGLException e) {
                    throw new RuntimeException("OpenGLException caused before any GL commands by LWJGLImageGraphics!", e);
                }
                boolean hasMipMaps = texture.getMinificationFilter().usesMipMapLevels();
                update();
                if (!glTexSubImage2DSupported || (hasMipMaps && paintedMipMapCount == 0)) {
                    if (!hasMipMaps) {
                        DisplaySystem.getDisplaySystem().getRenderer().updateTextureSubImage(texture, 0, 0, image, 0, 0, image.getWidth(), image.getHeight());
                    } else {
                        idBuff.clear();
                        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, idBuff);
                        int oldTex = idBuff.get();
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
                        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                        ByteBuffer data = image.getData(0);
                        data.rewind();
                        GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, oldTex);
                    }
                    Util.checkGLError();
                } else {
                    DisplaySystem.getDisplaySystem().getRenderer().updateTextureSubImage(texture, dirty.x, dirty.y, image, dirty.x, dirty.y, dirty.width, dirty.height);
                    try {
                        Util.checkGLError();
                    } catch (OpenGLException e) {
                        logger.warning("Error updating dirty region: " + dirty + " - " + "falling back to updating whole image!");
                        glTexSubImage2DSupported = false;
                        update(texture, clean);
                    }
                    updateChildren = mipMapChild != null;
                }
            }
        }
        if (updateChildren) {
            dirty.x &= ~1;
            dirty.y &= ~1;
            if ((dirty.width & 1) != 0) {
                dirty.width++;
            }
            if ((dirty.height & 1) != 0) {
                dirty.height++;
            }
            int dx1 = (int) ((dirty.x - translation.x) / scaleX);
            int dy1 = (int) ((dirty.y - translation.y) / scaleY);
            int dx2 = (int) ((dirty.x + dirty.width - translation.x) / scaleX);
            int dy2 = (int) ((dirty.y + dirty.height - translation.y) / scaleY);
            int dw = dx2 - dx1;
            int dh = dy2 - dy1;
            mipMapChild.setClip(dx1, dy1, dw, dh);
            mipMapChild.delegate.clearRect(dx1, dy1, dw, dh);
            mipMapChild.delegate.drawImage(awtImage, dx1, dy1, dx2, dy2, dirty.x, dirty.y, dirty.x + dirty.width, dirty.y + dirty.height, null);
            mipMapChild.makeDirty(dx1, dy1, dw, dh);
            mipMapChild.update(texture, clean);
        }
        if (clean) {
            this.dirty.width = 0;
        }
    }

    public Graphics create() {
        return new LWJGLImageGraphics(awtImage, data, (Graphics2D) delegate.create(), image, dirty, translation.x, translation.y, scaleX, scaleY, paintedMipMapCount, mipMapChild != null && mipMapLevel < paintedMipMapCount - 1 ? (ImageGraphicsBaseImpl) mipMapChild.create() : null, mipMapLevel);
    }
}
