package resource;

import javax.media.opengl.GL;
import resource.meta.SpriteMeta;

/**
 * A factory for creating sprites.
 * 
 * @author tom
 * 
 */
public class SpriteFactory extends ResourceFactory<Sprite, SpriteMeta, GL> {

    /**
	 * Create a new sprite factory.
	 * 
	 * @param cacheSize
	 *            The maximum size of the cache.
	 */
    public SpriteFactory(int cacheSize) {
        super(cacheSize);
    }

    @Override
    protected Sprite createResource(String path, ResourceLoader loader, SpriteMeta meta, GL gl) throws LoadResourceException {
        Texture t = loader.getTexture(meta.getTexturePath(), gl);
        int top = meta.getTop();
        if (top < 0) top = 0;
        int right = meta.getRight();
        if (right < 0) right = t.getWidth();
        int bottom = meta.getBottom();
        if (bottom < 0) bottom = t.getHeight();
        int left = meta.getLeft();
        if (left < 0) left = 0;
        return new Sprite(t, meta.getWidth(), meta.getHeight(), left, top, right, bottom);
    }
}
