package org.spantus.extractor.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;

/**
 * 
 * 
 * @author Mindaugas Greibus
 *
 * @since 0.0.1
 * 
 * Created 2008.05.24
 *
 */
public class HarmonicProductSpectrum extends AbstractSpectralExtractor {

    public FrameValues calculateWindow(FrameValues window) {
        FrameVectorValues val3d = calculateFFT(window);
        FrameValues rtnValues = super.calculateWindow(window);
        for (List<Double> fv : val3d) {
            BigDecimal product = BigDecimal.valueOf(1).setScale(6);
            for (Double f1 : fv) {
                if (f1 == 0) continue;
                product = product.multiply(BigDecimal.valueOf(f1).abs(), MathContext.DECIMAL32);
            }
        }
        return rtnValues;
    }

    public String getName() {
        return ExtractorEnum.SPECTRAL_CENTROID_EXTRACTOR.name();
    }
}
