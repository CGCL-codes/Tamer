package skycastle.gamerenderer.terrain;

import com.jme.image.Texture;
import skycastle.gamerenderer.renderer.GameRendererContext;

/**
 *
 *
 * @author Hans H�ggstr�m
 */
public interface TerrainTextureProvider {

    Texture getGroundTexture(final GameRendererContext context);

    Texture getSecondaryGroundTexture(final GameRendererContext context);
}
