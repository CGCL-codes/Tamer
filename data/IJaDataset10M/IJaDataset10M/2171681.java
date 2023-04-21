package papertoolkit.render.regions;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.media.jai.PlanarImage;
import papertoolkit.paper.regions.ImageRegion;
import papertoolkit.render.RegionRenderer;
import papertoolkit.units.Points;
import papertoolkit.units.Units;
import papertoolkit.util.DebugUtils;
import papertoolkit.util.graphics.ImageCache;
import papertoolkit.util.graphics.JAIUtils;

/**
 * <p>
 * Renders an ImageRegion to a graphics context or PDF file.
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 */
public class ImageRenderer extends RegionRenderer {

    /**
	 * The region to render.
	 */
    private ImageRegion imgRegion;

    /**
	 * @param region
	 */
    public ImageRenderer(ImageRegion region) {
        super(region);
        imgRegion = region;
    }

    /**
	 * Render the image to a graphics context, given the pixels per inch scaling.
	 * 
	 * @see papertoolkit.render.RegionRenderer#renderToG2D(java.awt.Graphics2D)
	 */
    public void renderToG2D(Graphics2D g2d) {
        if (RegionRenderer.DEBUG_REGIONS) {
            super.renderToG2D(g2d);
        }
        final File file = imgRegion.getFile();
        final Units units = imgRegion.getUnits();
        final double ppi = imgRegion.getPixelsPerInch();
        final double ppiConversion = 72 / ppi;
        final double conv = units.getScalarMultipleToConvertTo(new Points());
        PlanarImage image = ImageCache.loadPlanarImage(file);
        if (imgRegion.isActive()) {
            image = JAIUtils.blur(image);
        }
        final AffineTransform transform = new AffineTransform();
        transform.translate(imgRegion.getX() * conv, imgRegion.getY() * conv);
        transform.scale(imgRegion.getScaleX(), imgRegion.getScaleY());
        transform.scale(ppiConversion, ppiConversion);
        g2d.drawRenderedImage(image, transform);
    }
}
