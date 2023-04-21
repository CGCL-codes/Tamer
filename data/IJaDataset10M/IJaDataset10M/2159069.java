package org.stumeikle.NeuroCoSA.AutoCluster;

import java.util.*;

public class AutoClusterMemoryControl {

    private int iNumBins;

    private int iMaxNumBins;

    private long iTotalNumEntries;

    private LinkedList<AutoClusterMemory> iBins;

    private LinkedList<AutoClusterMemory> iConsolBins;

    private double iSizeOfNewBins;

    private AutoClusterMemory iScrapBin;

    private Random iRand;

    private long iCurrentTime;

    private long iNextId;

    private long iMaxNumInScrapBin;

    private long iNoiseLifeTime;

    private AutoClusterNeuroCoSAIF iOutsideWorld;

    public AutoClusterMemoryControl(int max_bins, double size_of_new_bins, long max_num_in_scrap_bin, long lifetime_of_noise, AutoClusterNeuroCoSAIF nif) {
        iNumBins = 0;
        iMaxNumBins = max_bins;
        iTotalNumEntries = 0;
        iScrapBin = null;
        iBins = new LinkedList<AutoClusterMemory>();
        iConsolBins = new LinkedList<AutoClusterMemory>();
        iRand = new Random();
        iSizeOfNewBins = size_of_new_bins;
        iCurrentTime = 1;
        iNextId = 1;
        iMaxNumInScrapBin = max_num_in_scrap_bin;
        iNoiseLifeTime = lifetime_of_noise;
        iOutsideWorld = nif;
    }

    public AutoClusterMemory getScrapBin() {
        return iScrapBin;
    }

    public LinkedList getBins() {
        return iBins;
    }

    public LinkedList getConsolidatedBins() {
        return iConsolBins;
    }

    public int getNumBins() {
        return iNumBins;
    }

    public int getMaxNumBins() {
        return iMaxNumBins;
    }

    public void setMaxNumBins(int n) {
        iMaxNumBins = n;
    }

    public void removeBin(AutoClusterMemory b) {
        iBins.remove(b);
        b = null;
        iNumBins--;
    }

    public void addValue(NMeasurable t, long curr_time) {
        iCurrentTime = curr_time;
        storeValue(t, false, iNumBins);
        lookForMelds();
        checkIfMoreBinsNeeded();
        checkForBinSplits();
        checkForNoiseBins();
        checkForUnchangingBins();
    }

    public void testValue(NMeasurable t, long test_time) {
        ListIterator i = iBins.listIterator(0);
        if (t == null) return;
        for (; i.hasNext(); ) {
            AutoClusterMemory mem = (AutoClusterMemory) i.next();
            if (mem.getIgnore() == true) continue;
            double d;
            NMeasurable nt = mem.getCentroid();
            d = t.distanceTo(nt);
            mem.storeTestDistance(d, test_time);
        }
        i = iConsolBins.listIterator(0);
        for (; i.hasNext(); ) {
            AutoClusterMemory mem = (AutoClusterMemory) i.next();
            if (mem.getIgnore()) continue;
            double d;
            NMeasurable nt = mem.getCentroid();
            d = t.distanceTo(nt);
            mem.storeTestDistance(d, test_time);
        }
    }

    public void storeValue(NMeasurable t, boolean afterbreak, int n) {
        boolean inside = false;
        double min_distance;
        double min_outside_distance;
        AutoClusterMemory closest = null;
        AutoClusterMemory closest_out = null;
        min_distance = min_outside_distance = -1.0;
        ListIterator i = iBins.listIterator(0);
        for (; i.hasNext(); ) {
            AutoClusterMemory mem = (AutoClusterMemory) i.next();
            mem.storeDistance(AutoClusterMemory.UNDEFINED_DISTANCE, iCurrentTime);
            if (mem.getIgnore() == true) continue;
            double d;
            NMeasurable nt = mem.getCentroid();
            d = t.distanceTo(nt);
            if (d < mem.getSize()) {
                inside = true;
                if (d < min_distance || min_distance == -1.0) {
                    min_distance = d;
                    closest = mem;
                }
            } else if (d < min_outside_distance || min_outside_distance == -1.0) {
                min_outside_distance = d;
                closest_out = mem;
            }
        }
        i = iConsolBins.listIterator(0);
        for (; i.hasNext(); ) {
            AutoClusterMemory mem = (AutoClusterMemory) i.next();
            mem.storeDistance(AutoClusterMemory.UNDEFINED_DISTANCE, iCurrentTime);
            if (mem.getIgnore()) continue;
            double d;
            NMeasurable nt = mem.getCentroid();
            d = t.distanceTo(nt);
            if (d < mem.getSize()) {
                inside = true;
                if (d < min_distance || min_distance == -1.0) {
                    min_distance = d;
                    closest = mem;
                }
            } else if (d < min_outside_distance || min_outside_distance == -1.0) {
                min_outside_distance = d;
                closest_out = mem;
            }
        }
        if (closest != null) closest.storeDistance(min_distance, iCurrentTime);
        if (inside == true) {
            addCT2Bin(t, closest);
        } else {
            if (n < iMaxNumBins) {
                if (min_outside_distance == -1) min_outside_distance = iSizeOfNewBins;
                if (!afterbreak) createNewBin(t, min_outside_distance); else createNewBin(t, iSizeOfNewBins);
            } else {
                if (iScrapBin == null) {
                    AutoClusterMemory mem = new AutoClusterMemory(t, Double.MAX_VALUE, iCurrentTime, iNextId++);
                    iScrapBin = mem;
                    addCT2Bin(t, mem);
                } else {
                    addCT2Bin(t, iScrapBin);
                }
            }
        }
    }

    public void createNewBin(NMeasurable t, double s) {
        if (iOutsideWorld.requestNewACM() == false) return;
        if (t == null) return;
        if (s > iSizeOfNewBins) s = iSizeOfNewBins;
        NMeasurable tb = t.createCopy();
        AutoClusterMemory hb = new AutoClusterMemory(tb, s, iCurrentTime, iNextId++);
        hb.storeDistance(0.0, iCurrentTime);
        addCT2Bin(t, hb);
        iNumBins++;
        iBins.add(hb);
        iOutsideWorld.setNewACM(hb);
    }

    public void lookForMelds() {
        double n1 = iRand.nextDouble() * (double) iNumBins;
        double n2 = iRand.nextDouble() * (double) iNumBins;
        if (iNumBins == 0) return;
        if ((int) n1 == (int) n2) return;
        AutoClusterMemory h1, h2;
        ListIterator i;
        int j;
        h1 = h2 = null;
        j = 0;
        i = iBins.listIterator(0);
        for (j = 0; i.hasNext(); j++) {
            AutoClusterMemory hb = (AutoClusterMemory) i.next();
            if (j == (int) n1) h1 = hb;
            if (j == (int) n2) h2 = hb;
        }
        if ((h1.getSize() + h2.getSize()) > h1.getCentroid().distanceTo(h2.getCentroid())) {
            checkForMeld(h1, h2);
            checkForMeld(h2, h1);
        }
    }

    public void checkForMeld(AutoClusterMemory hb1, AutoClusterMemory hb2) {
        if (hb1.isConsolidated() == true && hb2.isConsolidated() == true) {
            return;
        }
        int arraysize = 10;
        double hough[];
        double maxr[];
        int i;
        int c1max = 0, c2min = arraysize - 1;
        long total = 0;
        hough = new double[arraysize];
        maxr = new double[arraysize];
        for (i = 0; i < arraysize; ++i) {
            hough[i] = 0.0;
            maxr[i] = -1.0;
        }
        double cmag, vmag;
        double xscale;
        NMeasurable hb1_cen, hb2_cen;
        NMeasurable c_vec, v_vec;
        hb1_cen = hb1.getCentroid();
        hb2_cen = hb2.getCentroid();
        c_vec = hb2_cen.createCopy();
        c_vec.subtract(hb1_cen);
        cmag = c_vec.magnitude();
        if (cmag < 0.001) return;
        xscale = arraysize / cmag;
        ListIterator it;
        it = hb1.getEntryList().listIterator(0);
        for (; it.hasNext(); ) {
            NMeasurable c = (NMeasurable) it.next();
            double x, y;
            int ix;
            v_vec = c.createCopy();
            v_vec.subtract(hb1_cen);
            x = v_vec.dotProduct(c_vec) / cmag;
            vmag = v_vec.magnitude();
            y = Math.sqrt(vmag * vmag - x * x);
            x *= xscale;
            ix = (int) Math.floor(x);
            if (ix >= 0 && ix < arraysize) {
                hough[ix] += 1.0;
                total++;
                if (ix > c1max) c1max = ix;
                if (y > maxr[ix] || maxr[ix] == -1.0) maxr[ix] = y;
            }
        }
        it = hb2.getEntryList().listIterator(0);
        for (; it.hasNext(); ) {
            NMeasurable c = (NMeasurable) it.next();
            double x, y;
            int ix;
            v_vec = c.createCopy();
            v_vec.subtract(hb1_cen);
            x = v_vec.dotProduct(c_vec) / cmag;
            vmag = v_vec.magnitude();
            y = Math.sqrt(vmag * vmag - x * x);
            x *= xscale;
            ix = (int) Math.floor(x);
            if (ix >= 0 && ix < arraysize) {
                hough[ix] += 1.0;
                total++;
                if (ix < c2min) c2min = ix;
                if (y > maxr[ix] || maxr[ix] == -1.0) maxr[ix] = y;
            }
        }
        if (total < 10) return;
        for (i = 0; i < arraysize; i++) {
            if (maxr[i] > 0.0) hough[i] /= maxr[i]; else hough[i] = 0.0;
        }
        boolean monotonic = true;
        for (i = 1; i <= c1max; i++) {
            double d = hough[i] - hough[i - 1];
            if (d > 0) continue;
            monotonic = false;
            break;
        }
        if (monotonic == true) {
            mergeBins(hb2, hb1);
        }
    }

    public void checkIfMoreBinsNeeded() {
        if (iScrapBin == null) return;
        if (iNumBins == 0) return;
        long randbin = (long) (iRand.nextDouble() * (double) iScrapBin.getEntryList().size());
        long bincount = 0;
        double nn_dist = -1.0;
        AutoClusterMemory nn;
        NMeasurable chosen = null;
        ListIterator i;
        i = iScrapBin.getEntryList().listIterator(0);
        for (bincount = 0; bincount < randbin; bincount++) {
            i.next();
        }
        if (i.hasNext()) chosen = (NMeasurable) i.next();
        if (chosen == null) return;
        bincount = 0;
        i = iScrapBin.getEntryList().listIterator(0);
        for (; i.hasNext(); ) {
            NMeasurable c = (NMeasurable) i.next();
            if (c == chosen) continue;
            double d = c.distanceTo(chosen);
            if (d < iSizeOfNewBins) bincount++;
        }
        ListIterator j;
        nn = null;
        j = iBins.listIterator(0);
        for (; j.hasNext(); ) {
            AutoClusterMemory hb = (AutoClusterMemory) j.next();
            double d = hb.getCentroid().distanceTo(chosen);
            if (hb.isConsolidated() == true) continue;
            if (nn == null) {
                nn = hb;
                nn_dist = d;
            } else {
                if (d < nn_dist) {
                    nn = hb;
                    nn_dist = d;
                }
            }
        }
        if (nn == null) return;
        long nncount = nn.getNumEntries();
        if (nncount > iMaxNumInScrapBin) nncount = iMaxNumInScrapBin;
        if ((double) bincount > 0.75 * (double) nncount) {
            if (iNumBins < iMaxNumBins) {
                if (iOutsideWorld.requestNewACM() == true) {
                    AutoClusterMemory hb = new AutoClusterMemory(chosen, iSizeOfNewBins, iCurrentTime, iNextId++);
                    iNumBins++;
                    iBins.add(hb);
                    hb.storeDistance(0.0, iCurrentTime);
                    iOutsideWorld.setNewACM(hb);
                    i = iScrapBin.getEntryList().listIterator(0);
                    for (; i.hasNext(); ) {
                        NMeasurable c = (NMeasurable) i.next();
                        double d = c.distanceTo(chosen);
                        if (d < iSizeOfNewBins) {
                            addCT2Bin(c.createCopy(), hb);
                            i.remove();
                        }
                    }
                }
            } else {
                if (nn != null) {
                    nn.setSize(nn_dist + iSizeOfNewBins);
                    i = iScrapBin.getEntryList().listIterator(0);
                    for (; i.hasNext(); ) {
                        NMeasurable c = (NMeasurable) i.next();
                        double d = c.distanceTo(nn.getCentroid());
                        if (d < nn.getSize()) {
                            addCT2Bin(c.createCopy(), nn);
                            i.remove();
                        }
                    }
                } else {
                    ;
                }
            }
        }
    }

    public void mergeBins(AutoClusterMemory primary, AutoClusterMemory secondary) {
        if (iOutsideWorld.mergeACMs(primary, secondary) == false) return;
        ListIterator i;
        double maxd = 0, d;
        i = secondary.getEntryList().listIterator(0);
        for (; i.hasNext(); ) {
            NMeasurable c = (NMeasurable) i.next();
            d = c.distanceTo(primary.getCentroid());
            if (maxd == 0 || d > maxd) {
                maxd = d;
            }
        }
        primary.setSize(maxd);
        secondary.setIgnore(true);
        LinkedList e;
        e = secondary.getEntryList();
        int m = iMaxNumBins;
        iMaxNumBins = iNumBins - 1;
        i = e.listIterator(0);
        for (; i.hasNext(); ) {
            NMeasurable c = (NMeasurable) i.next();
            storeValue(c, true, iNumBins - 1);
        }
        iMaxNumBins = m;
        ListIterator j;
        j = iBins.listIterator(0);
        for (; j.hasNext(); ) {
            AutoClusterMemory hb = (AutoClusterMemory) j.next();
            if (hb == secondary) {
                if (iOutsideWorld.requestACMRemoval(hb)) {
                    j.remove();
                    iNumBins--;
                    iOutsideWorld.removeACM(hb);
                }
                break;
            }
        }
    }

    public void checkForBinSplits() {
        if (iNumBins == 0) return;
        AutoClusterMemory chosen = null;
        int i, j, n = (int) (iRand.nextDouble() * (double) iNumBins);
        ListIterator hbit;
        ListIterator cbit;
        hbit = iBins.listIterator(0);
        i = 0;
        for (; hbit.hasNext(); i++) {
            AutoClusterMemory c = (AutoClusterMemory) hbit.next();
            if (i == n) {
                chosen = c;
                break;
            }
        }
        if (chosen != null) {
            if (chosen.isConsolidated()) return;
        }
        double num_outside = 0;
        double num_inside = 0;
        cbit = chosen.getEntryList().listIterator(0);
        for (; cbit.hasNext(); ) {
            NMeasurable c = (NMeasurable) cbit.next();
            if (c.distanceTo(chosen.getCentroid()) < iSizeOfNewBins) num_inside += 1.0; else num_outside += 1.0;
        }
        if (num_inside < 0.1 * num_outside || num_inside > 0.85 * num_outside) ; else {
            chosen.incrNeedsToSplitCount();
            if (iOutsideWorld.requestACMRemoval(chosen)) {
                if (iNumBins < iMaxNumBins) {
                    LinkedList entries = chosen.getEntryList();
                    chosen.setIgnore(true);
                    int m = iMaxNumBins;
                    iMaxNumBins = iNumBins - 1;
                    cbit = entries.listIterator(0);
                    for (; cbit.hasNext(); ) {
                        NMeasurable c = (NMeasurable) cbit.next();
                        NMeasurable c2;
                        c2 = c.createCopy();
                        storeValue(c2, true, iNumBins - 1);
                    }
                    iMaxNumBins = m;
                    iOutsideWorld.removeACM(chosen);
                    hbit.remove();
                    iNumBins--;
                }
            }
        }
    }

    public void checkForNoiseBins() {
        if (iNumBins == 0) return;
        ListIterator hbit, nexthbit;
        hbit = iBins.listIterator(0);
        for (; hbit.hasNext(); ) {
            AutoClusterMemory hb = (AutoClusterMemory) hbit.next();
            if (hb.getNumEntries() == 1 && iCurrentTime - hb.getCreateTime() > iNoiseLifeTime) {
                if (iOutsideWorld.requestACMRemoval(hb)) {
                    iOutsideWorld.removeACM(hb);
                    hbit.remove();
                    iNumBins--;
                }
            }
        }
    }

    public void checkForUnchangingBins() {
        if (iNumBins == 0) return;
        AutoClusterMemory chosen = null;
        int i, j, n = (int) (iRand.nextDouble() * (double) iNumBins);
        ListIterator hbit;
        ListIterator cit;
        hbit = iBins.listIterator(0);
        for (i = 0; hbit.hasNext(); i++) {
            AutoClusterMemory acm = (AutoClusterMemory) hbit.next();
            if (i == n) {
                chosen = acm;
                break;
            }
        }
        if (chosen != null && chosen.isConsolidated() == false) {
            double cc, ss;
            cc = chosen.getCentroidChangeRatio();
            ss = chosen.getSizeChangeRatio();
            if (cc < 1.0 && ss < 1.0 && chosen.getNeedsToSplitCount() < 2) {
                chosen.consolidate();
                iBins.remove(chosen);
                iConsolBins.add(chosen);
                iNumBins--;
            }
        }
    }

    public void addCT2Bin(NMeasurable c, AutoClusterMemory hb) {
        long s = iMaxNumInScrapBin;
        if (hb == iScrapBin) s *= 10;
        hb.addEntry(c);
        if (hb.getEntryList().size() > s) {
            hb.removeFirstEntry();
        }
    }
}
