package net.openchrom.chromatogram.msd.comparison.supplier.incos.model;

import net.openchrom.chromatogram.msd.comparison.exceptions.ComparisonException;
import net.openchrom.chromatogram.msd.comparison.spectrum.AbstractMassSpectrumComparisonResult;
import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.IonLimitExceededException;
import net.openchrom.chromatogram.msd.model.implementation.DefaultIon;
import net.openchrom.chromatogram.msd.model.implementation.DefaultMassSpectrum;
import net.openchrom.chromatogram.msd.model.xic.IExtractedIonSignal;
import net.openchrom.chromatogram.msd.model.xic.IIonRange;
import net.openchrom.logging.core.Logger;

/**
 * This class represents a incos mass spectrum comparison result.
 * 
 * @author eselmeister
 */
public class INCOSMassSpectrumComparisonResult extends AbstractMassSpectrumComparisonResult {

    private static final Logger logger = Logger.getLogger(INCOSMassSpectrumComparisonResult.class);

    public static final String DESCRIPTION = "INCOS";

    private static final int NORMALIZATION_FACTOR = 100;

    private float matchQuality = 0.0f;

    public INCOSMassSpectrumComparisonResult(IMassSpectrum unknown, IMassSpectrum reference, IIonRange ionRange) throws ComparisonException {
        super(unknown, reference, ionRange);
        unknown = adjustMassSpectrum(unknown, ionRange);
        reference = adjustMassSpectrum(reference, ionRange);
        matchQuality = calculateGeometricDistanceMatchQuality(unknown, reference, ionRange);
        setDescription(DESCRIPTION);
    }

    @Override
    public float getMatchQuality() {
        return matchQuality;
    }

    /**
	 * This method will calculate new abundance values in the following manner:<br/>
	 * For each ion the new abundance will be set to:<br/>
	 * <br/>
	 * Inew = sqrt(I * Ion);<br/>
	 * <br/>
	 * Set the highest intensity value to 100 so that no problems will occur
	 * when you calculate the new abundance values. A new mass spectrum will be
	 * returned.
	 */
    private IMassSpectrum adjustMassSpectrum(IMassSpectrum massSpectrum, IIonRange ionRange) {
        IMassSpectrum adjustedMassSpectrum = new DefaultMassSpectrum();
        IIon adjustedIon;
        massSpectrum.normalize(NORMALIZATION_FACTOR);
        int startIon = ionRange.getStartIon();
        int stopIon = ionRange.getStopIon();
        IExtractedIonSignal signal;
        signal = massSpectrum.getExtractedIonSignal(startIon, stopIon);
        float abundance;
        for (int ion = startIon; ion <= stopIon; ion++) {
            abundance = signal.getAbundance(ion);
            if (abundance >= 0) {
                try {
                    adjustedIon = new DefaultIon(ion, (float) Math.sqrt(abundance * ion));
                    adjustedMassSpectrum.addIon(adjustedIon);
                } catch (AbundanceLimitExceededException e) {
                    logger.warn(e);
                } catch (IonLimitExceededException e) {
                    logger.warn(e);
                }
            }
        }
        return adjustedMassSpectrum;
    }
}
