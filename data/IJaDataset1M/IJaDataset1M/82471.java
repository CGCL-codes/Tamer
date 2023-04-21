package net.sf.ij_plugins.color;

import ij.IJ;
import ij.ImagePlus;
import ij.Macro;
import ij.plugin.PlugIn;
import ij.process.ColorProcessor;
import net.sf.ij_plugins.multiband.VectorProcessor;

/**
 * Converts image pixels from CIE L*a*b* color space to RGB color space.
 *
 * @author Jarek Sacha
 */
public class ConvertLabStackToRGBPlugin implements PlugIn {

    private static final String PLUGIN_NAME = "Convert CIE L*a*b* to RGB";

    private static final String ABOUT_COMMAND = "about";

    private static final String ABOUT_MESSAGE = "Converts image pixels from CIE L*a*b* color space to RGB color space.\n" + "The CIE L*a*b* image is assumed to be a stack of floating point images (32 bit).\n" + "The L* band is in the range 0 to 100, the a* band and the b* band between -100\n" + "and 100\n" + "Conversions assume observer = 2°, illuminant = D65, and use formulas provided at:\n" + "http://www.brucelindbloom.com";

    private static WhiteReference whiteReference = WhiteReference.A;

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
        if (imp.getType() != ImagePlus.GRAY32 || imp.getStackSize() != 3) {
            IJ.showMessage(PLUGIN_NAME, "Conversion supported for GRAY32 stacks with three slices.");
            Macro.abort();
            return;
        }
        IJ.showStatus(PLUGIN_NAME);
        final VectorProcessor vp = new VectorProcessor(imp.getStack());
        final ColorProcessor cp = ColorSpaceConversion.labToColorProcessor(vp);
        final ImagePlus dest = new ImagePlus(imp.getTitle() + " - RGB", cp);
        dest.show();
    }
}
