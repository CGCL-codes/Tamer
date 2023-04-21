package org.expasy.jpl.dev.spectralsearch.dataloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.expasy.jpl.bio.annotation.JPLMSnPeakListAnnotations;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationAnnotatedSpectrum;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;
import org.expasy.jpl.dev.spectralsearch.sequence.SptxtPeptide;
import org.expasy.jpl.dev.spectralsearch.spectrum.JPLLightBinnedAnnotatedSpectrum;
import org.expasy.jpl.dev.spectralsearch.spectrum.JPLLightBinnedSpectrum;

public class JPLLightSpectraCreation {

    public static List<JPLLightBinnedSpectrum> buildSortedLightFragmentationSpectrumList(List<JPLFragmentationSpectrum> spectrumList, double fragmentMzTolerance) {
        List<JPLLightBinnedSpectrum> lightSpectrumList = new ArrayList<JPLLightBinnedSpectrum>();
        for (Iterator<JPLFragmentationSpectrum> iterator = spectrumList.iterator(); iterator.hasNext(); ) {
            JPLFragmentationSpectrum jplFragmentationSpectrum = iterator.next();
            lightSpectrumList.add(buildLightBinnedSpectrum(jplFragmentationSpectrum, fragmentMzTolerance));
        }
        Collections.sort(lightSpectrumList);
        return lightSpectrumList;
    }

    public static List<JPLLightBinnedAnnotatedSpectrum> buildSortedLightFragmentationAnnotationSpectrumList(List<JPLFragmentationAnnotatedSpectrum> spectrumList, double fragmentMzTolerance) {
        List<JPLLightBinnedAnnotatedSpectrum> lightSpectrumList = new ArrayList<JPLLightBinnedAnnotatedSpectrum>();
        for (Iterator<JPLFragmentationAnnotatedSpectrum> iterator = spectrumList.iterator(); iterator.hasNext(); ) {
            JPLFragmentationAnnotatedSpectrum jplFragmentationAnnotatedSpectrum = iterator.next();
            lightSpectrumList.add(buildLightBinnedAnnotatedSpectrum(jplFragmentationAnnotatedSpectrum, fragmentMzTolerance));
        }
        Collections.sort(lightSpectrumList);
        return lightSpectrumList;
    }

    public static JPLLightBinnedSpectrum buildLightBinnedSpectrum(JPLFragmentationSpectrum spectrum, double fragmentMzTol) {
        HashMap<Integer, Double> binnedSpectrumHash = new HashMap<Integer, Double>();
        double mzs[] = spectrum.getMzs();
        double intensities[] = spectrum.getIntensities();
        for (int i = 0; i < mzs.length; i++) {
            double mz = mzs[i];
            double intensity = intensities[i];
            int mzBin = (int) (mz / fragmentMzTol);
            if (binnedSpectrumHash.containsKey(mzBin)) {
                double summedIntensity = intensity + binnedSpectrumHash.get(mzBin);
                binnedSpectrumHash.put(mzBin, summedIntensity);
            } else {
                binnedSpectrumHash.put(mzBin, intensity);
            }
        }
        ArrayList<Integer> bins = new ArrayList<Integer>(binnedSpectrumHash.keySet());
        Collections.sort(bins);
        int nbBins = bins.size();
        int[] mzBins = new int[nbBins];
        double[] binIntensities = new double[nbBins];
        int j = 0;
        for (Iterator<Integer> iterator = bins.iterator(); iterator.hasNext(); ) {
            int bin = iterator.next();
            mzBins[j] = bin;
            binIntensities[j] = binnedSpectrumHash.get(bin);
            j++;
        }
        JPLLightBinnedSpectrum lightBinnedSpectrum = new JPLLightBinnedSpectrum(mzBins, binIntensities, spectrum.getPrecursor(), spectrum.getTitle());
        return lightBinnedSpectrum;
    }

    public static JPLLightBinnedAnnotatedSpectrum buildLightBinnedAnnotatedSpectrum(JPLFragmentationAnnotatedSpectrum spectrum, double fragmentMzTol) {
        HashMap<Integer, ArrayList<Integer>> binnedSpectrumHash = new HashMap<Integer, ArrayList<Integer>>();
        double mzs[] = spectrum.getMzs();
        for (int i = 0; i < mzs.length; i++) {
            double mz = mzs[i];
            int mzBin = (int) (mz / fragmentMzTol);
            if (binnedSpectrumHash.containsKey(mzBin)) {
                ArrayList<Integer> indexes = binnedSpectrumHash.get(mzBin);
                indexes.add(i);
                binnedSpectrumHash.put(mzBin, indexes);
            } else {
                ArrayList<Integer> indexes = new ArrayList<Integer>();
                indexes.add(i);
                binnedSpectrumHash.put(mzBin, indexes);
            }
        }
        ArrayList<Integer> bins = new ArrayList<Integer>(binnedSpectrumHash.keySet());
        Collections.sort(bins);
        int nbBins = bins.size();
        int[] mzBins = new int[nbBins];
        double[] binIntensities = new double[nbBins];
        JPLMSnPeakListAnnotations annotations = new JPLMSnPeakListAnnotations(nbBins);
        int j = 0;
        for (Iterator<Integer> iterator = bins.iterator(); iterator.hasNext(); ) {
            int mzBin = iterator.next();
            mzBins[j] = mzBin;
            double intensity = 0;
            ArrayList<Integer> indexes = binnedSpectrumHash.get(mzBin);
            for (Iterator<Integer> iterator2 = indexes.iterator(); iterator2.hasNext(); ) {
                Integer index = iterator2.next();
                intensity += spectrum.getIntensityAt(index);
                annotations.addAnnotationsAtPeakIndex(spectrum.getAnnotationsAtPeak(index), j);
            }
            binIntensities[j] = intensity;
            j++;
        }
        String peptideString = spectrum.getPeptide();
        String peptide = peptideString;
        boolean decoy = false;
        Pattern decoyPattern = Pattern.compile("decoy_");
        Matcher peptideStringMatcher = decoyPattern.matcher(peptideString);
        if (peptideStringMatcher.find()) {
            peptide = peptideStringMatcher.replaceAll("");
            decoy = true;
        }
        peptide = SptxtPeptide.formatPeptideString(peptide);
        JPLLightBinnedAnnotatedSpectrum lightBinnedSpectrum = new JPLLightBinnedAnnotatedSpectrum(mzBins, binIntensities, annotations, peptide, spectrum.getPrecursor(), spectrum.getProteinTag(), decoy);
        return lightBinnedSpectrum;
    }
}
