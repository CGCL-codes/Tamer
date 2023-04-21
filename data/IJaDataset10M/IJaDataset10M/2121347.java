package org.jmage.filter.color;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jmage.filter.ConfigurableImageFilter;
import org.jmage.filter.FilterException;
import org.jmage.util.ColorUtil;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.Properties;

/**
 * Colorizes an existing RGB colored image with a specified color. Specify the following imageProperties to change
 * the appearance:<p>
 * <p/>
 * COLOR: [RRGGBB] the color to colorize with.
 */
public class ColorizeFilter extends ConfigurableImageFilter {

    public static final String COLOR = "COLOR";

    public static final String DEFAULTCOLOR = "FFFFFF";

    protected byte[][] lut;

    protected LookupTableJAI table = null;

    protected static Logger log = Logger.getLogger(ColorizeFilter.class.getName());

    /**
     * Initializes the ImageFilter with properties
     *
     * @param filterProperties
     */
    public void initialize(Properties filterProperties) throws org.jmage.filter.FilterException {
        try {
            String color = filterProperties.getProperty(COLOR, DEFAULTCOLOR);
            int[] rgb = null;
            rgb = ColorUtil.decodeRGBString(color);
            lut = new byte[3][256];
            for (int i = 0; i < 256; i++) {
                lut[0][i] = (byte) (this.clamp((int) (i * (rgb[0] / 256d))));
                lut[1][i] = (byte) (this.clamp((int) (i * (rgb[1] / 256d))));
                lut[2][i] = (byte) (this.clamp((int) (i * (rgb[2] / 256d))));
            }
            table = new LookupTableJAI(lut);
            this.filterProperties = filterProperties;
            if (log.isDebugEnabled()) log.debug(INITIALIZED);
        } catch (Throwable t) {
            String message = NOT_INITIALIZED + t.getMessage();
            this.filterProperties = null;
            if (log.isEnabledFor(Priority.ERROR)) log.error(message);
            throw new FilterException(message);
        }
    }

    /**
     * Color the image
     *
     * @throws org.jmage.filter.FilterException
     *          if an error occurs during filtering
     */
    public PlanarImage filter(PlanarImage image) throws FilterException {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(table);
        return JAI.create("lookup", pb, null);
    }

    private int clamp(int i) {
        if (i > 255) {
            i = 255;
        }
        if (i < 0) {
            i = 0;
        }
        return i;
    }
}
