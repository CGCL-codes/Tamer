package geometry.drawing;

import geo.draw.CoordinateTransformer;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import util.ImageUtil;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class PolygonDrawerAwt extends PolygonDrawerGraphics {

    private String filename;

    private BufferedImage img;

    private CoordinateTransformer ct;

    private Graphics2D g;

    @Override
    public Graphics2D getGraphics() {
        return g;
    }

    /**
	 * Create a new implementation of the PolygonDrawer interface.
	 * 
	 * @param ct
	 *            the transformation applied to coordinates.
	 * @param filename
	 *            where to save the file on call of {@link #close()}.
	 * @param width
	 *            the width of the image in pixels.
	 * @param height
	 *            the height of the image in pixels.
	 */
    public PolygonDrawerAwt(CoordinateTransformer ct, String filename, int width, int height) {
        this(ct, filename, width, height, false);
    }

    /**
	 * Create a new implementation of the PolygonDrawer interface.
	 * 
	 * @param ct
	 *            the transformation applied to coordinates.
	 * @param filename
	 *            where to save the file on call of {@link #close()}.
	 * @param image
	 *            the image to draw upon.
	 * @param antialiase
	 *            use antialiasing during rendering.
	 */
    public PolygonDrawerAwt(CoordinateTransformer ct, BufferedImage image, String filename, boolean antialiase) {
        super(ct, image.getWidth(), image.getHeight());
        this.ct = ct;
        this.filename = filename;
        this.img = image;
        g = img.createGraphics();
        if (antialiase) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    /**
	 * Create a new implementation of the PolygonDrawer interface.
	 * 
	 * @param ct
	 *            the transformation applied to coordinates.
	 * @param filename
	 *            where to save the file on call of {@link #close()}.
	 * @param width
	 *            the width of the image in pixels.
	 * @param height
	 *            the height of the image in pixels.
	 * @param antialiase
	 *            use antialiasing during rendering.
	 */
    public PolygonDrawerAwt(CoordinateTransformer ct, String filename, int width, int height, boolean antialiase) {
        super(ct, width, height);
        this.ct = ct;
        this.filename = filename;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = img.createGraphics();
        if (antialiase) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    @Override
    public void close() throws IOException {
        ImageIO.write(img, "PNG", new File(filename));
    }

    /**
	 * Retrieve the internal underlying BufferedImage to perform custom drawing operations.
	 * 
	 * @return the underlying BufferedImage.
	 */
    public BufferedImage getImage() {
        return img;
    }

    /**
	 * Get a duplicate of this drawer.
	 * 
	 * @param newFilename
	 *            a new associated filename.
	 * @return a duplicate.
	 */
    public PolygonDrawerAwt duplicate(String newFilename) {
        PolygonDrawerAwt copy = new PolygonDrawerAwt(ct, newFilename, img.getWidth(), img.getHeight());
        copy.img = ImageUtil.duplicate(img);
        copy.g = copy.img.createGraphics();
        return copy;
    }

    @Override
    public void blit(PolygonDrawer toBlit) {
        PolygonDrawerAwt pda = (PolygonDrawerAwt) toBlit;
        getGraphics().drawImage(pda.getImage(), null, 0, 0);
    }
}
