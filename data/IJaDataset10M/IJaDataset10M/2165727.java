package org.dcm4cheri.image;

import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;
import org.dcm4che.image.ColorModelFactory;
import org.dcm4che.image.ColorModelParam;
import java.awt.image.ColorModel;
import java.util.WeakHashMap;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
public class ColorModelFactoryImpl extends ColorModelFactory {

    private static final Logger log = Logger.getLogger(ColorModelFactoryImpl.class);

    private static final WeakHashMap cache = new WeakHashMap();

    private static final byte[] P2D_IDENTITY = new byte[256];

    static {
        for (int i = 0; i < 256; ++i) P2D_IDENTITY[i] = (byte) i;
    }

    /** Creates a new instance of ColorModelFactoryImpl */
    public ColorModelFactoryImpl() {
    }

    public ColorModel getColorModel(ColorModelParam param) {
        log.debug("Getting a color model from " + param);
        if (!param.isCacheable()) {
            return param.newColorModel();
        }
        ColorModel cm = (ColorModel) cache.get(param);
        if (cm == null) {
            cache.put(param, cm = param.newColorModel());
        }
        return cm;
    }

    public ColorModelParam makeParam(Dataset ds) {
        return makeParam(ds, null);
    }

    public ColorModelParam makeParam(Dataset ds, byte[] pv2dll) {
        String pmi = ds.getString(Tags.PhotometricInterpretation, "MONOCHROME2");
        if ("PALETTE COLOR".equals(pmi)) {
            return new PaletteColorParam(ds);
        }
        boolean mono1 = "MONOCHROME1".equals(pmi);
        if (!mono1 && !"MONOCHROME2".equals(pmi)) {
            throw new UnsupportedOperationException("pmi: " + pmi);
        }
        String pLUTShape = ds.getString(Tags.PresentationLUTShape);
        return new MonochromeParam(ds, pLUTShape == null ? mono1 : "INVERSE".equals(pLUTShape), pv2dll == null ? P2D_IDENTITY : pv2dll);
    }
}
