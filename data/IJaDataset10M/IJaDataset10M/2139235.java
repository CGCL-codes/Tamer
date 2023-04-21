package skycastle.gamerenderer.texture.scene;

import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import skycastle.gamerenderer.renderer.GameRendererContext;
import skycastle.util.region.MutableRectangularRegion;
import skycastle.util.region.RectangularRegionImpl;
import java.nio.FloatBuffer;

/**
 * Maintains a scene that can be used to render the terrain texture.
 *
 * @author Hans H�ggstr�m
 */
public class SimpleDynamicScene implements DynamicScene {

    private final int myWidth;

    private final int myHeight;

    private final String myTextureFileName;

    private final MutableRectangularRegion myRegion = new RectangularRegionImpl();

    private Quad myQuad;

    private boolean myUpdateNeeded = true;

    /**
     * @param width  the width of the rendered texture, in pixels.  Should preferably be a power of two (e.g. 64, 128, 256..).
     * @param height the height of the rendered texture, in pixels.  Should preferably be a power of two (e.g. 64, 128, 256..).
     * @param textureName the file name of the texture to use, relative to the classpath.
     */
    public SimpleDynamicScene(final int width, final int height, float worldWidth_m, float worldDepth_m, String textureName) {
        if (width <= 0) {
            throw new IllegalArgumentException("The parameter width should be larger than " + 0 + ", but it was: " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("The parameter height should be larger than " + 0 + ", but it was: " + height);
        }
        myWidth = width;
        myHeight = height;
        myRegion.setSize(worldWidth_m, worldDepth_m);
        myTextureFileName = textureName;
    }

    public void updateVisibleRegion(float centerX, float centerZ) {
        if (myRegion.getCenterX() != centerX && myRegion.getCenterZ() != centerZ) {
            myRegion.setCenter(centerX, centerZ);
            myUpdateNeeded = true;
        }
    }

    public Spatial init(GameRendererContext context) {
        myQuad = new Quad("texturescene", myWidth, myHeight);
        myQuad.setRenderState(context.getTextureService().createTextureState(myTextureFileName));
        return myQuad;
    }

    public void update(float secondsSinceLastCall, GameRendererContext context, final boolean cameraPositionChanged) {
        if (myUpdateNeeded) {
            final FloatBuffer textureBuffer = myQuad.getTextureBuffer(0, 0);
            final float textureSize_m = 2000.0f;
            textureBuffer.clear();
            textureBuffer.put(myRegion.getMinX() / textureSize_m);
            textureBuffer.put(myRegion.getMaxZ() / textureSize_m);
            textureBuffer.put(myRegion.getMinX() / textureSize_m);
            textureBuffer.put(myRegion.getMinZ() / textureSize_m);
            textureBuffer.put(myRegion.getMaxX() / textureSize_m);
            textureBuffer.put(myRegion.getMinZ() / textureSize_m);
            textureBuffer.put(myRegion.getMaxX() / textureSize_m);
            textureBuffer.put(myRegion.getMaxZ() / textureSize_m);
            myQuad.updateRenderState();
            myUpdateNeeded = false;
        }
    }

    public int getWidth() {
        return myWidth;
    }

    public int getHeight() {
        return myHeight;
    }
}
