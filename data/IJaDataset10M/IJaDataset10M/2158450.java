package org.jmage.filter.color;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jmage.filter.ConfigurableImageFilter;
import org.jmage.filter.FilterException;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.Properties;

/**
 * Brightens an RGB Image by a certain percentage. Specify the following imageProperties to change
 * the appearance:<p>
 * <p/>
 * ADJUST: [0-100], set this value to adjust the brightening in percent.
 */
public class BrightenFilter extends ConfigurableImageFilter {

    public static final String ADJUST = "ADJUST";

    protected double adjust = 0;

    protected static Logger log = Logger.getLogger(BrightenFilter.class.getName());

    /**
     * Initializes the ImageFilter with properties
     *
     * @param filterProperties
     */
    public void initialize(Properties filterProperties) throws org.jmage.filter.FilterException {
        try {
            adjust = Double.valueOf(filterProperties.getProperty(ADJUST, "10")).doubleValue();
            assert (adjust >= 0 && adjust <= 100) : ADJUST + " values are only allowed ranging from 0-100, out of range error: " + adjust;
            adjust *= 2.5d;
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
     * Brightens the image
     *
     * @throws org.jmage.filter.FilterException
     *          if an error occurs during filtering.
     */
    public PlanarImage filter(PlanarImage image) throws FilterException {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        int bands = image.getSampleModel().getNumBands();
        double[] matrix = new double[bands];
        for (int i = 0; i <= bands - 1; i++) {
            matrix[i] = adjust;
        }
        pb.add(matrix);
        return JAI.create("addconst", pb, null);
    }
}
