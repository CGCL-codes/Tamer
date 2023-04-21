package net.openchrom.chromatogram.msd.comparison.supplier.incos.comparator;

import net.openchrom.chromatogram.msd.comparison.exceptions.ComparisonException;
import net.openchrom.chromatogram.msd.comparison.spectrum.IMassSpectrumComparator;
import net.openchrom.chromatogram.msd.comparison.spectrum.IMassSpectrumComparisonResult;
import net.openchrom.chromatogram.msd.comparison.supplier.incos.model.INCOSMassSpectrumComparisonResult;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.xic.IIonRange;
import net.openchrom.logging.core.Logger;

/**
 * This class gives back a IMassSpectrumComparisonResult which implements the
 * INCOS mass spectrum comparison algorithm.
 * 
 * @author eselmeister
 */
public class INCOSMassSpectrumComparator implements IMassSpectrumComparator {

    public static final String COMPARATOR_ID = "net.openchrom.chromatogram.msd.comparison.supplier.incos";

    private static final Logger logger = Logger.getLogger(INCOSMassSpectrumComparator.class);

    @Override
    public IMassSpectrumComparisonResult compare(IMassSpectrum unknown, IMassSpectrum reference, IIonRange ionRange) {
        INCOSMassSpectrumComparisonResult result = null;
        try {
            result = new INCOSMassSpectrumComparisonResult(unknown, reference, ionRange);
        } catch (ComparisonException e) {
            logger.warn(e);
            return null;
        }
        return result;
    }
}
