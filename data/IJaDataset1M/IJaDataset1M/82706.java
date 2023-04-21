package org.tigr.seq.seqdata;

import java.util.*;
import org.tigr.seq.seqdata.edit.*;
import org.tigr.seq.cloe.Cloe;
import org.tigr.seq.log.*;

/**
 *
 * Describe class <code>SeqdataUtil</code> here. 
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: SeqdataUtil.java,v $
 * $Revision: 1.65 $
 * $Date: 2005/12/09 18:27:29 $
 * $Author: dkatzel $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public class SeqdataUtil {

    /**
     * Stringify the array of shorts as unsigned values.
     *
     *
     * @param pQuality a <code>short[]</code> value
     * 
     * @return a <code>String</code> value
     *
     */
    public static String unsignedShortsToString(short[] pQuality) {
        String ret = null;
        if (pQuality != null) {
            char[] charQual = new char[pQuality.length * 6];
            Arrays.fill(charQual, ' ');
            int cq = 0;
            for (int q = 0; q < pQuality.length; q++) {
                if (pQuality[q] >= 10000) {
                    charQual[cq++] = (char) ((pQuality[q] / 10000) + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 1000) % 10 + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 100) % 10 + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 10) % 10 + 0x30);
                } else if (pQuality[q] >= 1000) {
                    charQual[cq++] = (char) ((pQuality[q] / 1000) + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 100) % 10 + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 10) % 10 + 0x30);
                } else if (pQuality[q] >= 100) {
                    charQual[cq++] = (char) ((pQuality[q] / 100) + 0x30);
                    charQual[cq++] = (char) ((pQuality[q] / 10) % 10 + 0x30);
                } else if (pQuality[q] >= 10) {
                    charQual[cq++] = (char) ((pQuality[q] / 10) + 0x30);
                }
                charQual[cq++] = (char) ((pQuality[q] % 10) + 0x30);
                cq++;
            }
            ret = new String(charQual);
            ret = ret.trim();
        }
        return ret;
    }

    /**
     * Describe <code>stringToShorts</code> method here.
     *
     *
     * @param pData a <code>String</code> value
     * 
     * @return a <code>short[]</code> value
     *
     */
    public static short[] stringToShorts(String pData) {
        String qualString = pData.trim();
        StringTokenizer token = new StringTokenizer(qualString, " ");
        int size = token.countTokens();
        short[] qualArray = new short[size];
        int counter = 0;
        while (token.hasMoreElements()) {
            qualArray[counter] = java.lang.Short.parseShort(token.nextToken());
            counter++;
        }
        return qualArray;
    }

    /**
     * Remove gap characters ('-') from a sequence.
     *
     * @param   pSequence   A <code>String</code> object, the
     *                      original sequence with gaps (perhaps).
     *
     * @return  An <code>String</code> object, a new sequence
     *          which represents just the non-gap characters of the original.
     */
    public static String removeGaps(String pSequence) {
        char[] newSequence;
        char c;
        int oldIdx, newIdx;
        newSequence = new char[pSequence.length()];
        for (newIdx = 0, oldIdx = 0; oldIdx < pSequence.length(); oldIdx++) {
            c = pSequence.charAt(oldIdx);
            if (c != '-') {
                newSequence[newIdx++] = c;
            }
        }
        return new String(newSequence, 0, newIdx);
    }

    public static ICharSequenceData removeGaps(ICharSequenceData pSequence) {
        char[] oldSequence = pSequence.toArray();
        char[] newSequence;
        char c;
        int oldIdx, newIdx;
        newSequence = new char[oldSequence.length];
        for (newIdx = 0, oldIdx = 0; oldIdx < oldSequence.length; oldIdx++) {
            c = oldSequence[oldIdx];
            if (c != '-') {
                newSequence[newIdx++] = c;
            }
        }
        char[] tmp = new char[newIdx];
        System.arraycopy(newSequence, 0, tmp, 0, newIdx);
        return new SimpleCharSequenceData(tmp);
    }

    /**
     * Reverse complementing with <code>char []</code> arrays.
     *
     *
     * @param pData a <code>char[]</code> value
     * 
     * @return a <code>char[]</code> value
     *
     */
    public static char[] reverseComplementSequence(char[] pData) {
        char chardata[] = pData;
        char[] complemented = new char[chardata.length];
        char comp;
        for (int i = 0; i < chardata.length; i++) {
            switch(chardata[i]) {
                case 'A':
                    comp = 'T';
                    break;
                case 'T':
                    comp = 'A';
                    break;
                case 'G':
                    comp = 'C';
                    break;
                case 'C':
                    comp = 'G';
                    break;
                case '-':
                    comp = '-';
                    break;
                case 'B':
                    comp = 'V';
                    break;
                case 'V':
                    comp = 'B';
                    break;
                case 'D':
                    comp = 'H';
                    break;
                case 'H':
                    comp = 'D';
                    break;
                case 'R':
                    comp = 'Y';
                    break;
                case 'Y':
                    comp = 'R';
                    break;
                case 'K':
                    comp = 'M';
                    break;
                case 'M':
                    comp = 'K';
                    break;
                case 'a':
                    comp = 't';
                    break;
                case 't':
                    comp = 'a';
                    break;
                case 'g':
                    comp = 'c';
                    break;
                case 'c':
                    comp = 'g';
                    break;
                case 'b':
                    comp = 'v';
                    break;
                case 'v':
                    comp = 'b';
                    break;
                case 'd':
                    comp = 'h';
                    break;
                case 'h':
                    comp = 'd';
                    break;
                case 'r':
                    comp = 'y';
                    break;
                case 'y':
                    comp = 'r';
                    break;
                case 'k':
                    comp = 'm';
                    break;
                case 'm':
                    comp = 'k';
                    break;
                default:
                    comp = chardata[i];
                    break;
            }
            complemented[chardata.length - i - 1] = comp;
        }
        return complemented;
    }

    /**
     * This method passes in a ICharSequenceData and returns a reverse
     * complemented ICharSequenceData instance
     *
     * @param  pData  a <code>ICharSequenceData</code>  value
     *
     * @return A <code> IMutableCharSequenceData</code> */
    public static IMutableCharSequenceData reverseComplementSequence(ICharSequenceData pData) {
        IMutableCharSequenceData ret = null;
        char[] complemented = SeqdataUtil.reverseComplementSequence(pData.toArray());
        ret = new SimpleCharSequenceData(complemented);
        return ret;
    }

    /**
     * This method converts a 0-based offset in a gapped sequence to a 1-based
     * position value in the corresponding ungapped sequence.
     *
     * @param   A <code>String</code> value, the gapped sequence.
     * @param   An <code>int</code> value, the offset to be converted.
     *
     * @return  An <code>int</code> value, the 1-based position within the
     *          ungapped sequence.
     *
     */
    public static int offsetToPosition(String pSequence, int pOffset) {
        int position = 1;
        for (int i = 0; i < pOffset && i < pSequence.length(); i++) {
            if (pSequence.charAt(i) != '-') {
                position++;
            }
        }
        return position;
    }

    /**
     * Describe <code>calculateGappedAndComplementedQualityData</code> method here.
     *
     *
     * @param pForwardUngappedQualities an <code>IShortSequenceData</code> value
     * 
     * @param pValidRangeLeftEnd an <code>int</code> value
     * 
     * @param pValidRangeRightEnd an <code>int</code> value
     * 
     * @param pGapPositions a <code>short[]</code> value
     * 
     * @return an <code>IShortSequenceData</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public static IShortSequenceData calculateGappedAndComplementedQualityData(IShortSequenceData pForwardUngappedQualities, int pValidRangeLeftEnd, int pValidRangeRightEnd, short[] pGapPositions) throws SeqdataException {
        IShortSequenceData ret = null;
        short[] gappedQualities = new short[pForwardUngappedQualities.getSize() + pGapPositions.length];
        short[] ungappedQualities = (short[]) pForwardUngappedQualities.toArray().clone();
        int validRangeLeftEnd;
        int validRangeRightEnd;
        if (pValidRangeRightEnd < pValidRangeLeftEnd) {
            for (int i = 0; i < ungappedQualities.length / 2; i++) {
                short temp = ungappedQualities[i];
                ungappedQualities[i] = ungappedQualities[ungappedQualities.length - 1 - i];
                ungappedQualities[ungappedQualities.length - 1 - i] = temp;
            }
            validRangeLeftEnd = pForwardUngappedQualities.getSize() - pValidRangeLeftEnd + 1;
            validRangeRightEnd = pForwardUngappedQualities.getSize() - pValidRangeRightEnd + 1;
        } else {
            validRangeLeftEnd = pValidRangeLeftEnd;
            validRangeRightEnd = pValidRangeRightEnd;
        }
        int gapSize = pGapPositions.length;
        int validLength = validRangeRightEnd - validRangeLeftEnd + 1;
        if (gapSize > 0) {
            for (short gapPos : pGapPositions) {
                if (gapPos > validLength) {
                    System.out.println(gapPos + "  " + validLength);
                    throw new SeqdataException("Error: There is a gap beyond the last base in the valid range");
                }
            }
        }
        for (int i = 0; i < validRangeLeftEnd - 1; i++) {
            gappedQualities[i] = ungappedQualities[i];
        }
        int gq = validRangeLeftEnd - 1;
        int g = 0;
        final int GAP_QUALITY = -11;
        for (int u = validRangeLeftEnd - 1; u < validRangeRightEnd; u++) {
            int v = u - validRangeLeftEnd + 2;
            while (g < pGapPositions.length && pGapPositions[g] == v) {
                gappedQualities[gq] = GAP_QUALITY;
                gq++;
                g++;
            }
            gappedQualities[gq] = ungappedQualities[u];
            gq++;
        }
        int u = validRangeRightEnd;
        for (g = validRangeRightEnd + pGapPositions.length; g < gappedQualities.length; g++) {
            gappedQualities[g] = ungappedQualities[u];
            u++;
        }
        u = 0;
        for (g = 0; g < gappedQualities.length; g++) {
            if (gappedQualities[g] == GAP_QUALITY) continue;
            if (gappedQualities[g] != ungappedQualities[u]) {
                System.out.println("ERROR: gapped=" + gappedQualities[g] + "  and ungapped=" + ungappedQualities[u] + "  do not agree!");
                System.out.println("discrepancy at u=" + u + ", g=" + g + " for valid range [" + pValidRangeLeftEnd + ", " + pValidRangeRightEnd + "]");
                Log.log(Log.ERROR, new Throwable(), ResourceUtil.getMessage(SeqdataUtil.class, "mismatched_qualities"));
                String message = ResourceUtil.getResource(SeqdataUtil.class, "text.mismatched_qualities");
                throw new SeqdataException(message);
            }
            u++;
        }
        for (int i = 0; i < gappedQualities.length - 1; i++) {
            if (gappedQualities[i] == 99) {
                gappedQualities[i] = (short) AssemblyEditUtil.EDITED_QUALITY_VALUE;
            }
        }
        for (int i = 1; i < gappedQualities.length - 1; i++) {
            if (gappedQualities[i] == GAP_QUALITY) {
                int below = i - 1;
                int above = i + 1;
                while (below >= 0 && gappedQualities[below] == GAP_QUALITY) below--;
                while (above < gappedQualities.length && gappedQualities[above] == GAP_QUALITY) above++;
                below = Math.max(below, 0);
                above = Math.min(gappedQualities.length - 1, above);
                gappedQualities[i] = (short) Math.min(gappedQualities[below], gappedQualities[above]);
            }
        }
        ret = new SimpleShortSequenceData(gappedQualities);
        return ret;
    }

    /**
     * Reverse an IShortSequenceData object that contains trace peak positions.  
     * Returns a new object with the elements reversed (the original object is 
     * unaffected).  Since the elements are trace peak positions, the elements
     * themselves are modified to reflect the fact that index 0 is now at the
     * opposite end of the trace peaks array.
     *
     * @param   An <code>IShortSequenceData</code> value, the positions data.
     * @patam   An <code>int</code>value, the length of the trace data.
     *
     * @return  A new <code>IShortSequenceData</code> value.
     *
     */
    public static IShortSequenceData reversePositionData(IShortSequenceData pPositions, int tracesLength) {
        short[] reversed;
        reversed = SeqdataUtil.reverseShortArray(pPositions.toArray());
        for (int i = 0; i < reversed.length; i++) {
            reversed[i] = (short) (tracesLength - reversed[i] - 1);
        }
        return new SimpleShortSequenceData(reversed);
    }

    /**
     * Remove the elements corresponding to sequence gaps from an
     * IShortSequenceData object.  The object returned is a new object (the
     * original object is unaffected).
     *
     * @param   A <code>String</code> value, a DNA sequence whose gaps guide the
     *          removal of gaps from the data.
     * @patam   An <code>IShortSequenceData</code>value, the data from which
     *          gaps are to be removed.
     *
     * @return  A new <code>IShortSequenceData</code> value.
     *
     */
    public static IShortSequenceData removeGapsFromShortSequenceData(String pSequence, IShortSequenceData pData) throws SeqdataException {
        char[] seq;
        short[] in;
        short[] out;
        int i, j, gaps;
        if (pSequence.length() != pData.getSize()) {
            String message = ResourceUtil.getResource(SeqdataUtil.class, "text.data_length_mismatch");
            throw new SeqdataException(message);
        }
        seq = pSequence.toCharArray();
        in = pData.toArray();
        for (i = 0, gaps = 0; i < seq.length; i++) {
            if (seq[i] == '-') {
                gaps++;
            }
        }
        out = new short[in.length - gaps];
        for (i = 0, j = 0; i < seq.length; i++) {
            if (seq[i] != '-') {
                out[j++] = in[i];
            }
        }
        return new SimpleShortSequenceData(out);
    }

    /**
     * Reverse an IShortSequenceData object.  Returns a new object with the
     * elements reversed (the original object is unaffected).
     *
     * @param   An <code>IShortSequenceData</code> value.
     *
     * @return  A new <code>IShortSequenceData</code> value.
     *
     */
    public static IShortSequenceData reverseShortSequenceData(IShortSequenceData pData) {
        return new SimpleShortSequenceData(SeqdataUtil.reverseShortArray(pData.toArray()));
    }

    /**
     * Reverse this trace data as appropriate.  It's assumed that the
     * caller is passing in the A trace if we're trying to reverse
     * complement the T data (i.e., the caller is assumed to have
     * handled the complementing).  This is a fairly simple array
     * reversing operation, unlike the position reversing.
     *
     *
     * @return an <code>IShortSequenceData</code> value
     * */
    public static IShortSequenceData reverseTraceData(IShortSequenceData pData) {
        IShortSequenceData ret = null;
        if (pData != null) {
            ret = new SimpleShortSequenceData(SeqdataUtil.reverseShortArray(pData.toArray()));
        }
        return ret;
    }

    /**
     * Describe <code>reverseShortArrayData</code> method here.
     *
     *
     * @param pInputShortArray a <code>short[]</code> value
     * 
     * @return a <code>short[]</code> value
     *
     */
    public static short[] reverseShortArray(short[] pInputShortArray) {
        short[] rdata = (short[]) pInputShortArray.clone();
        int len = rdata.length;
        for (int i = 0; i < len / 2; i++) {
            short tmp = rdata[len - i - 1];
            rdata[len - i - 1] = rdata[i];
            rdata[i] = tmp;
        }
        return rdata;
    }

    /**
     * Describe <code>buildFullRangeGappedPositionData</code> method here.
     *
     *
     * @param pAssemblySequence an <code>IBaseAssemblySequence</code> value
     * 
     * @return an <code>IShortSequenceData</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public static IShortSequenceData buildFullRangeGappedPositionData(IBaseAssemblySequence pAssemblySequence) throws SeqdataException {
        short[] ungapped = (short[]) pAssemblySequence.getDelegateSequence().getPositionData().toArray().clone();
        char[] gappedSeq = pAssemblySequence.getGappedSequenceData().toArray();
        short[] ret = new short[gappedSeq.length];
        int tracesLength = pAssemblySequence.getDelegateSequence().getTracesLength();
        if (pAssemblySequence.isComplemented()) {
            ungapped = SeqdataUtil.reverseShortArray(ungapped);
        }
        int g = 0;
        int u = 0;
        for (g = 0; g < ret.length; g++) {
            if (gappedSeq[g] == '-') {
                ret[g] = -1;
                continue;
            }
            ret[g] = ungapped[u++];
        }
        short lastval = 0;
        short nextval = 0;
        int lasti = -1;
        int nexti = 0;
        g = 0;
        while (g < ret.length) {
            if (ret[g] != -1) {
                lasti = g;
                lastval = ret[g];
                g++;
            } else {
                for (; g < ret.length && ret[g] == -1; g++) ;
                nexti = g;
                if (g < ret.length) {
                    nextval = ret[g];
                } else {
                    nextval = (short) (tracesLength - 1);
                }
                for (int i = lasti + 1; i < nexti; i++) {
                    ret[i] = (short) (lastval + (nextval - lastval) * ((i - lasti) / ((double) (nexti - lasti))));
                }
                g = nexti - 1;
            }
        }
        if (pAssemblySequence.isComplemented()) {
            for (g = 0; g < ret.length; g++) {
                ret[g] = (short) (tracesLength - ret[g] - 1);
            }
        }
        return new SimpleShortSequenceData(ret);
    }

    /**
     * Describe <code>buildFullRangeGappedSequenceData</code> method here.
     *
     *
     * @param pAssemblySequence an <code>IBaseAssemblySequence</code> value
     * 
     * @return an <code>ICharSequenceData</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public static ICharSequenceData buildFullRangeGappedSequenceData(IBaseAssemblySequence pAssemblySequence) throws SeqdataException {
        char[] us;
        char[] ls = pAssemblySequence.getGappedValidRangeSequenceData().toArray();
        int lower = Math.min(pAssemblySequence.getValidRangeLeftEnd(), pAssemblySequence.getValidRangeRightEnd());
        int higher = Math.max(pAssemblySequence.getValidRangeLeftEnd(), pAssemblySequence.getValidRangeRightEnd());
        if (pAssemblySequence.isComplemented()) {
            us = SeqdataUtil.reverseComplementSequence(pAssemblySequence.getDelegateSequence().getSequenceData()).toArray();
            int temp = lower;
            lower = us.length - higher + 1;
            higher = us.length - temp + 1;
        } else {
            us = pAssemblySequence.getDelegateSequence().getSequenceData().toArray();
        }
        if (us.length + pAssemblySequence.getGapPositions().length != lower - 1 + ls.length + Math.abs(us.length - higher)) {
            Log.log(Log.ERROR, new Throwable(), ResourceUtil.getMessage(SeqdataUtil.class, "mismatched_lsequence_sequence"));
            String message = ResourceUtil.getResource(SeqdataUtil.class, "text.mismatched_lsequence_sequence", pAssemblySequence.getDelegateSequence().getSequenceName());
            SeqdataException ex = new SeqdataException(message);
            String logMessage = "mismatch lsequence: " + us.length + " + " + pAssemblySequence.getGapPositions().length + "!= " + (lower - 1 + ls.length + Math.abs(us.length - higher));
            Cloe.getLogger().error(logMessage, ex);
            throw ex;
        }
        char[] cret = new char[us.length + pAssemblySequence.getGapPositions().length];
        int r;
        for (r = 1; r < lower; r++) {
            cret[r - 1] = us[r - 1];
        }
        r--;
        for (int i = 0; i < ls.length; i++) {
            cret[r++] = ls[i];
        }
        for (int i = higher; i < us.length; i++) {
            cret[r++] = us[i];
        }
        return new SimpleCharSequenceData(cret);
    }

    /**
     * Extract the gapped valid range sequence from an IBaseAssemblySequence instance.
     *
     *
     * @param pAssemblySequence an <code>IBaseAssemblySequence</code> value
     * 
     * @return an <code>ICharSequenceData</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public static ICharSequenceData extractGappedValidRangeSequenceData(IBaseAssemblySequence pAssemblySequence) throws SeqdataException {
        System.out.println("fullgapped = " + new String(pAssemblySequence.getGappedSequenceData().toArray()));
        String validString = new String(pAssemblySequence.getGappedSequenceData().toArray(), pAssemblySequence.getSequenceStartOffset(), pAssemblySequence.getAlignmentDataLength());
        return new SimpleCharSequenceData(validString.toCharArray());
    }

    /**
     * Describe <code>getSequenceStartOffset</code> method here.
     *
     *
     * @param pAssemblySequence an <code>IBaseAssemblySequence</code> value
     * 
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public static int getSequenceStartOffset(IBaseAssemblySequence pAssemblySequence) throws SeqdataException {
        int ret;
        if (!pAssemblySequence.isComplemented()) {
            ret = pAssemblySequence.getValidRangeLeftEnd() - 1;
        } else {
            ret = pAssemblySequence.getGappedQualityData().getSize() - pAssemblySequence.getGapPositions().length - pAssemblySequence.getValidRangeLeftEnd();
        }
        return ret;
    }

    /**
     * Determine whether the first base (A, C, G, T) matches a second base, which
     * may be an ambiguity code.
     *
     * @param   A <code>char</code> value, the first base (A, C, G or T), upper
     *          or lower case.
     *
     * @param   A <code>char</code> value, the second base (A, C, G, T or an
     *          ambiguity code), upper or lower case.
     *
     * @return  A <code>boolean</code> value, true if the first base is equal
     *          to the second (ignoring case) or falls within the set defined by
     *          the ambiguity code.
     *
     */
    public static boolean baseMatchesBaseOrAmbiguity(char pBase, char pBaseOrAmbiguity) {
        boolean ret = false;
        String ambiguities = null;
        pBase = Character.toUpperCase(pBase);
        pBaseOrAmbiguity = Character.toUpperCase(pBaseOrAmbiguity);
        if (pBase == pBaseOrAmbiguity) {
            ret = true;
        } else {
            switch(pBase) {
                case 'A':
                    ambiguities = "RMWDHVN";
                    break;
                case 'C':
                    ambiguities = "YMSBHVN";
                    break;
                case 'G':
                    ambiguities = "RKSBDVN";
                    break;
                case 'T':
                    ambiguities = "YKWBDHN";
                    break;
            }
            if (ambiguities != null && ambiguities.indexOf(pBaseOrAmbiguity) >= 0) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     *
     * Take the input positions array, look for instances of pSentinel
     * values to indicate positions corresponding to gaps, and
     * substitute said positions with interpolations of the flanking
     * positions.  If the pInputQualities parameter is non-null, we'll
     * fill in the lesser of the flanking qualities while we're at it.
     *
     * <p>
     *
     * This method will modify the input arrays, so clone them if you
     * wish to preserve the originals.
     *
     * @param pInputPositions a <code>short[]</code> value
     * 
     * @param pInputQualities a <code>short[]</code> value
     * 
     * @param pSentinel a <code>short</code> value
     */
    public static void fillInPositionAndQualityGaps(short[] pInputPositions, short[] pInputQualities, short pSentinel) {
        for (int i = 0; i < pInputPositions.length; i++) {
            if (pInputPositions[i] == pSentinel) {
                int lower = Math.max(0, i - 1);
                int upper = Math.min(pInputPositions.length - 1, i + 1);
                while (lower > 0 && pInputPositions[lower] == pSentinel) lower--;
                while (upper < pInputPositions.length - 1 && pInputPositions[upper] == pSentinel) upper++;
                int lowerpos = pInputPositions[lower];
                int upperpos = pInputPositions[upper];
                if (pInputPositions[lower] == pSentinel) {
                    pInputPositions[lower] = (short) 1;
                    lowerpos = 1;
                }
                if (pInputPositions[upper] == pSentinel) {
                    pInputPositions[upper] = (short) (pInputPositions[lower] + 1);
                    upperpos = pInputPositions[lower] + 1;
                }
                int lowerqual = 0, upperqual = 0;
                if (pInputQualities != null) {
                    lowerqual = pInputQualities[lower];
                    upperqual = pInputQualities[upper];
                }
                if (lowerqual == pSentinel && pInputQualities != null) {
                    pInputQualities[lower] = AssemblyEditUtil.LOW_QUALITY_VALUE;
                    lowerqual = AssemblyEditUtil.LOW_QUALITY_VALUE;
                }
                if (upperqual == pSentinel && pInputQualities != null) {
                    pInputQualities[upper] = AssemblyEditUtil.LOW_QUALITY_VALUE;
                    upperqual = AssemblyEditUtil.LOW_QUALITY_VALUE;
                }
                for (int j = 0; j < upper - lower - 1; j++) {
                    pInputPositions[lower + j + 1] = (short) (lowerpos + ((j + 1) * (upperpos - lowerpos)) / ((float) (upper - lower)));
                    if (pInputQualities != null) {
                        pInputQualities[lower + j + 1] = (short) Math.min(lowerqual, upperqual);
                    }
                }
            }
        }
    }

    /**
     * Given the specified lsequence, construct the list of 1-based
     * positions in the ungapped sequence at which gaps would be
     * inserted into to create the gapped sequence.
     *
     *
     * @param pLsequence a <code>String</code> value
     * 
     * @return a <code>short[]</code> value
     * */
    public static short[] gapIndexer(String pLsequence) {
        short[] ret = null;
        short[] tmpgaps = new short[pLsequence.length()];
        short nonGapIndex = 1;
        int anyIndex = 0;
        int gapCounter = 0;
        int length = pLsequence.length();
        while (anyIndex < length) {
            char c = pLsequence.charAt(anyIndex);
            if (c == '-') {
                tmpgaps[gapCounter] = nonGapIndex;
                gapCounter++;
            } else {
                nonGapIndex++;
            }
            anyIndex++;
        }
        ret = new short[gapCounter];
        for (int i = 0; i < gapCounter; i++) {
            ret[i] = tmpgaps[i];
        }
        return ret;
    }
}
