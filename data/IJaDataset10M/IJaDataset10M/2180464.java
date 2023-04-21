package com.solidmatrix.regxmaker.util.shared.matching;

import java.util.Arrays;
import java.util.LinkedList;
import com.solidmatrix.regxmaker.util.shared.SortUtils;

/**
 * <PRE>
 * Name   : com.solidmatrix.regxmaker.util.shared.matching.LongMatcher
 * Project: RegXmaker Library
 * Version: 1.1
 * Tier   : 3 (Function Class)
 * Author : Gennadiy Shafranovich
 * Purpose: Finds matching sequences in two long arrays
 *
 * Copyright (C) 2001, 2004 SolidMatrix Technologies, Inc.
 * This file is part of RegXmaker Library.
 *
 * RegXmaker Library is is free software; you can redistribute it and/or modify
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * RegXmaker library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Comments: Full, with javadoc.
 *
 * Modification History
 *
 * 06-28-2001 GS Created
 *               NOTE: implemintaion has to be tested
 *
 * 07-05-2001 GS Added FailSafe features
 * 
 * 07-05-2004 YS Added licensing information
 * </PRE>
 */
public final class LongMatcher extends Matcher {

    /**
     * First input long array
     */
    private long[] in1;

    /**
     * Second input long array
     */
    private long[] in2;

    /**
     * Boolean array containing flags wether a certain location 
     * in the first input long array is a delimiter for 
     * multi-string splice.
     */
    private boolean[] upperDelim;

    /**
     * Boolean array containing flags wether a certain location 
     * in the second input long array is a delimiter for 
     * multi-string splice.
     */
    private boolean[] lowerDelim;

    /**
	 * Constructs a new LongMatcher with a given SecureObject,
     * input long arrays and delimiters arrays and a default ignore
     * weight.<BR>
	 *
	 * @param   o  SecureObject used for verification
	 * @param   t1  First input long array
	 * @param   t2  Second input long array
	 * @param   upper  boolean array containing delimiter flags for first long array
	 * @param   lower  boolean array containing delimiter flags for second long array
     * @throws SecurityException if the given SecureObject has not been verified
	 */
    public LongMatcher(long[] t1, long[] t2, boolean[] upper, boolean[] lower) throws SecurityException {
        this(t1, t2, upper, lower, DEFAULT_IGNORE_WEIGHT, false);
    }

    /**
	 * Constructs a new LongMatcher with a given SecureObject,
     * input long arrays, no delimiters within the long arrays,
     * and the default ignore weight.<BR>
	 *
	 * @param   o  SecureObject used for verification
	 * @param   t1  First input long array
	 * @param   t2  Second input long array
     * @throws SecurityException if the given SecureObject has not been verified
	 */
    public LongMatcher(long[] t1, long[] t2) throws SecurityException {
        this(t1, t2, DEFAULT_IGNORE_WEIGHT);
    }

    /**
	 * Constructs a new LongMatcher with a given SecureObject,
     * input long arrays, no delimiters within the long arrays,
     * and a given ignore weight.<BR>
	 *
	 * @param   o  SecureObject used for verification
	 * @param   t1  First input long array
	 * @param   t2  Second input long array
	 * @param   iw  minimum size of match to be recorded
     * @throws SecurityException if the given SecureObject has not been verified
	 */
    public LongMatcher(long[] t1, long[] t2, int iw) throws SecurityException {
        this(t1, t2, makeEmptyDelimArray(t1.length), makeEmptyDelimArray(t2.length), iw, false);
    }

    /**
	 * Constructs a new LongMatcher with a given SecureObject,
     * input long arrays, delimiter arrays, and a given ignore weight.<BR>
	 *
	 * @param   o  SecureObject used for verification
	 * @param   t1  First input long array
	 * @param   t2  Second input long array
	 * @param   upper  boolean array containing delimiter flags for first long array
	 * @param   lower  boolean array containing delimiter flags for second long array
	 * @param   iw  minimum size of match to be recorded
     * @throws SecurityException if the given SecureObject has not been verified
	 */
    public LongMatcher(long[] t1, long[] t2, boolean[] upper, boolean[] lower, int iw) throws SecurityException {
        this(t1, t2, upper, lower, iw, false);
    }

    /**
     * Constructs a new LongMatcher with a given SecureObject,
     * input long arrays, delimiter arrays, a given ignore weight,
     * and a given debug status.<BR>
     *
     * @param   o  SecureObject used for verification
     * @param   t1  First input long array
     * @param   t2  Second input long array
     * @param   upper  boolean array containing delimiter flags for first long array
     * @param   lower  boolean array containing delimiter flags for second long array
     * @param   iw  minimum size of match to be recorded
     * @param   debug  flag wether the matcher will be in debug mode
     * @throws SecurityException if the given SecureObject has not been verified
     */
    public LongMatcher(long[] t1, long[] t2, boolean[] upper, boolean[] lower, int iw, boolean debug) throws SecurityException {
        super(iw, debug);
        long start = System.currentTimeMillis();
        processing = true;
        in1 = t1;
        in2 = t2;
        upperDelim = upper;
        lowerDelim = lower;
        long[] sorted1 = new long[in1.length];
        for (int i = 0; i < sorted1.length && processing; i++) sorted1[i] = in1[i];
        int[] pos1 = SortUtils.sortRetain(sorted1);
        long[] sorted2 = new long[in2.length];
        for (int j = 0; j < sorted2.length && processing; j++) sorted2[j] = in2[j];
        int[] pos2 = SortUtils.sortRetain(sorted2);
        LinkedList uBins = new LinkedList();
        LinkedList lBins = new LinkedList();
        int s1 = 0;
        int s2 = 0;
        int e1 = s1;
        int e2 = s2;
        int s1l = sorted1.length;
        int s2l = sorted2.length;
        do {
            while (s1 < s1l && s2 < s2l && sorted1[s1] != sorted2[s2] && processing) {
                for (; s2 < s2l && sorted1[s1] > sorted2[s2] && processing; s2++) ;
                for (; s2 < s2l && s1 < s1l && sorted1[s1] < sorted2[s2] && processing; s1++) ;
            }
            for (e1 = s1; e1 < s1l && sorted1[s1] == sorted1[e1] && processing; e1++) ;
            for (e2 = s2; e2 < s2l && sorted2[s2] == sorted2[e2] && processing; e2++) ;
            int[] uBin = new int[e1 - s1];
            for (int i = 0; s1 < e1 && processing; i++, s1++) uBin[i] = pos1[s1];
            int[] lBin = new int[e2 - s2];
            for (int i = 0; s2 < e2 && processing; i++, s2++) lBin[i] = pos2[s2];
            Arrays.sort(uBin);
            Arrays.sort(lBin);
            uBins.add(uBin);
            lBins.add(lBin);
            uBin = null;
            lBin = null;
        } while (s1 < s1l && s2 < s2l);
        sorted1 = null;
        sorted2 = null;
        pos1 = null;
        pos2 = null;
        prepareUpperBins(uBins);
        prepareLowerBins(lBins);
        ;
        uBins = null;
        lBins = null;
        if (debug) System.out.println("\tInstantiation Delay: " + (System.currentTimeMillis() - start));
        processing = false;
    }

    /**
     * A method which returns a two dimentional array as specified by
     * the getResult() method which contains the best set of matches
     * between given structures. <BR><BR>
     * 
     * This class uses the MatchTwo algorithm defined by 
     * SolidMatrix Technologies, Inc.
     *
     * @see smti.util.shared.Matcher#getResult()
     */
    public int[][] match() {
        if (matched) return result;
        processing = true;
        int[][] uPoint = getDefaultPointArray(uBinArray);
        int[][] lPoint = getDefaultPointArray(lBinArray);
        matchTwo(uPoint, lPoint, 0, 0, in1.length, in2.length);
        result = outputLists();
        resetLists();
        processing = false;
        return result;
    }

    /**
	 * Recursive implemintation for the MatchTwo algorithm as defined
     * by SolidMatrix Technologies, Inc. <BR><BR>
     *
     * The structures matched are those that were given during
     * the instantiation of this class. The results are stored to 
     * the three linked lists defined by the super class (Matcher).
     * <BR><BR>
     *
     * The first call to this method should be in the following form:<BR>
     * matchTwo(default point array for upper bin list,<BR>
     *          default point array for lower bin list,<BR>
     *          0, 0, <BR>
     *          length of first input array,<BR>
     *          length of second input array)<BR>
	 */
    private void matchTwo(int[][] uPoint, int[][] lPoint, int start1, int start2, int end1, int end2) {
        long start = System.currentTimeMillis();
        int bfor = 0;
        int bsec = 0;
        int weight = 0;
        boolean fast = needsFastAlgo(end1 - start1, end2 - start2);
        int center1 = (end1 + start1) / 2;
        int center2 = (end2 + start2) / 2;
        int offset = center1 + center2;
        for (int i = 0; i < uBinArray.length && processing; i++) {
            int s1 = uPoint[i][0];
            int s2 = lPoint[i][0];
            int e1 = uPoint[i][1];
            int e2 = lPoint[i][1];
            if (fast) {
                while (s1 < e1 && s2 < e2 && processing) {
                    int i1 = uBinArray[i][s1];
                    int i2 = lBinArray[i][s2];
                    s1++;
                    s2++;
                    int mw = 1;
                    int k = i1 + 1;
                    int l = i2 + 1;
                    while (k < end1 && l < end2 && !upperDelim[k] && !lowerDelim[l] && in1[k] == in2[l] && processing) {
                        mw++;
                        k++;
                        l++;
                    }
                    k = i1 - 1;
                    l = i2 - 1;
                    while (k >= start1 && l >= start2 && !upperDelim[k] && !lowerDelim[l] && in1[k] == in2[l] && processing) {
                        mw++;
                        k--;
                        l--;
                    }
                    i1 = k + 1;
                    i2 = l + 1;
                    if (mw >= weight && mw >= ignoreWeight) {
                        int coffset = Math.abs(center1 - i1) + Math.abs(center2 - i2);
                        if (mw > weight || coffset < offset) {
                            bfor = i1;
                            bsec = i2;
                            weight = mw;
                            offset = coffset;
                        }
                    }
                }
            } else {
                for (int j = s1; j < e1 && processing; j++) {
                    int i1 = uBinArray[i][j];
                    for (int k = s2; k < e2 && processing; k++) {
                        int i2 = lBinArray[i][k];
                        int mw = 1;
                        int l = i1 + 1;
                        int m = i2 + 1;
                        while (l < end1 && m < end2 && !upperDelim[l] && !lowerDelim[m] && in1[l] == in2[m] && processing) {
                            mw++;
                            l++;
                            m++;
                        }
                        if (mw >= weight && mw >= ignoreWeight) {
                            int coffset = Math.abs(center1 - i1) + Math.abs(center2 - i2);
                            if (mw > weight || coffset < offset) {
                                bfor = i1;
                                bsec = i2;
                                weight = mw;
                                offset = coffset;
                            }
                        }
                    }
                }
            }
        }
        if (weight != 0 && processing) {
            int comp1 = bfor + weight;
            int comp2 = bsec + weight;
            int[][] uPointFront = new int[uPoint.length][2];
            int[][] lPointFront = new int[lPoint.length][2];
            int[][] uPointBack = new int[uPoint.length][2];
            int[][] lPointBack = new int[lPoint.length][2];
            trimPointArray(uPoint, uPointFront, uPointBack, uBinArray, bfor, comp1);
            trimPointArray(lPoint, lPointFront, lPointBack, lBinArray, bsec, comp2);
            uPoint = null;
            lPoint = null;
            if (bfor > start1 && bsec > start2 && processing) matchTwo(uPointFront, lPointFront, start1, start2, bfor, bsec);
            uPointFront = null;
            lPointFront = null;
            firstMatch.add(new Integer(bfor));
            secondMatch.add(new Integer(bsec));
            weights.add(new Integer(weight));
            if (comp1 < end1 && comp2 < end2 && processing) matchTwo(uPointBack, lPointBack, comp1, comp2, end1, end2);
            uPointBack = null;
            lPointBack = null;
        }
        if (debug) System.out.println("\tMatchTwo Delay(" + (end1 - start1 > end2 - start2 ? end1 - start1 : end2 - start2) + "): " + (System.currentTimeMillis() - start));
    }
}
