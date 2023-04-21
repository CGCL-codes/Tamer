package org.proteomecommons.MSExpedite.SignalProcessing;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;

public class PeakFinder {

    ArrayList al = new ArrayList();

    float x[], y[];

    IndexAssociation centroid[];

    public PeakFinder() {
    }

    public PeakFinder(float x[], float y[]) {
        setData(x, y);
    }

    public void setData(float x[], float y[]) {
        this.x = x;
        this.y = y;
    }

    public static final int getPeakIndex(final float x[], float y[]) {
        double fPeak = -1;
        int ri = 0;
        if (x == null || x.length == 0) return -1;
        for (int i = 0; i < x.length; i++) {
            if (y[i] > fPeak) {
                fPeak = x[i];
                ri = i;
            }
        }
        return ri;
    }

    private final void buildTree() {
        int start = 0;
        int end = x.length;
        int parentIndex = findPeak(start, end);
        if (parentIndex == -1) return;
        if (x[parentIndex] != Float.NaN) al.add(new Integer(parentIndex));
        int ileft = findLeftEdge(parentIndex);
        int iright = findRightEdge(parentIndex);
        findNodes(ileft, start, iright, end);
    }

    private final void findNodes(int leftEdge, int leftToIndex, int rightEdge, int rightToIndex) {
        int iPeak = findPeak(leftToIndex, leftEdge);
        int inewleft, inewright;
        if (iPeak > 0) {
            if (x[iPeak] != Float.NaN) al.add(new Integer(iPeak));
            inewleft = findLeftEdge(iPeak);
            inewright = findRightEdge(iPeak);
            if (inewleft != leftEdge) findNodes(inewleft, leftToIndex, inewright, leftEdge);
        }
        iPeak = findPeak(rightEdge, rightToIndex);
        if (iPeak < 0) return;
        if (x[iPeak] != Float.NaN) al.add(new Integer(iPeak));
        inewleft = findLeftEdge(iPeak);
        inewright = findRightEdge(iPeak);
        if (inewright != rightEdge) findNodes(inewleft, rightEdge, inewright, rightToIndex);
    }

    public final void reset() {
        al.clear();
    }

    public final int getPeakIndex(final float m) {
        return Arrays.binarySearch(x, m);
    }

    public final int[] getPeaks(final float sn, final int iWin, final float stopIntensity, final boolean deisotope) {
        if (sn == 0) return null;
        buildTree();
        final int size = al.size();
        int peakIndex;
        double noise = 0.0f;
        Integer dp[] = (Integer[]) al.toArray(new Integer[0]);
        dp = sortPeakList(dp);
        ArrayList passedPeaks = new ArrayList();
        int offset = 0;
        int div = 2 * iWin;
        for (int i = 0; i < dp.length; i++) {
            peakIndex = dp[i].intValue();
            if (peakIndex < iWin) {
                offset = 0;
            } else if ((peakIndex + iWin) > x.length) {
                offset = peakIndex - iWin - Math.abs(x.length - peakIndex - iWin);
            } else {
                offset = peakIndex - iWin;
            }
            float[] curve = new float[div];
            curve = arraycopy(y, offset, curve, div);
            try {
                noise = Noise.calculate(curve);
            } catch (UnableToConvergeException ucEx) {
                ucEx.printStackTrace();
            }
            if (y[peakIndex] / noise > sn) passedPeaks.add(dp[i]);
        }
        if (!deisotope) return intValue((Integer[]) passedPeaks.toArray(new Integer[0]));
        Integer[] rp = (Integer[]) passedPeaks.toArray(new Integer[0]);
        rp = deisotop(rp, 0.95f, 1.05f);
        return intValue(rp);
    }

    public static int[] intValue(Integer[] intArray) {
        int[] ri = new int[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            ri[i] = intArray[i].intValue();
        }
        return ri;
    }

    float[] arraycopy(float[] source, int start, float[] dest, int length) {
        int end = start + length;
        int j = 0;
        for (int i = start; i < end; i++) {
            dest[j++] = source[i];
        }
        return dest;
    }

    public final int[] getPeaks(final float intensity, final boolean deisotope) {
        if (intensity == 0) return null;
        buildTree();
        final int size = al.size();
        int peakIndex;
        float noise;
        Integer dp[] = (Integer[]) al.toArray(new Integer[0]);
        dp = sortPeakList(dp);
        ArrayList passedPeaks = new ArrayList();
        int offset = 0;
        float centroid;
        for (int i = 0; i < dp.length; i++) {
            if (y[dp[i].intValue()] >= intensity) {
                peakIndex = dp[i].intValue();
                passedPeaks.add(dp[i]);
            }
        }
        if (!deisotope) return intValue((Integer[]) passedPeaks.toArray(new Integer[0]));
        return intValue(deisotop((Integer[]) passedPeaks.toArray(new Integer[0]), 0.93f, 1.07f));
    }

    public final int[] getPeaks(final float sn, final int iWin, final float maxIntensity, final float maxIntensityPercent, final boolean deisotope) {
        if (sn == 0) return null;
        buildTree();
        final int size = al.size();
        int peakIndex;
        float noise = 0.0f;
        ;
        Integer dp[] = (Integer[]) al.toArray(new Integer[0]);
        dp = sortPeakList(dp);
        ArrayList passedPeaks = new ArrayList();
        int offset = 0;
        int div = 2 * iWin;
        float centroid;
        for (int i = 0; i < dp.length; i++) {
            peakIndex = dp[i].intValue();
            if (peakIndex < iWin) {
                offset = 0;
            } else if ((peakIndex + iWin) > x.length) {
                offset = peakIndex - iWin - Math.abs(x.length - peakIndex - iWin);
            } else {
                offset = peakIndex - iWin;
            }
            float[] curve = new float[div];
            curve = arraycopy(y, offset, curve, div);
            try {
                noise = Noise.calculate(curve);
            } catch (UnableToConvergeException ucEx) {
                ucEx.printStackTrace();
            }
            if (y[dp[i].intValue()] / noise >= sn) {
                passedPeaks.add(dp[i]);
            }
        }
        if (!deisotope) return intValue((Integer[]) passedPeaks.toArray(new Integer[0]));
        return intValue(deisotop((Integer[]) passedPeaks.toArray(new Integer[0]), 0.93f, 1.07f));
    }

    public final int[] getPeaks(final float sn, final int win, boolean deisotope) {
        return getPeaks(sn, win, 0, deisotope);
    }

    public final ArrayList binPeaks(final Integer[] dp, final float d) {
        final ArrayList rl = new ArrayList();
        for (int i = 0; i < dp.length; i++) {
            ArrayList al = new ArrayList();
            IndexAssociation ia = new IndexAssociation(i, new Boolean(false));
            for (int j = i + 1; j < dp.length; j++) {
                if (Math.abs(x[i] - x[j]) > 10) break;
                if (Math.abs(x[i] - x[j]) <= d) {
                    IndexAssociation ia2 = new IndexAssociation(j, new Boolean(true));
                }
            }
            final IndexAssociation sp[] = (IndexAssociation[]) al.toArray(new IndexAssociation[0]);
            Arrays.sort(sp, new Comparator() {

                public int compare(Object sp1, Object sp2) {
                    IndexAssociation a = (IndexAssociation) sp1;
                    IndexAssociation b = (IndexAssociation) sp2;
                    if (x[a.index] < x[b.index]) {
                        return -1;
                    } else if (x[a.index] == x[b.index]) {
                        return 0;
                    } else if (x[a.index] > x[b.index]) {
                        return 1;
                    }
                    return -2;
                }
            });
            al.clear();
            al = add(al = new ArrayList(sp.length), sp);
            rl.add(al);
        }
        return rl;
    }

    ArrayList add(ArrayList al, IndexAssociation[] ia) {
        for (int i = 0; i < ia.length; i++) {
            al.add(ia);
        }
        return al;
    }

    public ArrayList binPeaks(Integer peaks[], float dmLow, float dmMax) {
        final ArrayList rl = new ArrayList();
        final float mergeCutoff = 0.2f;
        int pos = 0;
        float cent_i, cent_j;
        IndexAssociation dp[] = IndexAssociation.construct(peaks, new Boolean(false));
        Boolean tmpBool = null;
        for (int i = 0; i < dp.length; i++) {
            tmpBool = (Boolean) dp[i].association;
            if (!tmpBool.booleanValue()) {
                ArrayList al = new ArrayList();
                al.add(dp[i]);
                pos = dp[i].index;
                for (int j = i + 1; j < dp.length; j++) {
                    if (Math.abs(x[pos] - x[dp[j].index]) > 20) break;
                    boolean b = ((Boolean) dp[j].association).booleanValue();
                    cent_j = centroid[j].index;
                    float mj = ((Float) centroid[j].association).floatValue();
                    float mj1 = ((Float) centroid[j - 1].association).floatValue();
                    float dm = Math.abs(mj - mj1);
                    if (!b && dm >= dmLow && dm <= dmMax) {
                        al.add(dp[j]);
                        dp[j].association = new Boolean(true);
                    } else if (!b && dm > dmMax) break;
                }
                rl.add(al);
            }
        }
        return rl;
    }

    public final Integer[] deisotop(final Integer[] dp, final float dmLow, final float dmMax) {
        final ArrayList al = binPeaks(dp, dmLow, dmMax);
        ArrayList sl = new ArrayList();
        final int size = al.size();
        ArrayList tmpSL = null;
        ArrayList l = new ArrayList();
        for (int i = 0; i < size; i++) {
            tmpSL = (ArrayList) (al.get(i));
            l.add("===================");
            l.add("Bin = " + i);
            if (tmpSL.size() > 1) sl.add(tmpSL.get(0));
            for (int j = 0; j < tmpSL.size(); j++) {
                l.add(tmpSL.get(j).toString());
            }
        }
        return (Integer[]) sl.toArray(new Integer[0]);
    }

    public final Integer[] deisotop(final Integer dp[], final float d) {
        final ArrayList al = binPeaks(dp, d);
        final ArrayList sl = new ArrayList();
        final int size = al.size();
        ArrayList tmpSL = null;
        for (int i = 0; i < size; i++) {
            tmpSL = (ArrayList) (al.get(i));
            if (tmpSL.size() > 1) sl.add(tmpSL.get(0));
        }
        return (Integer[]) sl.toArray(new Integer[0]);
    }

    public final int[] getPeaks(final int numPeaksToReturn) {
        if (numPeaksToReturn <= 0 || numPeaksToReturn > x.length) return null;
        buildTree();
        int size = al.size();
        Integer peaks[] = new Integer[size];
        System.arraycopy((Integer[]) al.toArray(new Integer[0]), 0, peaks, 0, size);
        if (numPeaksToReturn == x.length) return intValue(peaks);
        int rp[] = new int[numPeaksToReturn];
        peaks = sortPeakList(peaks);
        for (int i = peaks.length - size; i < peaks.length; i++) {
            rp[i - (peaks.length - size)] = peaks[i].intValue();
        }
        return rp;
    }

    public final int[] getPeaks() {
        return getPeaks(x.length);
    }

    private int findPeak(int start, int end) {
        double counts = -1;
        int index = -1;
        if (start < 0 || end < 0) return -1;
        for (int i = start; i < end; i++) {
            if (counts < y[i]) {
                counts = y[i];
                index = i;
            }
        }
        return index;
    }

    private int findLeftEdge(int index) {
        if (index < 0) return -1;
        double counts = y[index];
        int ri = -1;
        for (int i = index; i > 0; i--) {
            if (counts >= y[i]) {
                counts = y[i];
                ri = i;
            } else break;
        }
        return ri;
    }

    int findRightEdge(int index) {
        if (index < 0) return -1;
        double counts = y[index];
        int ri = -1;
        for (int i = index; i < y.length; i++) {
            if (counts >= y[i]) {
                counts = y[i];
                ri = i;
            } else break;
        }
        return ri;
    }

    private final Integer[] sortPeakList(Integer[] peakArray) {
        Arrays.sort(peakArray, new Comparator() {

            public int compare(Object sp1, Object sp2) {
                Integer a = (Integer) sp1;
                Integer b = (Integer) sp2;
                if (a.intValue() < b.intValue()) {
                    return -1;
                } else if (a.intValue() == b.intValue()) {
                    return 0;
                } else if (a.intValue() > b.intValue()) {
                    return 1;
                }
                return -2;
            }
        });
        return peakArray;
    }
}
