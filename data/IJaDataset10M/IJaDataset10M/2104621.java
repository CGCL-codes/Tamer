package org.jaitools.demo.regionalize;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.util.List;
import java.util.Map;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import org.jaitools.CollectionFactory;
import org.jaitools.demo.DemoImages;
import org.jaitools.imageutils.ImageUtils;
import org.jaitools.media.jai.regionalize.Region;
import org.jaitools.media.jai.regionalize.RegionalizeDescriptor;
import org.jaitools.swing.ImageFrame;

/**
 * Demonstrates using the Regionalize operation to identify regions
 * of uniform value in a source image.
 *
 * @author Michael Bedward
 * @since 1.0
 * @version $Id: RegionalizeDemo.java 1721 2011-06-16 08:21:06Z michael.bedward $
 */
public class RegionalizeDemo {

    /**
     * Main method: simple calls the demo method
     * @param args ignored
     */
    public static void main(String[] args) {
        RegionalizeDemo me = new RegionalizeDemo();
        me.demo();
    }

    /**
     * Gets a test image (the chessboard image) from the
     * {@linkplain DemoImages object}. When the image
     * has been created the receiveImage method will be called.
     */
    public void demo() {
        RenderedImage image = DemoImages.createChessboardImage(320, 320);
        ImageFrame frame;
        frame = new ImageFrame(image, "Regionalize demo: test image");
        frame.setVisible(true);
        regionalizeImage(image);
    }

    /**
     * Regionalizes the test chessboard image in two ways:
     * firstly with only orthogonal connectedness; then
     * allowing diagonal connectedness. Displays the results
     * of each regionalization in an {@linkplain ImageFrame}.
     *
     * @param image the test image
     */
    public void regionalizeImage(RenderedImage image) {
        ImageFrame frame;
        ParameterBlockJAI pb = new ParameterBlockJAI("regionalize");
        pb.setSource("source0", image);
        pb.setParameter("diagonal", false);
        RenderedOp orthoImg = JAI.create("Regionalize", pb);
        orthoImg.getData();
        List<Region> regions = (List<Region>) orthoImg.getProperty(RegionalizeDescriptor.REGION_DATA_PROPERTY);
        for (Region r : regions) {
            System.out.println(r);
        }
        Color[] colors = ImageUtils.createRampColours(regions.size());
        Map<Integer, Color> colorMap = CollectionFactory.map();
        int k = 0;
        for (Region r : regions) {
            colorMap.put(r.getId(), colors[k++]);
        }
        RenderedImage displayImg = ImageUtils.createDisplayImage(orthoImg, colorMap);
        frame = new ImageFrame(displayImg, orthoImg, "Regions with orthogonal connection");
        frame.setVisible(true);
        pb = new ParameterBlockJAI("regionalize");
        pb.setSource("source0", image);
        pb.setParameter("diagonal", true);
        RenderedOp diagImg = JAI.create("regionalize", pb);
        colorMap.clear();
        colorMap.put(1, Color.CYAN);
        colorMap.put(2, Color.ORANGE);
        RenderedImage diagDisplayImg = ImageUtils.createDisplayImage(diagImg, colorMap);
        frame = new ImageFrame(diagDisplayImg, diagImg, "Regions with diagonal connection");
        frame.setVisible(true);
    }
}
