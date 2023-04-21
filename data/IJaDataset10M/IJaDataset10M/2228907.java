package com.chromamorph.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import com.chromamorph.notes.Note;
import com.chromamorph.notes.Notes;

/**
 * Based on HarmonicAnalysis02, but allowing for dominant seventh chords.
 * 
 * @author David Meredith
 *
  *
 */
public class HarmonicAnalysis04 {

    public TreeSet<PCHistogramSegment> pcHistogramSegmentation = null;

    public ArrayList<Long> barSegmentation = null;

    public TreeSet<PCHistogramSegment> triadSeventhSegmentation = null;

    public HarmonicAnalysis04(Notes notes) {
        Long endTimePoint = notes.getMaxTimePoint();
        barSegmentation = new ArrayList<Long>();
        barSegmentation.add(0l);
        for (Long timePoint = 1l; timePoint < endTimePoint; timePoint++) {
            if (notes.getMetricLevel(timePoint).equals(1)) barSegmentation.add(timePoint);
        }
        barSegmentation.add(endTimePoint);
        pcHistogramSegmentation = new TreeSet<PCHistogramSegment>();
        int i = 0;
        Long currentSegmentOnset = barSegmentation.get(i);
        Long currentSegmentOffset = barSegmentation.get(i + 1);
        PCHistogram currentPCHistogram = new PCHistogram();
        for (Note note : notes.getNotes()) {
            if (note.getOnset().compareTo(currentSegmentOffset) >= 0) {
                pcHistogramSegmentation.add(new PCHistogramSegment(currentSegmentOnset, currentSegmentOffset, currentPCHistogram));
                currentPCHistogram = new PCHistogram();
                i++;
                currentSegmentOnset = barSegmentation.get(i);
                currentSegmentOffset = barSegmentation.get(i + 1);
            }
            if (note.getOnset().compareTo(currentSegmentOnset) >= 0 && note.getOnset().compareTo(currentSegmentOffset) < 0) {
                currentPCHistogram.addToPitchClassFreq(note.getPitch().getPitchClass(), 1.0 / note.getMetricLevel());
            }
        }
        pcHistogramSegmentation.add(new PCHistogramSegment(currentSegmentOnset, endTimePoint, currentPCHistogram));
        PCHistogram[] chords = { new PCHistogram(new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0 }), new PCHistogram(new double[] { 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0 }), new PCHistogram(new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0 }) };
        triadSeventhSegmentation = new TreeSet<PCHistogramSegment>();
        for (PCHistogramSegment s : pcHistogramSegmentation) {
            PCHistogram bestFitTriad = chords[0];
            PCHistogram triad = null;
            double bestFit = bestFitTriad.multiply(s.getPCHistogram()).sum();
            double fit = 0.0;
            for (int j = 0; j < 12; j++) {
                for (PCHistogram chord : chords) {
                    triad = chord.transpose(j);
                    fit = triad.multiply(s.getPCHistogram()).sum();
                    if (fit > bestFit) {
                        bestFit = fit;
                        bestFitTriad = triad;
                    }
                }
            }
            triadSeventhSegmentation.add(new PCHistogramSegment(s.getOnset(), s.getOffset(), bestFitTriad));
        }
        System.out.println("CHORDS");
        for (PCHistogram chord : chords) System.out.println(chord + "\t" + chord.getPCSet());
        System.out.println("\nANALYSIS");
        for (PCHistogramSegment analysisSegment : triadSeventhSegmentation) System.out.println(analysisSegment.getOnset() + "\t" + analysisSegment.getOffset() + "\t" + analysisSegment.getPCHistogram().getPCSet());
    }

    public static void main(String[] args) {
        try {
            new HarmonicAnalysis04(new Notes(new File("../notes/data/chopin-etude-op10-no1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
