package de.lessvoid.nifty.jme.render;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureKey;
import com.jme.util.TextureManager;
import de.lessvoid.nifty.spi.render.RenderImage;

public class JmeRenderImage implements RenderImage {

    private Texture texture;

    private TextureState textureState;

    private Vector2i originalSize;

    private static Map<TextureKey, Vector2i> sizeCache = new HashMap<TextureKey, Vector2i>();

    public JmeRenderImage(final URL url, final boolean filterParam) {
        if (filterParam) {
            texture = TextureManager.loadTexture(url, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 0.0f, true);
        } else {
            texture = TextureManager.loadTexture(url, Texture.MinificationFilter.NearestNeighborNoMipMaps, Texture.MagnificationFilter.NearestNeighbor, Image.Format.GuessNoCompression, 0.0f, true);
        }
        synchronized (sizeCache) {
            TextureKey key = texture.getTextureKey();
            originalSize = sizeCache.get(key);
            if (originalSize == null) {
                originalSize = new Vector2i(texture.getImage().getWidth(), texture.getImage().getHeight());
                sizeCache.put(key, originalSize);
            }
        }
        textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        textureState.setEnabled(true);
        textureState.setTexture(texture);
    }

    public int getWidth() {
        return texture.getImage().getWidth();
    }

    public int getHeight() {
        return texture.getImage().getHeight();
    }

    public int getTextureWidth() {
        return (int) originalSize.getX();
    }

    public int getTextureHeight() {
        return (int) originalSize.getY();
    }

    public TextureState getTextureState() {
        return textureState;
    }

    public void dispose() {
    }

    class Vector2i {

        private final int x;

        private final int y;

        public Vector2i(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        /**
     * @return the x
     */
        public int getX() {
            return x;
        }

        /**
     * @return the y
     */
        public int getY() {
            return y;
        }
    }
}
