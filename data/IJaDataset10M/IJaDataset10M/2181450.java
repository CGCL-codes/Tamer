package net.sf.ij_plugins.multiband;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.FloatProcessor;

/**
 * @author Jarek Sacha
 */
public class VectorEdgeDetectorPlugin implements PlugIn {

    private static final String PLUGIN_NAME = "Multi-band edge detection";

    private static final String ABOUT_COMMAND = "about";

    private static final String ABOUT_MESSAGE = "Sobel edge detector that supports multi-band and color images.\n" + "Slices in an image stack are interpreted as bands in a multi-band image.\n" + "An RGB image is interpreted as 3 band image. A simple gray image in interpreted as\n" + "as a single band image.";

    /**
     * Values of the <code>arg</code> argument: <ul> <li> 'about' - show short help message. </li>
     * <li> 'VectorSobelEdgeOperator' - invoke {@link VectorSobelEdgeOperator} filter.</li>
     * <li>'VectorGradientEdgeOperator' - invoke {@link VectorGradientEdgeOperator} filter.</li>
     * <li>'VectorDifferenceEdgeOperator' - invoke {@link VectorDifferenceEdgeOperator} filter.</li>
     * </ul>
     */
    @Override
    public void run(final String arg) {
        if (ABOUT_COMMAND.equalsIgnoreCase(arg)) {
            IJ.showMessage("About " + PLUGIN_NAME, ABOUT_MESSAGE);
            return;
        }
        final ImagePlus imp = IJ.getImage();
        if (imp == null) {
            IJ.noImage();
            return;
        }
        final FloatProcessor fp;
        final String postFix;
        if ("VectorSobelEdgeOperator".equalsIgnoreCase(arg)) {
            fp = VectorSobelEdgeOperator.run(imp);
            postFix = "Sobel";
        } else if ("VectorDifferenceEdgeOperator".equalsIgnoreCase(arg)) {
            fp = VectorDifferenceEdgeOperator.run(imp);
            postFix = "difference";
        } else if ("VectorGradientEdgeOperator".equalsIgnoreCase(arg)) {
            fp = VectorGradientEdgeOperator.run(imp);
            postFix = "gradient";
        } else {
            throw new IllegalArgumentException("Invalid invocation argument: " + arg);
        }
        fp.resetMinAndMax();
        final String title = imp.getShortTitle() + " - " + postFix + " edges";
        new ImagePlus(title, fp).show();
    }
}
