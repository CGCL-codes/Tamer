package org.dcm4che2.hp.plugins;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.hp.HPSelector;
import org.dcm4che2.hp.spi.HPSelectorSpi;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 5516 $ $Date: 2007-11-23 06:42:30 -0500 (Fri, 23 Nov 2007) $
 * @since Aug 6, 2005
 * 
 */
public class ImagePlaneSelectorSpi extends HPSelectorSpi {

    private static final String[] CATEGORIES = { "IMAGE_PLANE" };

    private static final float MIN_MIN_COSINE = 0.8f;

    private float minCosine = ImagePlaneSelector.DEF_MIN_COSINE;

    public ImagePlaneSelectorSpi() {
        super(CATEGORIES);
    }

    @Override
    public void setProperty(String name, Object value) {
        if (!"MinCosine".equals(name)) throw new IllegalArgumentException("Unsupported property: " + name);
        float tmp = ((Float) value).floatValue();
        if (tmp < MIN_MIN_COSINE || tmp > 1f) throw new IllegalArgumentException("minCosine: " + value);
        minCosine = tmp;
    }

    @Override
    public Object getProperty(String name) {
        if (!"MinCosine".equals(name)) throw new IllegalArgumentException("Unsupported property: " + name);
        return new Float(minCosine);
    }

    @Override
    public HPSelector createHPSelector(DicomObject filterOp) {
        ImagePlaneSelector sel = new ImagePlaneSelector(filterOp);
        sel.setMinCosine(minCosine);
        return sel;
    }
}
