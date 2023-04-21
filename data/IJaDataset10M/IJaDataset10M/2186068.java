package bop.filter;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
import bop.datamodel.*;
import bop.exception.BopException;

public class BlastFilter implements ResultFilterI {

    boolean debug = false;

    String debug_this = "";

    int max_coverage = 10;

    /**
   * Debugging may be turned on by 
   * setting -debug t in the preferences file
   */
    public void cleanUpResults(Analysis analysis, Hashtable options) throws BopException {
        Hashtable repeats = null;
        double coincidence = 100;
        double min_expect = 1.0;
        debug = false;
        if (options != null) {
            repeats = parseRepeats(options);
            coincidence = parseCoincidence(options);
            min_expect = parseMinExpect(options);
            max_coverage = parseMaxCover(options);
            parseDebug(options);
        }
        SequenceI seq = analysis.getSequence();
        String seq_name = (seq != null) ? seq.getID() : null;
        Vector hits = analysis.getResults();
        int hit_index = 0;
        while (hit_index < hits.size()) {
            ResultSet hit = (ResultSet) hits.elementAt(hit_index);
            ResultSet next_hit = ((hit_index + 1 < hits.size()) ? (ResultSet) hits.elementAt(hit_index + 1) : null);
            if (!debug_this.equals("")) {
                SequenceI align_seq = hit.getAlignment().getSequence();
                debug = (align_seq.getName().indexOf(debug_this) >= 0 || align_seq.getID().indexOf(debug_this) >= 0);
            }
            if (hit.getType().indexOf("blast") >= 0) {
                if (seq_name != null && seq_name.equals(hit.getAlignName())) {
                    removeHit(hit, analysis, "hit is to itself");
                } else if (hit.getExpect() > min_expect) {
                    removeHit(hit, analysis, hit.getExpect() + " > " + min_expect);
                } else {
                    cleanUpCoincidents(hit, coincidence);
                    Vector ordered_hits = splitHit(hit, hits, analysis, options);
                    for (int j = 0; j < ordered_hits.size(); j++) {
                        ResultSet sub_hit = (ResultSet) ordered_hits.elementAt(j);
                        if (lacksContent(sub_hit, repeats)) {
                            removeHit(sub_hit, analysis, "hit lacks content");
                        }
                    }
                }
            }
            if (next_hit != null) hit_index = hits.indexOf(next_hit); else hit_index = hits.size();
        }
        Vector regions = sortRegions(analysis, hits);
        cleanUpShadows(regions, analysis);
        if (max_coverage > 0) cleanUpCoverage(regions, analysis, max_coverage);
        return;
    }

    /** 
   * If this is a nucleic acid sequence (actually this would
   * work for a amino acid sequence as well) BOP uses Huffman
   * encoding to look for low information content. If the string
   * compresses too easily (too easily being a threshhold set
   * in the preferences file). The word size is also set in the
   * preferences file. A setting of -blast_repeat2 15 indicates
   * a word size of two (dinucleotide repeat) and compression cutoff of
   * less than 15 bits of information
   */
    private boolean lacksContent(ResultSet hit, Hashtable repeats) {
        ResultSpan span;
        String key, value;
        int wordsize;
        double cutoff;
        double length;
        if (repeats == null) return false;
        boolean no_info = true;
        Enumeration e = repeats.keys();
        while (no_info && e.hasMoreElements()) {
            key = (String) e.nextElement();
            value = (String) repeats.get(key);
            wordsize = Integer.parseInt(key);
            cutoff = (Double.valueOf(value)).doubleValue();
            int i = 0;
            while (no_info && i < (hit.getSpans()).size()) {
                span = (ResultSpan) (hit.getSpans()).elementAt(i);
                if (span.getQueryResidues().length() == 0) {
                    System.err.println("ERROR!! " + " no sequence in span " + i + 1);
                } else {
                    length = Compress.compress(span.getQueryResidues(), wordsize);
                    no_info &= length <= cutoff;
                }
                i++;
            }
        }
        if (no_info && debug) {
            System.out.println("Removing low complexity hit " + hit);
        }
        return no_info;
    }

    private Vector splitHit(ResultSet hit, Vector hits, Analysis analysis, Hashtable options) throws BopException {
        Vector split_hits = new Vector();
        analysis.removeResult(hit);
        Vector spans = hit.getSpans();
        while (hit.getSpans().size() > 0) {
            ResultSpan span = (ResultSpan) hit.getSpans().elementAt(0);
            ResultSet bin_hit = getClosestOnGenomic(span, split_hits);
            if (bin_hit == null) {
                bin_hit = makeNewHit(hit, span);
                split_hits.addElement(bin_hit);
                if (debug) System.out.println("Nothing close to " + span.getStart() + "-" + span.getEnd());
            } else {
                if (debug) System.out.println("Closest hit to " + span.getStart() + "-" + span.getEnd() + " is " + bin_hit.getStart() + "-" + bin_hit.getEnd());
                if (isSubjSequential(span.getAlignment(), bin_hit.getSpans())) {
                    if (debug) System.out.println("Span is sequential " + span.getStart() + "-" + span.getEnd() + " follows " + bin_hit.getStart() + "-" + bin_hit.getEnd());
                    addToHit(span, bin_hit);
                } else {
                    if (debug) System.out.println("Span is not sequential " + span.getStart() + "-" + span.getEnd() + " making new hit");
                    bin_hit = makeNewHit(hit, span);
                    split_hits.addElement(bin_hit);
                }
            }
            hit.removeSpan(span);
        }
        if (debug) System.out.println("Checking " + split_hits.size() + " split hits");
        for (int i = 0; i < split_hits.size(); ) {
            ResultSet new_hit = (ResultSet) split_hits.elementAt(i);
            if (new_hit.getScore() <= 100) {
                split_hits.removeElement(new_hit);
                if (debug) {
                    System.out.println("Removed synthetic hit (" + new_hit.getSpans().size() + ") because score " + new_hit.getScore() + " is too low. At " + new_hit.getStart() + "-" + new_hit.getEnd());
                }
            } else {
                i++;
            }
        }
        if (debug) System.out.println("Cleaning out twilight on " + split_hits.size() + " split hits");
        for (int i = 0; i < split_hits.size(); i++) {
            ResultSet first_hit = (ResultSet) split_hits.elementAt(i);
            if (debug) {
                System.out.println("Checking twilight spans (" + first_hit.getSpans().size() + ") on hit " + first_hit.getScore() + ". At " + first_hit.getStart() + "-" + first_hit.getEnd());
            }
            cleanUpTwilight(analysis, first_hit, options);
        }
        if (debug) System.out.println("Check merges on " + split_hits.size() + " split hits");
        for (int i = 0; i < split_hits.size() - 1; ) {
            ResultSet first_hit = (ResultSet) split_hits.elementAt(i);
            ResultSet second_hit = (ResultSet) split_hits.elementAt(i + 1);
            ResultSpan span = (ResultSpan) second_hit.getSpans().elementAt(0);
            if (isSubjSequential(span.getAlignment(), first_hit.getSpans())) {
                split_hits.removeElement(second_hit);
                if (debug) System.out.println("Re-Merging spans (" + first_hit.getSpans().size() + ") on hit " + first_hit.getScore() + ". At " + first_hit.getStart() + "-" + first_hit.getEnd());
                while (second_hit.getSpans().size() > 0) {
                    span = (ResultSpan) second_hit.getSpans().elementAt(0);
                    second_hit.removeSpan(span);
                    addToHit(span, first_hit);
                }
            } else {
                i++;
            }
        }
        if (debug) System.out.println("Left with " + split_hits.size() + " split hits");
        for (int i = 0; i < split_hits.size(); i++) {
            ResultSet new_hit = (ResultSet) split_hits.elementAt(i);
            analysis.addResult(new_hit);
        }
        return split_hits;
    }

    private void removeWeakerSpan(ResultSet hit, ResultSpan span1, ResultSpan span2, String caller_id) {
        ResultSpan weaker;
        if (SecondIsWeaker(span1, span2)) {
            weaker = span2;
        } else {
            if (SecondIsWeaker(span2, span1)) {
                weaker = span1;
            } else {
                weaker = span2;
            }
        }
        hit.removeSpan(weaker);
    }

    private ResultSet getClosestOnGenomic(ResultSpan span, Vector split_hits) {
        ResultSet bin_hit = null;
        int max_distance = 99999999;
        for (int i = 0; i < split_hits.size(); i++) {
            ResultSet check_hit = (ResultSet) split_hits.elementAt(i);
            if ((check_hit.isForward() && check_hit.getEnd() < span.getStart() && (span.getStart() - check_hit.getEnd()) < max_distance) || (!check_hit.isForward() && check_hit.getEnd() > span.getStart() && (check_hit.getEnd() - span.getStart()) < max_distance)) {
                bin_hit = check_hit;
            }
        }
        return bin_hit;
    }

    private boolean isSubjSequential(SpanI second_span, Vector spans) {
        boolean is_sequential = true;
        SpanI first_span = ((ResultSpan) spans.elementAt(spans.size() - 1)).getAlignment();
        int spacing;
        if (first_span.isForward()) spacing = second_span.getStart() - first_span.getEnd(); else spacing = first_span.getEnd() - second_span.getStart();
        if (spacing < 0) {
            int spans_sum = first_span.length() + second_span.length();
            double percent_overlap = (Math.abs(spacing) * 100) / spans_sum;
            if (debug) {
                System.out.println("Distance between spans " + " is " + spacing + " first span ends at " + first_span.getEnd() + " second span starts at " + second_span.getStart() + " this is " + percent_overlap + "% of " + spans_sum + " bases");
            }
            is_sequential = (percent_overlap <= 10);
        }
        return is_sequential;
    }

    private ResultSet makeNewHit(ResultSet old_hit, ResultSpan span) {
        String value;
        ResultSet new_hit = new ResultSet();
        new_hit.setSequence(old_hit.getSequence());
        new_hit.setType(old_hit.getType());
        Span align_span = new Span();
        align_span.setSequence(old_hit.getAlignment().getSequence());
        new_hit.setAlignment(align_span);
        addToHit(span, new_hit);
        return new_hit;
    }

    private void addToHit(ResultSpan span, ResultSet hit) {
        hit.addSpan(span);
        hit.setScore(-1);
        setHitScore(hit);
    }

    private void setHitScore(ResultSet hit) {
        int score;
        for (int i = 0; i < (hit.getSpans()).size(); i++) {
            ResultSpan span = (ResultSpan) (hit.getSpans()).elementAt(i);
            if (span.getScore() > hit.getScore() || hit.getScore() == -1 || (span.getScore() == hit.getScore() && span.getExpectValue() < hit.getExpect())) {
                hit.setScore(span.getScore());
                hit.setExpect(span.getExpect());
                hit.setProb(span.getProb());
            }
        }
    }

    private boolean SecondIsWeaker(ResultSpan span1, ResultSpan span2) {
        boolean span2weaker = false;
        double identity1, identity2;
        if (span1.getExpectValue() < span2.getExpectValue()) {
            span2weaker = true;
        } else {
            if (span1.getExpectValue() == span2.getExpectValue() && span1.getScore() > span2.getScore()) {
                span2weaker = true;
            } else {
                identity1 = span1.identity();
                identity2 = span2.identity();
                if (span1.getScore() == span2.getScore() && span1.getExpectValue() == span2.getExpectValue() && identity1 > identity2) {
                    span2weaker = true;
                } else {
                    if (span1.getScore() == span2.getScore() && span1.getExpectValue() == span2.getExpectValue() && identity1 == identity2 && span1.length() > span2.length()) {
                        span2weaker = true;
                    }
                }
            }
        }
        return span2weaker;
    }

    private void cleanUpCoincidents(ResultSet hit, double coincidence) {
        int i;
        ResultSpan span1, span2;
        boolean cleanup = true;
        while (cleanup) {
            cleanup = false;
            i = 0;
            while (((i + 1) < (hit.getSpans()).size()) && !cleanup) {
                span1 = (ResultSpan) (hit.getSpans()).elementAt(i);
                span2 = (ResultSpan) (hit.getSpans()).elementAt(i + 1);
                cleanup = spansOverlap(hit, span1, span2, coincidence);
                if (cleanup) {
                    removeWeakerSpan(hit, span1, span2, "coincidence");
                    i = 0;
                } else {
                    i++;
                }
            }
        }
    }

    private void cleanUpTwilight(Analysis analysis, ResultSet hit, Hashtable options) throws BopException {
        TwilightFilter twilight = new TwilightFilter();
        twilight.setDebug(debug);
        String program = analysis.getProgram().toLowerCase();
        if (program.indexOf("blastn") >= 0 || program.indexOf("tblastx") >= 0) {
            twilight.setSeqNucleic(true);
            twilight.setRefNucleic(true);
        } else if (program.indexOf("blastx") >= 0) {
            twilight.setSeqNucleic(true);
            twilight.setRefNucleic(false);
        } else if (program.indexOf("blastp") >= 0) {
            twilight.setSeqNucleic(false);
            twilight.setRefNucleic(false);
        } else {
        }
        twilight.cleanUpTwilightZone(analysis, hit, options);
    }

    private boolean spansOverlap(ResultSet hit, ResultSpan span1, ResultSpan span2, double coincidence) {
        boolean overlaps = span1.contains(span2) || span2.contains(span1);
        if (!overlaps) {
            if (span1.overlaps(span2) && coincidence < 100) {
                int extent_start, extent_end;
                int connect_start, connect_end;
                if (span1.getLowest() < span2.getLowest()) {
                    extent_start = span1.getLowest();
                    connect_start = span2.getLowest();
                } else {
                    extent_start = span2.getLowest();
                    connect_start = span1.getLowest();
                }
                if (span1.getHighest() > span2.getHighest()) {
                    extent_end = span1.getHighest();
                    connect_end = span2.getHighest();
                } else {
                    extent_end = span2.getHighest();
                    connect_end = span1.getHighest();
                }
                int overlap = connect_end - connect_start;
                if (overlap > 0) {
                    double common = (overlap * 100) / (extent_end - extent_start);
                    overlaps = common >= coincidence;
                    if (overlaps && debug) {
                        System.out.println("Removing coincident span in hit " + connect_end + "-" + connect_start + " / " + extent_end + "-" + extent_start + " = " + common + "%");
                    }
                }
            }
        } else if (debug) {
            System.out.println("Spans are contained within one another at " + span1.getLowest() + "-" + span1.getHighest() + " and " + span2.getLowest() + "-" + span2.getHighest());
        }
        return overlaps;
    }

    private Vector sortRegions(Analysis analysis, Vector hits) {
        Vector regions = new Vector();
        ResultSet hit, region_hit, check_hit;
        boolean added;
        int j, k;
        for (int hit_index = 0; hit_index < hits.size(); hit_index++) {
            Vector region;
            hit = (ResultSet) hits.elementAt(hit_index);
            boolean found_region = false;
            boolean skip = (hit.getType()).indexOf("blast") < 0;
            if (!debug_this.equals("")) {
                SequenceI align_seq = hit.getAlignment().getSequence();
                debug = (align_seq.getName().indexOf(debug_this) >= 0 || align_seq.getID().indexOf(debug_this) >= 0);
            }
            int i = 0;
            while (!skip && !found_region && i < regions.size()) {
                region = (Vector) regions.elementAt(i);
                j = 0;
                while (!found_region && j < region.size()) {
                    region_hit = (ResultSet) region.elementAt(j);
                    found_region = regionsOverlap(hit, region_hit);
                    if (found_region) {
                        k = 0;
                        added = false;
                        while (!added && k < region.size()) {
                            check_hit = (ResultSet) region.elementAt(k);
                            if (hit.getScore() > check_hit.getScore() || (hit.getScore() == check_hit.getScore() && hit.getExpect() < check_hit.getExpect())) {
                                region.insertElementAt(hit, k);
                                added = true;
                            } else {
                                k++;
                            }
                        }
                        if (!added) region.addElement(hit);
                    }
                    j++;
                }
                i++;
            }
            if (!skip && !found_region) {
                region = new Vector();
                region.addElement(hit);
                regions.addElement(region);
            }
        }
        return regions;
    }

    private void cleanUpCoverage(Vector regions, Analysis analysis, int max) {
        Vector region;
        ResultSet hit;
        int del;
        for (int i = 0; i < regions.size(); i++) {
            region = (Vector) regions.elementAt(i);
            while (region.size() > max) {
                hit = (ResultSet) region.elementAt(max);
                if (!debug_this.equals("")) {
                    SequenceI align_seq = hit.getAlignment().getSequence();
                    debug = (align_seq.getName().indexOf(debug_this) >= 0 || align_seq.getID().indexOf(debug_this) >= 0);
                }
                region.removeElementAt(max);
                removeHit(hit, analysis, " have " + region.size() + " hits at " + hit.getStart() + "-" + hit.getEnd());
            }
        }
    }

    private void removeHit(ResultSet hit, Analysis analysis, String why) {
        if (debug) System.out.println("Removing hit " + hit.getAlignName() + " because " + why);
        hit.getSpans().removeAllElements();
        analysis.removeResult(hit);
    }

    private void cleanUpShadows(Vector regions, Analysis analysis) {
        for (int i = 0; i < regions.size(); i++) {
            Vector region = (Vector) regions.elementAt(i);
            for (int j = 0; j < region.size(); j++) {
                ResultSet hit = (ResultSet) region.elementAt(j);
                if (!debug_this.equals("")) {
                    SequenceI align_seq = hit.getAlignment().getSequence();
                    debug = (align_seq.getName().indexOf(debug_this) >= 0 || align_seq.getID().indexOf(debug_this) >= 0);
                }
                boolean shadow = false;
                int k = j + 1;
                String hit_name = hit.getAlignName();
                ResultSet close_hit;
                while (!shadow && k < region.size()) {
                    close_hit = (ResultSet) region.elementAt(k);
                    if (!debug_this.equals("")) {
                        SequenceI align_seq = close_hit.getAlignment().getSequence();
                        debug |= (align_seq.getName().indexOf(debug_this) >= 0 || align_seq.getID().indexOf(debug_this) >= 0);
                    }
                    if (hit_name.equals(close_hit.getAlignName()) && (hit.isForward() != close_hit.isForward())) {
                        shadow = true;
                        region.removeElementAt(k);
                        removeHit(close_hit, analysis, "its a shadow");
                    } else {
                        k++;
                    }
                }
            }
        }
    }

    private boolean regionsOverlap(ResultSet hit, ResultSet region_hit) {
        ResultSpan region_span, span;
        int i = 0;
        int j;
        boolean found_overlap = false;
        while (!found_overlap && i < (hit.getSpans()).size()) {
            span = (ResultSpan) (hit.getSpans()).elementAt(i);
            j = 0;
            while (!found_overlap && j < (region_hit.getSpans()).size()) {
                region_span = (ResultSpan) (region_hit.getSpans()).elementAt(j);
                found_overlap = span.overlaps(region_span);
                j++;
            }
            i++;
        }
        return found_overlap;
    }

    private Hashtable parseRepeats(Hashtable options) {
        String option;
        String wordsize = "";
        String cutoff = "";
        Hashtable repeats = null;
        Enumeration e = options.keys();
        while (e.hasMoreElements()) {
            option = (String) e.nextElement();
            if (option.startsWith("-blast_repeat")) {
                try {
                    wordsize = option.substring("-blast_repeat".length());
                    int test = Integer.parseInt(wordsize);
                } catch (Exception ex) {
                    System.err.println("Couldn't parse wordsize from " + option);
                }
                cutoff = (String) options.get(option);
                try {
                    double test = (Double.valueOf(cutoff)).doubleValue();
                    if (repeats == null) {
                        repeats = new Hashtable();
                    }
                    repeats.put(wordsize, cutoff);
                    System.err.println("Removing repeats of length " + wordsize + " where compressed length < " + cutoff);
                } catch (Exception ex) {
                    System.err.println("Couldn't parse low complexity threshhold from " + cutoff);
                }
            }
        }
        return repeats;
    }

    private double parseCoincidence(Hashtable options) {
        String value;
        double coincidence = 100;
        value = (String) options.get("-blast_coincidence");
        if (value != null) {
            try {
                coincidence = (Double.valueOf(value)).doubleValue();
                if (coincidence <= 0 || coincidence > 100) {
                    System.out.println("Using default of 100 not " + coincidence);
                    coincidence = 100;
                }
                System.out.println("Removing spans with overlap of " + coincidence + "% or more");
            } catch (Exception ex) {
                System.err.println("Couldn't parse coincidence from " + value);
            }
        }
        return coincidence;
    }

    private double parseMinExpect(Hashtable options) {
        String value;
        double min_expect = 1.0;
        value = (String) options.get("-min_expect");
        if (value != null) {
            try {
                min_expect = (Double.valueOf(value)).doubleValue();
                System.err.println("Removing spans with expectation of " + min_expect + " or more");
            } catch (Exception ex) {
                System.err.println("Couldn't parse expect from " + value);
            }
        }
        return min_expect;
    }

    private int parseMaxCover(Hashtable options) {
        String value;
        int max = max_coverage;
        value = (String) options.get("-max_coverage");
        if (value != null) {
            try {
                max = Integer.parseInt(value);
                System.err.println("Removing hits with that cover a region > " + max + " layers deep");
            } catch (Exception ex) {
                System.err.println("Couldn't parse max_coverage from " + value);
            }
        }
        return max;
    }

    private void parseDebug(Hashtable options) {
        String value;
        value = (String) options.get("-debug");
        if (value != null) {
            if (value.equals("t")) debug = true; else if (value.equals("f")) debug = false; else {
                debug_this = value;
                System.out.println("Will debug hit to " + debug_this);
            }
        }
    }
}
