package hu.ihash.metadata.helper;

import hu.ihash.metadata.ImageMetadataException;
import java.awt.Dimension;
import java.util.Map;
import org.w3c.dom.Node;

/**
 * A format helper for GIF files.
 * 
 * @author Gergely Kiss
 */
public class GIFFormatHelper extends DefaultFormatHelper {

    private static final String GIF_FORMAT = "javax_imageio_gif_image_1.0";

    protected final NodeWalker nw = new NodeWalker();

    @Override
    public int getBitsPerPixel(Map<String, Node> roots) {
        return 8;
    }

    @Override
    public Dimension getSize(Map<String, Node> roots) {
        Node root = roots.get(GIF_FORMAT);
        try {
            int w = nw.at(root).e("ImageDescriptor").a("imageWidth").intValue();
            int h = nw.at(root).e("ImageDescriptor").a("imageHeight").intValue();
            return new Dimension(w, h);
        } catch (Exception e) {
            throw new ImageMetadataException("Failed to parse GIF size.", e);
        }
    }

    @Override
    public String getFormatType() {
        return GIF_FORMAT;
    }
}
