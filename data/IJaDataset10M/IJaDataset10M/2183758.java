package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d.utils.image;

/**
 * The listener interface which is called when a ImageComponent2DURL is
 * loaded from the scenegraph file
 */
public interface ImageComponent2DURLIOListener {

    /**
     * The listener method which is called when a ImageComponent2DURL is
     * loaded from the scenegraph file.
     *
     * This method must return a valid ImageComponent2DURL, the returned object
     * will be placed in the scene graph.
     *
     * @param format The image format from ImageComponent
     * @param width The image width from ImageComponent
     * @param height The image height from ImageComponent
     * @param byReference The byReference flag from ImageComponent
     * @param yUp The yUp flag from ImageComponent
     * @param url The URL for the image component
     */
    public ImageComponent2DURL createImageComponent(int format, int width, int height, boolean byReference, boolean yUp, java.net.URL url);
}
