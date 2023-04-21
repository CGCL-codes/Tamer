package org.expasy.jpl.dev.spectralsearch.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.commons.ms.peak.JPLCharge;
import org.expasy.jpl.experimental.ms.peak.JPLExpMSPeakLC;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;
import org.expasy.jpl.utils.JPLIndexRange;

/**
 * @author eahrne
 *
 * Creates a new JPLAnnotatedFragmentation spectrum based on the optimal alignment
 *  between an experimental spectrum (es) and a library spectrum (ls), allowing for peak shifts 
 *  in accordance with the mass of a potential modification (precursor mass diff between the 2 spectra)
 */
public class JPLAlignSpectraTemp {

    public static JPLFragmentationSpectrum createModifiedSpectrum(JPLFragmentationSpectrum es, JPLFragmentationSpectrum ls, double fragmentMzTol) {
        Log log = LogFactory.getLog(JPLAlignSpectraTemp.class);
        JPLExpMSPeakLC lsPrecursor = ls.getPrecursor();
        JPLExpMSPeakLC esPrecursor = es.getPrecursor();
        double lsPrecursorMz = lsPrecursor.getMz();
        double esPrecursorMz = esPrecursor.getMz();
        int charge = JPLCharge.state2chargelist(lsPrecursor.getChargeState())[0];
        double lsPrecursorNeutralMass = lsPrecursorMz * charge - charge;
        double lsMaxIntensity = ls.getIntensityAt(ls.getMaxIntensityIndex());
        double esMaxIntensity = es.getIntensityAt(es.getMaxIntensityIndex());
        double modMass = (esPrecursorMz - lsPrecursorMz) * charge;
        int lsPeakNb = ls.getNbPeak();
        HashMap<Double, Double> modifiedSpectrumHash = new HashMap<Double, Double>();
        for (int i = 0; i < lsPeakNb; i++) {
            double lsPeakMz = ls.getMzAt(i);
            double bestMz = lsPeakMz;
            double[] potentialShifts;
            int nbConsideredShifts = (int) (lsPeakMz / (lsPrecursorNeutralMass / charge));
            potentialShifts = new double[charge + 1 - nbConsideredShifts];
            potentialShifts[0] = 0;
            for (int j = 1; j < potentialShifts.length; j++) {
                potentialShifts[j] = modMass / j;
            }
            double lsPeakInt = ls.getIntensityAt(i) / lsMaxIntensity;
            double bestIntDiff = 999999999;
            for (int j = 0; j < potentialShifts.length; j++) {
                double shift = potentialShifts[j];
                double mzLowerBound = lsPeakMz + shift - fragmentMzTol;
                double mzUpperBound = lsPeakMz + shift + fragmentMzTol;
                JPLIndexRange pklIndices = es.getIndexRange(mzLowerBound, mzUpperBound);
                if (!pklIndices.isEmpty()) {
                    for (int index = pklIndices.getMin(); index <= pklIndices.getMax(); index++) {
                        double esPeakInt = es.getIntensityAt(index) / esMaxIntensity;
                        double intDiff = Math.abs(lsPeakInt - esPeakInt);
                        if (intDiff < bestIntDiff) {
                            bestIntDiff = intDiff;
                            bestMz = es.getMzAt(index);
                        }
                    }
                }
            }
            if (modifiedSpectrumHash.containsKey(bestMz)) {
                modifiedSpectrumHash.put(bestMz, modifiedSpectrumHash.get(bestMz) + lsPeakInt);
            } else {
                modifiedSpectrumHash.put(bestMz, lsPeakInt);
            }
        }
        int lsModifPeakNb = modifiedSpectrumHash.keySet().size();
        double[] modifiedIntensities = new double[lsModifPeakNb];
        double[] modifiedMzs = new double[lsModifPeakNb];
        ArrayList<Double> lsModifMzList = new ArrayList<Double>(modifiedSpectrumHash.keySet());
        Collections.sort(lsModifMzList);
        int k = 0;
        for (Iterator<Double> iterator = lsModifMzList.iterator(); iterator.hasNext(); ) {
            double mz = iterator.next();
            modifiedMzs[k] = mz;
            modifiedIntensities[k] = modifiedSpectrumHash.get(mz);
            k++;
        }
        JPLFragmentationSpectrum modifiedSpectrum = new JPLFragmentationSpectrum(modifiedMzs, modifiedIntensities);
        modifiedSpectrum.setPrecursor(lsPrecursor);
        modifiedSpectrum.setTitle(ls.getTitle() + "modified");
        return modifiedSpectrum;
    }
}
