package net.openchrom.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.exceptions.FilterException;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.model.INoiseMassSpectrum;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.settings.SegmentWidth;
import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.ICombinedMassSpectrum;
import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.ISupplierMassSpectrum;
import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;
import net.openchrom.chromatogram.msd.model.core.support.IMarkedIons;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.AnalysisSupportException;
import net.openchrom.chromatogram.msd.model.exceptions.IonLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.NoExtractedIonSignalStoredException;
import net.openchrom.chromatogram.msd.model.implementation.DefaultIon;
import net.openchrom.chromatogram.msd.model.xic.ExtractedIonSignalsModifier;
import net.openchrom.chromatogram.msd.model.xic.IExtractedIonSignal;
import net.openchrom.chromatogram.msd.model.xic.IExtractedIonSignals;
import net.openchrom.logging.core.Logger;
import net.openchrom.numeric.statistics.Calculations;
import org.eclipse.core.runtime.IProgressMonitor;

public class Denoising {

    private static final Logger logger = Logger.getLogger(Denoising.class);

    /**
	 * Use only static methods.
	 */
    private Denoising() {
    }

    /**
	 * Tries to remove ions according to noise and to lower the
	 * signals of noise.
	 * 
	 * @param chromatogramSelection
	 * @throws FilterException
	 */
    public static List<ICombinedMassSpectrum> applyDenoisingFilter(IChromatogramSelection chromatogramSelection, IMarkedIons ionsToRemove, IMarkedIons ionsToPreserve, boolean adjustThresholdTransitions, int numberOfUsedIonsForCoefficient, SegmentWidth segmentWidth, IProgressMonitor monitor) throws FilterException {
        if (ionsToRemove == null) {
            throw new FilterException("The ions to remove instance was null.");
        }
        if (ionsToPreserve == null) {
            throw new FilterException("The ions to preserve instance was null.");
        }
        IChromatogram chromatogram = chromatogramSelection.getChromatogram();
        IExtractedIonSignals extractedIonSignals = chromatogram.getExtractedIonSignals(chromatogramSelection);
        Calculator calculator = new Calculator();
        monitor.subTask("Remove selected ions.");
        extractedIonSignals = removeIonsInScanRange(extractedIonSignals, ionsToRemove, monitor);
        if (adjustThresholdTransitions) {
            try {
                monitor.subTask("Adjust threshold transitions.");
                ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
            } catch (AnalysisSupportException e) {
                logger.warn(e);
            }
        }
        List<INoiseSegment> noiseSegments = calculator.getNoiseSegments(extractedIonSignals, ionsToPreserve, segmentWidth, monitor);
        List<ICombinedMassSpectrum> noiseMassSpectra = subtractNoiseMassSpectraFromSegments(extractedIonSignals, noiseSegments, ionsToPreserve, numberOfUsedIonsForCoefficient, monitor);
        monitor.subTask("Write the results.");
        writeExtractedIonSignalsBackToChromatogram(chromatogram, extractedIonSignals, monitor);
        return noiseMassSpectra;
    }

    /**
	 * Removes the given ions from the scan range (start/stop scan).
	 * 
	 * @param extractedIonSignals
	 * @param startScan
	 * @param stopScan
	 * @param ionsToRemove
	 * @param monitor
	 */
    private static IExtractedIonSignals removeIonsInScanRange(IExtractedIonSignals extractedIonSignals, IMarkedIons ionsToRemove, IProgressMonitor monitor) {
        int startScan = extractedIonSignals.getStartScan();
        int stopScan = extractedIonSignals.getStopScan();
        IExtractedIonSignal extractedIonSignal;
        for (int scan = startScan; scan <= stopScan; scan++) {
            monitor.subTask("Remove calculated noise from scan: " + scan);
            try {
                extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
                removeIons(extractedIonSignal, ionsToRemove);
            } catch (NoExtractedIonSignalStoredException e) {
                logger.warn(e);
            }
        }
        return extractedIonSignals;
    }

    /**
	 * Removes the selected ions from the given extracted ion signal.
	 */
    private static void removeIons(IExtractedIonSignal extractedIonSignal, IMarkedIons selectedIons) {
        for (int ion : selectedIons.getIonsNominal()) {
            extractedIonSignal.setAbundance(ion, 0.0f, true);
        }
    }

    /**
	 * Subtracts the noise mass spectrum from the given scan range.
	 * 
	 * @param extractedIonSignals
	 * @param noiseMassSpectrum
	 * @param startScan
	 * @param stopScan
	 * @param monitor
	 */
    private static void subtractNoiseMassSpectrumFromScanRange(IExtractedIonSignals extractedIonSignals, ICombinedMassSpectrum noiseMassSpectrum, int startScan, int stopScan, int numberOfUsedIonsForCoefficient, IProgressMonitor monitor) {
        IExtractedIonSignal extractedIonSignal;
        for (int scan = startScan; scan <= stopScan; scan++) {
            try {
                extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
                subtractNoiseMassSpectrumFromScan(extractedIonSignal, noiseMassSpectrum, numberOfUsedIonsForCoefficient, monitor);
            } catch (NoExtractedIonSignalStoredException e) {
                logger.warn(e);
            }
        }
    }

    /**
	 * Subtracts the noise mass spectrum from the scan.
	 * 
	 * @param extractedIonSignal
	 * @param noiseMassSpectrum
	 * @param monitor
	 */
    private static void subtractNoiseMassSpectrumFromScan(IExtractedIonSignal extractedIonSignal, ICombinedMassSpectrum noiseMassSpectrum, int numberOfUsedIonsForCoefficient, IProgressMonitor monitor) {
        IExtractedIonSignal noiseSignal = noiseMassSpectrum.getExtractedIonSignal();
        float correlationFactor = calculateCoefficient(extractedIonSignal, noiseSignal, numberOfUsedIonsForCoefficient);
        if (correlationFactor <= 0) {
            return;
        }
        int startIon = noiseSignal.getStartIon();
        int stopIon = noiseSignal.getStopIon();
        float abundance;
        float subtractAbundance;
        float newAbundance;
        for (int ion = startIon; ion <= stopIon; ion++) {
            abundance = extractedIonSignal.getAbundance(ion);
            if (abundance > 0) {
                subtractAbundance = correlationFactor * noiseSignal.getAbundance(ion);
                newAbundance = abundance - subtractAbundance;
                if (newAbundance <= 0.0f) {
                    extractedIonSignal.setAbundance(ion, 0.0f, true);
                } else {
                    extractedIonSignal.setAbundance(ion, newAbundance, true);
                }
            }
        }
    }

    /**
	 * Calculates a noise coefficient.
	 * 
	 * @param extractedIonSignal
	 * @param noiseSignal
	 * @return float
	 */
    private static float calculateCoefficient(IExtractedIonSignal extractedIonSignal, IExtractedIonSignal noiseSignal, int numberOfUsedIonsForCoefficient) {
        int startIon = noiseSignal.getStartIon();
        int stopIon = noiseSignal.getStopIon();
        float abundanceNoise;
        float abundanceScan;
        float coefficient = 0.0f;
        List<IonNoise> entries = new ArrayList<IonNoise>();
        for (int ion = startIon; ion <= stopIon; ion++) {
            entries.add(new IonNoise(ion, noiseSignal.getAbundance(ion)));
        }
        Collections.sort(entries);
        List<Float> coefficients = new ArrayList<Float>();
        int counter = 0;
        exitfor: for (IonNoise entry : entries) {
            abundanceNoise = noiseSignal.getAbundance(entry.getIon());
            if (abundanceNoise > 0.0f) {
                abundanceScan = extractedIonSignal.getAbundance(entry.getIon());
                if (abundanceScan > 0.0f) {
                    coefficient = abundanceScan / abundanceNoise;
                    coefficients.add(coefficient);
                    counter++;
                    if (counter > numberOfUsedIonsForCoefficient) {
                        break exitfor;
                    }
                }
            }
        }
        int size = coefficients.size();
        float[] values = new float[size];
        for (int i = 0; i < size; i++) {
            values[i] = coefficients.get(i);
        }
        return Calculations.getMean(values);
    }

    /**
	 * Writes the results back to the chromatogram.
	 * 
	 * @param chromatogram
	 * @param extractedIonSignals
	 * @param startScan
	 * @param stopScan
	 * @param monitor
	 */
    private static void writeExtractedIonSignalsBackToChromatogram(IChromatogram chromatogram, IExtractedIonSignals extractedIonSignals, IProgressMonitor monitor) {
        int startScan = extractedIonSignals.getStartScan();
        int stopScan = extractedIonSignals.getStopScan();
        IExtractedIonSignal extractedIonSignal;
        ISupplierMassSpectrum supplierMassSpectrum;
        for (int scan = startScan; scan <= stopScan; scan++) {
            try {
                extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
                supplierMassSpectrum = chromatogram.getScan(scan);
                replaceIons(extractedIonSignal, supplierMassSpectrum);
            } catch (NoExtractedIonSignalStoredException e) {
                logger.warn(e);
            }
        }
    }

    /**
	 * Replaces all ions in the supplier mass spectrum by the mass
	 * fragments stored in the extracted ion signal.
	 * 
	 * @param extractedIonSignal
	 * @param supplierMassSpectrum
	 */
    private static void replaceIons(IExtractedIonSignal extractedIonSignal, ISupplierMassSpectrum supplierMassSpectrum) {
        int startIon = extractedIonSignal.getStartIon();
        int stopIon = extractedIonSignal.getStopIon();
        float abundance;
        supplierMassSpectrum.removeAllIons();
        IIon defaultIon;
        for (int ion = startIon; ion <= stopIon; ion++) {
            abundance = extractedIonSignal.getAbundance(ion);
            if (abundance > 0.0f) {
                try {
                    defaultIon = new DefaultIon(ion, abundance);
                    supplierMassSpectrum.addIon(defaultIon);
                } catch (AbundanceLimitExceededException e) {
                    logger.warn(e);
                } catch (IonLimitExceededException e) {
                    logger.warn(e);
                }
            }
        }
    }

    /**
	 * Subtracts the noise mass spectra from the calculated segments.
	 * 
	 * @return
	 */
    private static List<ICombinedMassSpectrum> subtractNoiseMassSpectraFromSegments(IExtractedIonSignals extractedIonSignals, List<INoiseSegment> noiseSegments, IMarkedIons ionsToPreserve, int numberOfUsedIonsForCoefficient, IProgressMonitor monitor) {
        List<ICombinedMassSpectrum> noiseMassSpectra = new ArrayList<ICombinedMassSpectrum>();
        Calculator calculator = new Calculator();
        int segments = noiseSegments.size();
        int startScan;
        int stopScan;
        List<INoiseMassSpectrum> segmentNoiseMassSpectra;
        INoiseSegment currentNoiseSegment;
        INoiseSegment followingNoiseSegment;
        boolean firstRun = true;
        for (int segment = 0; segment < segments; segment++) {
            segmentNoiseMassSpectra = new ArrayList<INoiseMassSpectrum>();
            currentNoiseSegment = noiseSegments.get(segment);
            segmentNoiseMassSpectra.add(currentNoiseSegment.getNoiseMassSpectrum());
            if (segment == 0 && firstRun == true) {
                startScan = extractedIonSignals.getStartScan();
                stopScan = currentNoiseSegment.getAnalysisSegment().getStopScan() - calculateTailingScans(currentNoiseSegment);
                firstRun = false;
                segment--;
            } else if (segment == segments - 1) {
                startScan = currentNoiseSegment.getAnalysisSegment().getStartScan() + calculateLeadingScans(currentNoiseSegment);
                stopScan = extractedIonSignals.getStopScan();
            } else {
                followingNoiseSegment = noiseSegments.get(segment + 1);
                startScan = currentNoiseSegment.getAnalysisSegment().getStartScan() + calculateLeadingScans(currentNoiseSegment);
                stopScan = followingNoiseSegment.getAnalysisSegment().getStopScan() - calculateTailingScans(followingNoiseSegment);
                segmentNoiseMassSpectra.add(followingNoiseSegment.getNoiseMassSpectrum());
            }
            INoiseMassSpectrum noiseMassSpectrum = calculator.getNoiseMassSpectrum(segmentNoiseMassSpectra, ionsToPreserve, monitor);
            noiseMassSpectrum.setStartScan(startScan);
            noiseMassSpectrum.setStopScan(stopScan);
            noiseMassSpectrum.setStartRetentionTime(extractedIonSignals.getChromatogram().getScan(startScan).getRetentionTime());
            noiseMassSpectrum.setStopRetentionTime(extractedIonSignals.getChromatogram().getScan(stopScan).getRetentionTime());
            noiseMassSpectra.add(noiseMassSpectrum);
            monitor.subTask("Substract the noise mass spectrum from the scans: " + startScan + " - " + stopScan);
            subtractNoiseMassSpectrumFromScanRange(extractedIonSignals, noiseMassSpectrum, startScan, stopScan, numberOfUsedIonsForCoefficient, monitor);
        }
        return noiseMassSpectra;
    }

    /**
	 * If the segment has e.g. a width of 13 scans, the method will return 6. If
	 * the segment has e.g. a width of 14 scans, it will return 7.
	 * 
	 * @param noiseSegment
	 * @return int
	 */
    private static int calculateLeadingScans(INoiseSegment noiseSegment) {
        int width = noiseSegment.getAnalysisSegment().getSegmentWidth();
        int result = 0;
        if (width > 0) {
            result = width / 2;
        }
        return result;
    }

    /**
	 * If the segment has e.g. a width of 13 scans, the method will return 7. If
	 * the segment has e.g. a width of 14 scans, it will return 7.
	 * 
	 * @param noiseSegment
	 * @return int
	 */
    private static int calculateTailingScans(INoiseSegment noiseSegment) {
        int width = noiseSegment.getAnalysisSegment().getSegmentWidth();
        int result = 0;
        if (width > 0) {
            if (width % 2 == 0) {
                result = width / 2;
            } else {
                result = (width / 2) + 1;
            }
        }
        return result;
    }
}
