package it.southdown.avana.coevolution;

import it.southdown.avana.alignment.*;
import it.southdown.avana.alignscan.Diversity;
import it.southdown.avana.alignscan.PositionData;
import it.southdown.avana.alignscan.SequenceCursor;
import it.southdown.avana.alignscan.ValueCounter;
import java.util.*;

/**
 * This is an alignment cursor that looks at the combined peptide values at pairs
 * of sites in two different alignments. It is used to study correlations between 
 * sites.
 * 
 * The functionality is analogous to that of AlignmentCursor, except in this case
 * have also a reference alignment (besides the test alignment being analyzed). 
 * Whenever we set the position in the reference alignment, the diversity is 
 * recomputed in the test alignment, combining the value seen at the current
 * position in the test alignment with those seen at the reference position in 
 * the reference alignment, in the sequence that is paired with the test sequence
 * (at the same position in the alignment). The resulting two values are combined 
 * to form a joint value which is then used for variant analysis.
 * 
 */
public class JointAlignmentCursor {

    private Alignment refAlign;

    private Alignment testAlign;

    private SequenceCursor[] refCursors;

    private SequenceCursor[] testCursors;

    private int refPosition = -1;

    private int testPosition = -1;

    private int cursorSampleSize;

    private JointAlignmentEntropy[] results;

    public JointAlignmentCursor(Alignment refAlign, Alignment testAlign, int cursorSampleSize) {
        int seqCount = refAlign.getSequenceCount();
        if (testAlign.getSequenceCount() != seqCount) {
            throw new java.lang.IllegalArgumentException("Cannot creata a joint alignment cursor: alignments are not of the same size");
        }
        this.refAlign = refAlign;
        this.testAlign = testAlign;
        this.refCursors = buildSequenceCursors(refAlign, cursorSampleSize);
        this.testCursors = buildSequenceCursors(testAlign, cursorSampleSize);
        this.cursorSampleSize = cursorSampleSize;
        setRefPosition(0);
        setTestPosition(0);
    }

    public int getSequenceCount() {
        return refCursors.length;
    }

    public int getSampleSize() {
        return cursorSampleSize;
    }

    public void setRefPosition(int refPosition) {
        if ((refPosition >= refAlign.getAlignmentLength()) || (refPosition < 0)) {
            throw new IllegalStateException("No such alignment position: " + refPosition);
        }
        if (refPosition == this.refPosition) {
            return;
        }
        this.refPosition = refPosition;
        for (int i = 0; i < refCursors.length; i++) {
            refCursors[i].setPosition(refPosition);
        }
        scanJointAlignments();
    }

    public void setTestPosition(int testPosition) {
        if ((testPosition >= testAlign.getAlignmentLength()) || (testPosition < 0)) {
            throw new IllegalStateException("No such alignment position: " + testPosition);
        }
        if (this.testPosition == testPosition) {
            return;
        }
        this.testPosition = testPosition;
        for (int i = 0; i < testCursors.length; i++) {
            testCursors[i].setPosition(testPosition);
        }
    }

    public JointAlignmentEntropy getAnalysisResult() {
        return results[testPosition];
    }

    /**
	 * Scans the alignment for the current sample size, and caches the 
	 * results for faster access.
	 */
    private void scanJointAlignments() {
        int savePosition = testPosition;
        int testAlignLength = testAlign.getAlignmentLength();
        results = new JointAlignmentEntropy[testAlignLength];
        for (int i = 0; i < testAlignLength; i++) {
            setTestPosition(i);
            results[i] = computeJointPositionData();
        }
        testPosition = savePosition;
    }

    private JointAlignmentEntropy computeJointPositionData() {
        int count = testAlign.getSequenceCount();
        HashMap<String, ValueCounter> refSamples = new HashMap<String, ValueCounter>();
        HashMap<String, ValueCounter> testSamples = new HashMap<String, ValueCounter>();
        HashMap<String, ValueCounter> jointSamples = new HashMap<String, ValueCounter>();
        int sampleSupport = 0;
        for (int i = 0; i < count; i++) {
            SequenceCursor refCursor = refCursors[i];
            SequenceCursor testCursor = testCursors[i];
            if (!testCursor.isValidSymbol() || !refCursor.isValidSymbol()) {
                continue;
            }
            String refSample = refCursor.getSample();
            String testSample = testCursor.getSample();
            String jointSample = testSample + '/' + refSample;
            incrementCount(refSamples, refSample);
            incrementCount(testSamples, testSample);
            incrementCount(jointSamples, jointSample);
            sampleSupport++;
        }
        PositionData refPosData = makePositionData(refAlign, refPosition, refSamples);
        PositionData testPosData = makePositionData(refAlign, testPosition, testSamples);
        PositionData jointPosData = makePositionData(refAlign, -1, jointSamples);
        return new JointAlignmentEntropy(refPosData, testPosData, jointPosData);
    }

    private PositionData makePositionData(Alignment alignment, int pos, HashMap<String, ValueCounter> table) {
        ValueCounter[] counters = new ValueCounter[table.size()];
        counters = table.values().toArray(counters);
        Arrays.sort(counters);
        Diversity diversity = null;
        System.err.println("ERROR - This functionality does not work.");
        System.err.println("JointAlignmentCursor.makePositionData() must be fixed.");
        return new PositionData(pos, alignment, cursorSampleSize, false, false, null, diversity);
    }

    /**
	 * Increments the counter for a given value in a table of ValueCounters.
	 * If it is the first occurrence of the value, it creates a new ValueCounter
	 * and inserts it into the table.
	 * 
	 * @param table the table of ValueCounters
	 * @param value the value whose count is to be increased
	 */
    private void incrementCount(HashMap<String, ValueCounter> table, String value) {
        ValueCounter sc = table.get(value);
        if (sc == null) {
            sc = new ValueCounter(value);
            table.put(value, sc);
        }
        sc.incrementCount();
    }

    private SequenceCursor[] buildSequenceCursors(Alignment alignment, int cursorSampleSize) {
        int count = alignment.getSequenceCount();
        SequenceCursor[] cursors = new SequenceCursor[count];
        for (int i = 0; i < count; i++) {
            cursors[i] = new SequenceCursor(alignment.getSequence(i), cursorSampleSize);
        }
        return cursors;
    }
}
