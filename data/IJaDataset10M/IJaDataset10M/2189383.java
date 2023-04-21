package skycastle.util.view2d.layer;

import skycastle.util.region.RectangularRegion;
import skycastle.util.view2d.camera.Camera2D;
import skycastle.util.view2d.camera.CameraListener;
import skycastle.util.view2d.renderer.RendererListener;
import java.awt.image.BufferedImage;

/**
 * Helper class for the LayeredView.  Contains a renderer, and if the renderer is used in buffered mode, an image buffer.
 * <p/>
 * A layer may be cached if it is also buffered, if so, the buffer is stored in the layer, and re-drawn only when the
 * layer image changes for some area.
 * <p/>
 * If a layer is not cached, it uses a common image buffer if it is buffered, or draws directly on the target image
 * if unbuffered.
 * <p/>
 * There may also be an overcaching policy for the view, if so, the cached image is rendered larger than the
 * view size, allowing some panning to occur without re-rendering the image.
 * <p/>
 * The layer keeps track of which parts of the cache it holds outside the view are up-to-date, and which ones will need to be re-drawn..
 * <p/>
 * The layer can also support rendering itself in a separarte thread.
 * <p/>
 * REFACTOR: Perhaps combine the layer and renderer concepts, and add an abstract base class for renderers that takes care of buffering and such.
 *
 * @author Hans H�ggstr�m
 */
public interface Layer extends CameraListener {

    /**
     * Renders the layer to a buffer.
     *
     * @param target       the buffer to render to.
     * @param screenRegion the region of the target buffer to render to.  Coordinates in pixels.
     * @param worldRegion  the world region to render to the specified screen region.
     * @param camera       the camera transformation used.
     */
    void render(BufferedImage target, RectangularRegion screenRegion, RectangularRegion worldRegion, Camera2D camera);

    /**
     * @param listener a listener that is notified when the rendered picture changes for some specified area,
     *                 and should be re-drawn there
     */
    void addRendererListener(RendererListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeRendererListener(RendererListener listener);
}
