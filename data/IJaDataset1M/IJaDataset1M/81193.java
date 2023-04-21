package org.jcvi.glyph.nuc;

import java.util.ArrayList;
import java.util.List;
import org.jcvi.assembly.AssemblyUtil;
import org.jcvi.common.util.Range;

public abstract class AbstractEnocdedNucleotideGlyphs implements NucleotideEncodedGlyphs {

    @Override
    public int convertGappedValidRangeIndexToUngappedValidRangeIndex(int gappedValidRangeIndex) {
        if (isAGap(gappedValidRangeIndex)) {
            throw new IllegalArgumentException(gappedValidRangeIndex + " is a gap");
        }
        int numberOfGaps = computeNumberOfInclusiveGapsInGappedValidRangeUntil(gappedValidRangeIndex);
        return gappedValidRangeIndex - numberOfGaps;
    }

    @Override
    public Range convertGappedValidRangeToUngappedValidRange(Range gappedValidRange) {
        return Range.buildRange(convertGappedValidRangeIndexToUngappedValidRangeIndex(AssemblyUtil.getLeftFlankingNonGapIndex(this, (int) gappedValidRange.getStart())), convertGappedValidRangeIndexToUngappedValidRangeIndex(AssemblyUtil.getLeftFlankingNonGapIndex(this, (int) gappedValidRange.getEnd())));
    }

    @Override
    public Range convertUngappedValidRangeToGappedValidRange(Range ungappedValidRange) {
        return Range.buildRange(convertUngappedValidRangeIndexToGappedValidRangeIndex((int) ungappedValidRange.getStart()), convertUngappedValidRangeIndexToGappedValidRangeIndex((int) ungappedValidRange.getEnd()));
    }

    private boolean isAGap(int gappedValidRangeIndex) {
        return getGapIndexes().contains(Integer.valueOf(gappedValidRangeIndex));
    }

    @Override
    public long getUngappedLength() {
        return getLength() - getNumberOfGaps();
    }

    @Override
    public int computeNumberOfInclusiveGapsInGappedValidRangeUntil(int gappedValidRangeIndex) {
        int numberOfGaps = 0;
        for (Integer gapIndex : getGapIndexes()) {
            if (gapIndex.intValue() <= gappedValidRangeIndex) {
                numberOfGaps++;
            }
        }
        return numberOfGaps;
    }

    @Override
    public int computeNumberOfInclusiveGapsInUngappedValidRangeUntil(int ungappedValidRangeIndex) {
        int numberOfGaps = 0;
        for (Integer gapIndex : getGapIndexes()) {
            if (gapIndex.intValue() <= ungappedValidRangeIndex + numberOfGaps) {
                numberOfGaps++;
            }
        }
        return numberOfGaps;
    }

    @Override
    public int convertUngappedValidRangeIndexToGappedValidRangeIndex(int ungappedValidRangeIndex) {
        int numberOfGaps = computeNumberOfInclusiveGapsInUngappedValidRangeUntil(ungappedValidRangeIndex);
        return ungappedValidRangeIndex + numberOfGaps;
    }

    @Override
    public List<NucleotideGlyph> decode(Range range) {
        if (range == null) {
            return decode();
        }
        List<NucleotideGlyph> result = new ArrayList<NucleotideGlyph>();
        for (long index : range) {
            result.add(get((int) index));
        }
        return result;
    }

    @Override
    public List<NucleotideGlyph> decodeUngapped() {
        List<NucleotideGlyph> withoutGaps = decode();
        final List<Integer> gapIndexes = getGapIndexes();
        for (int i = gapIndexes.size() - 1; i >= 0; i--) {
            withoutGaps.remove(gapIndexes.get(i).intValue());
        }
        return withoutGaps;
    }
}
