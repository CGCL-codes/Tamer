package org.phramer.v1.constraints.blockorder.core.inphrase;

import org.phramer.v1.constraints.blockorder.core.*;
import org.phramer.v1.decoder.*;
import org.phramer.v1.decoder.table.wordalignment.*;
import info.olteanu.utils.lang.*;
import java.util.*;

public class InPhraseAnalysisTools {

    private static final boolean DEBUG = true;

    public static InPhraseAnalysisDescriptor getDescriptor(PhraseTranslationVariant[][][] phraseTableVariants, WordAlignmentBuilder wordAlignmentBuilder, BlockConstraint[] bc, OrderConstraint[] oc) {
        int maxPhraseLength = getMaxPhraseLength(phraseTableVariants);
        InPhraseAnalysisDescriptorBlockConstraint[] bcD = new InPhraseAnalysisDescriptorBlockConstraint[bc.length];
        for (int i = 0; i < bcD.length; i++) bcD[i] = InPhraseAnalysisTools.getDescriptorForBlockConstraint(phraseTableVariants, wordAlignmentBuilder, maxPhraseLength, bc[i]);
        InPhraseAnalysisDescriptorOrderConstraint[] ocD = new InPhraseAnalysisDescriptorOrderConstraint[oc.length];
        for (int i = 0; i < ocD.length; i++) ocD[i] = InPhraseAnalysisTools.getDescriptorForOrderConstraint(phraseTableVariants, wordAlignmentBuilder, maxPhraseLength, oc[i]);
        return new InPhraseAnalysisDescriptor(bcD, ocD);
    }

    private static int getMaxPhraseLength(PhraseTranslationVariant[][][] phraseTableVariants) {
        int max = -1;
        for (int i = 0; i < phraseTableVariants.length; i++) if (phraseTableVariants[i] != null) max = Math.max(max, phraseTableVariants[i].length);
        return max;
    }

    private static InPhraseAnalysisDescriptorBlockConstraint getDescriptorForBlockConstraint(PhraseTranslationVariant[][][] phraseTableVariants, WordAlignmentBuilder wordAlignmentBuilder, int maxPhraseLength, BlockConstraint bc) {
        int bi = bc.i, bj = bc.j;
        if (bi == bj) return null;
        InPhraseAnalysisDescriptorBlockConstraint d = new InPhraseAnalysisDescriptorBlockConstraint(wordAlignmentBuilder.generatesTheSameObjectForTheSameAlignment());
        MutableBool bad = new MutableBool();
        MutableBool flip = new MutableBool();
        Set<ByteArrayHasher> processedAlignments = new HashSet<ByteArrayHasher>();
        for (int start = Math.max(0, bi - maxPhraseLength + 1); start <= bj; start++) if (phraseTableVariants[start] != null) {
            int minLenX = start < bi ? bi - start : 0;
            assert start >= bi || !blockOverlapPartial(start, start + minLenX - 1, bi, bj);
            assert blockOverlap(start, start + minLenX, bi, bj) : start + " " + (start + minLenX) + "   (" + bi + "," + bj + ")";
            for (int i = minLenX; i < phraseTableVariants[start].length; i++) if (phraseTableVariants[start][i] != null) {
                int end = start + i;
                if (blockOverlapPartial(start, end, bi, bj)) {
                    processedAlignments.clear();
                    for (PhraseTranslationVariant ptv : phraseTableVariants[start][i]) if (ptv.translationTableLine != null && ptv.translationTableLine.getWordAlignment() != null) {
                        byte[] encodedWordAlignment = (byte[]) ptv.translationTableLine.getWordAlignment();
                        ByteArrayHasher hashValue = new ByteArrayHasher(encodedWordAlignment);
                        if (!processedAlignments.contains(hashValue)) {
                            processedAlignments.add(hashValue);
                            boolean[][] array = wordAlignmentBuilder.decodeWordAlignmentIntoMatrix(i + 1, ptv.translationTableLine.getTranslation().length, encodedWordAlignment);
                            if (bi >= start && bj <= end) {
                                if (isBadBlock(start, end, bi, bj, array)) {
                                    if (DEBUG) System.err.println("Bad nonoverlapping " + getDebugString(start, end, bc, wordAlignmentBuilder.decodeWordAlignment(-1, -1, encodedWordAlignment)));
                                    d.bad.add(AlignmentIdentifier.getAlignmentIdentifier(d.wordAlignmentBuilderWIthCache, start, end, encodedWordAlignment));
                                }
                            } else {
                                checkFlippedOrBadInNonInclusion(start, end, bi, bj, array, bad, flip);
                                assert !bad.value || !flip.value;
                                if (bad.value) {
                                    if (DEBUG) System.err.println("Bad overlapping " + getDebugString(start, end, bc, wordAlignmentBuilder.decodeWordAlignment(-1, -1, encodedWordAlignment)));
                                    d.bad.add(AlignmentIdentifier.getAlignmentIdentifier(d.wordAlignmentBuilderWIthCache, start, end, encodedWordAlignment));
                                }
                                if (flip.value) {
                                    if (DEBUG) System.err.println("Flipped " + getDebugString(start, end, bc, wordAlignmentBuilder.decodeWordAlignment(-1, -1, encodedWordAlignment)));
                                    d.flipped.add(AlignmentIdentifier.getAlignmentIdentifier(d.wordAlignmentBuilderWIthCache, start, end, encodedWordAlignment));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (DEBUG) System.err.println("Constraint " + bc + " : " + d.bad.size() + " bad and " + d.flipped.size() + " flipped");
        if (d.isEmpty()) return null;
        return d;
    }

    private static void checkFlippedOrBadInNonInclusion(int a, int b, int i, int j, boolean[][] array, MutableBool isBad, MutableBool isFlip) {
        isBad.value = false;
        isFlip.value = false;
        assert array.length == (b - a + 1);
        assert !(a <= i && j <= b);
        int breakPoint;
        if (i > a && i <= b) breakPoint = i - 1; else {
            assert (j >= a && j < b);
            breakPoint = j;
        }
        breakPoint -= a;
        IntPair projectionStart = getProjection(array, 0, breakPoint);
        IntPair projectionEnd = getProjection(array, breakPoint + 1, array.length - 1);
        assert projectionStart != null || projectionEnd != null : "At least one half should have non-null projection...";
        if (projectionStart == null || projectionEnd == null) return;
        if (projectionStart.second < projectionEnd.first) return;
        if (projectionStart.first > projectionEnd.second) {
            isFlip.value = true;
            return;
        }
        isBad.value = true;
        return;
    }

    protected static boolean isBadBlock(int start, int end, int bi, int bj, boolean[][] array) {
        assert array.length == (end - start + 1);
        assert bi >= start && bi <= end;
        assert bj >= start && bj <= end;
        bi -= start;
        bj -= start;
        assert bi >= 0 && bi < array.length;
        assert bj >= 0 && bj < array.length;
        IntPair blockProjection = getProjection(array, bi, bj);
        if (blockProjection == null) return false;
        return checkBlockBadOverlap(bi, bj, array, blockProjection.first, blockProjection.second);
    }

    private static boolean checkBlockBadOverlap(int bi, int bj, boolean[][] array, int low, int high) {
        for (int i = 0; i < bi; i++) for (int j = low; j <= high; j++) if (array[i][j]) return true;
        for (int i = bj + 1; i < array.length; i++) for (int j = low; j <= high; j++) if (array[i][j]) return true;
        return false;
    }

    private static IntPair getProjection(boolean[][] array, int start, int end) {
        int minIndexEforBlock = Integer.MAX_VALUE;
        int maxIndexEforBlock = -1;
        for (int i = start; i <= end; i++) for (int j = 0; j < array[i].length; j++) if (array[i][j]) {
            if (minIndexEforBlock > j) minIndexEforBlock = j;
            if (maxIndexEforBlock < j) maxIndexEforBlock = j;
        }
        if (maxIndexEforBlock == -1) return null;
        return new IntPair(minIndexEforBlock, maxIndexEforBlock);
    }

    private static boolean blockOverlapPartial(int a, int b, int i, int j) {
        if (i > a && i <= b) return true;
        if (j >= a && j < b) return true;
        return false;
    }

    private static boolean blockOverlap(int a, int b, int i, int j) {
        if (i >= a && i <= b) return true;
        if (j >= a && j <= b) return true;
        if (a >= i && a <= j) return true;
        if (b >= i && b <= j) return true;
        return false;
    }

    private static InPhraseAnalysisDescriptorOrderConstraint getDescriptorForOrderConstraint(PhraseTranslationVariant[][][] phraseTableVariants, WordAlignmentBuilder wordAlignmentBuilder, int maxPhraseLength, OrderConstraint oc) {
        int i1 = oc.i1, j1 = oc.j1;
        int i2 = oc.i2, j2 = oc.j2;
        InPhraseAnalysisDescriptorOrderConstraint d = new InPhraseAnalysisDescriptorOrderConstraint(wordAlignmentBuilder.generatesTheSameObjectForTheSameAlignment());
        Set<ByteArrayHasher> processedAlignments = new HashSet<ByteArrayHasher>();
        for (int start = 0; start < phraseTableVariants.length; start++) if (phraseTableVariants[start] != null) for (int i = 0; i < phraseTableVariants[start].length; i++) if (phraseTableVariants[start][i] != null) {
            int end = start + i;
            if (blockOverlap(start, end, i1, j1) && blockOverlap(start, end, i2, j2)) {
                processedAlignments.clear();
                for (PhraseTranslationVariant ptv : phraseTableVariants[start][i]) if (ptv.translationTableLine != null && ptv.translationTableLine.getWordAlignment() != null) {
                    byte[] encodedWordAlignment = (byte[]) ptv.translationTableLine.getWordAlignment();
                    ByteArrayHasher hashValue = new ByteArrayHasher(encodedWordAlignment);
                    if (!processedAlignments.contains(hashValue)) {
                        processedAlignments.add(hashValue);
                        boolean[][] array = wordAlignmentBuilder.decodeWordAlignmentIntoMatrix(i + 1, ptv.translationTableLine.getTranslation().length, encodedWordAlignment);
                        if (isBadOrder(start, end, i1, j1, i2, j2, array)) {
                            if (DEBUG) System.err.println("Bad " + getDebugString(start, end, oc, wordAlignmentBuilder.decodeWordAlignment(-1, -1, encodedWordAlignment)));
                            d.bad.add(AlignmentIdentifier.getAlignmentIdentifier(d.wordAlignmentBuilderWIthCache, start, end, encodedWordAlignment));
                        }
                    }
                }
            }
        }
        if (DEBUG) System.err.println("Constraint " + oc + " : " + d.bad.size() + " bad");
        if (d.isEmpty()) return null;
        return d;
    }

    protected static boolean isBadOrder(int start, int end, int i1, int j1, int i2, int j2, boolean[][] array) {
        assert array.length == (end - start + 1);
        i1 = Math.max(i1 - start, 0);
        j1 = Math.min(j1 - start, array.length - 1);
        i2 = Math.max(i2 - start, 0);
        j2 = Math.min(j2 - start, array.length - 1);
        assert i1 >= 0 && i1 < array.length;
        assert j1 >= 0 && j1 < array.length;
        assert i2 >= 0 && i2 < array.length;
        assert j2 >= 0 && j2 < array.length;
        IntPair projection1 = getProjection(array, i1, j1);
        IntPair projection2 = getProjection(array, i2, j2);
        if (projection1 == null || projection2 == null) return false;
        if (!(projection1.second < projection2.first)) return true;
        return false;
    }

    private static String getDebugString(int start, int end, PrimitiveConstraint bc, BytePair[] wa) {
        return "[" + start + "," + end + "] << " + WordAlignmentTools.serializeWordAlignment(-1, -1, wa, -1) + " >> for " + bc;
    }

    public static AlignmentIdentifier getIdentifier(boolean cache, PhraseTranslationVariant phrase, int a, int b) {
        if (phrase.translationTableLine != null && phrase.translationTableLine.getWordAlignment() != null) return AlignmentIdentifier.getAlignmentIdentifier(cache, a, b, phrase.translationTableLine.getWordAlignment());
        return null;
    }
}
