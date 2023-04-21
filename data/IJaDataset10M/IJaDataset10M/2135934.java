package net.sf.metaprint2d.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.metaprint2d.Fingerprint;
import net.sf.metaprint2d.Fingerprint.FPLevel;

public class InMemoryDataSource<FPData extends Fingerprint> {

    private HashMap<FPLevel, HashSet<FPData>> level0Index = new HashMap<FPLevel, HashSet<FPData>>();

    private HashMap<FPLevel, HashSet<FPData>> level1Index = new HashMap<FPLevel, HashSet<FPData>>();

    private HashMap<FPLevel, HashSet<FPData>> level2Index = new HashMap<FPLevel, HashSet<FPData>>();

    private ArrayList<FPData> fingerprints = new ArrayList<FPData>();

    public InMemoryDataSource(Collection<? extends FPData> data) {
        fingerprints.ensureCapacity(data.size());
        for (FPData fp : data) {
            addFingerprint(fp);
        }
    }

    protected InMemoryDataSource() {
        super();
    }

    protected void addFingerprint(FPData fp) {
        HashSet<FPData> l0fps = level0Index.get(fp.getLevel(0));
        if (l0fps == null) {
            l0fps = new HashSet<FPData>();
            level0Index.put(fp.getLevel(0), l0fps);
        }
        l0fps.add(fp);
        HashSet<FPData> l1fps = level1Index.get(fp.getLevel(1));
        if (l1fps == null) {
            l1fps = new HashSet<FPData>();
            level1Index.put(fp.getLevel(1), l1fps);
        }
        l1fps.add(fp);
        HashSet<FPData> l2fps = level2Index.get(fp.getLevel(2));
        if (l2fps == null) {
            l2fps = new HashSet<FPData>();
            level2Index.put(fp.getLevel(2), l2fps);
        }
        l2fps.add(fp);
        fingerprints.add(fp);
    }

    protected void ensureCapacity(int numFingerprints) {
        fingerprints.ensureCapacity(numFingerprints);
    }

    public int getFingerprintCount() {
        return fingerprints.size();
    }

    public List<FPData> getData() {
        return new ArrayList<FPData>(fingerprints);
    }

    public Map<Fingerprint, List<FPData>> getFingerprintData(List<Fingerprint> fps, int levels) {
        Map<Fingerprint, List<FPData>> map = new HashMap<Fingerprint, List<FPData>>();
        for (Fingerprint fp : fps) {
            map.put(fp, getFingerprintData(fp, levels));
        }
        return map;
    }

    /**
     * Returns list for FingerprintData objects where matching fp for number of
     * exact levels.
     * @param fp
     * @param exactLevels
     * @return
     */
    public List<FPData> getFingerprintData(Fingerprint fp, int exactLevels) {
        FPLevel[] fps = new FPLevel[exactLevels];
        for (int i = 0; i < exactLevels; i++) {
            fps[i] = fp.getLevel(i);
        }
        return getFingerprintData(fps);
    }

    /**
     * Returns list for FingerprintData objects where first n levels exactly
     * match specified fingerprint.
     * @param fps
     * @return
     */
    public List<FPData> getFingerprintData(FPLevel... fps) {
        if (fps == null) {
            throw new NullPointerException();
        }
        int numLevels = fps.length;
        if (numLevels == 0) {
            return new ArrayList<FPData>(fingerprints);
        }
        for (FPLevel fp : fps) {
            if (fp == null) {
                throw new NullPointerException();
            }
        }
        List<FPData> result;
        if (numLevels == 1) {
            result = findLevel0(fps[0]);
        } else if (numLevels == 2) {
            result = findLevel1(fps[0], fps[1]);
        } else {
            result = findLevel2(fps[0], fps[1], fps[2]);
            if (numLevels > 3 && result.size() > 0) {
                List<FPData> list = new ArrayList<FPData>();
                fp: for (FPData fp : result) {
                    for (int i = 3; i < numLevels; i++) {
                        if (!fp.getLevel(i).equals(fps[i])) {
                            continue fp;
                        }
                    }
                    list.add(fp);
                }
                result = list;
            }
        }
        return result;
    }

    /**
     * Get list of fingerprints matching query at level 0.
     * @param fp0
     * @return
     */
    private List<FPData> findLevel0(FPLevel fp0) {
        Set<FPData> s0 = level0Index.get(fp0);
        if (s0 != null) {
            return new ArrayList<FPData>(s0);
        } else {
            return new ArrayList<FPData>(0);
        }
    }

    /**
     * Get list of fingerprints matching query at levels 0 and 1.
     * @param fp0
     * @param fp1
     * @return
     */
    private List<FPData> findLevel1(FPLevel fp0, FPLevel fp1) {
        Set<FPData> s0 = level0Index.get(fp0);
        Set<FPData> s1 = level1Index.get(fp1);
        if (s0 != null && s1 != null) {
            if (s0.size() > s1.size()) {
                Set<FPData> s = s1;
                s1 = s0;
                s0 = s;
            }
            List<FPData> result = new ArrayList<FPData>();
            for (FPData f : s0) {
                if (s1.contains(f)) {
                    result.add(f);
                }
            }
            return result;
        } else {
            return new ArrayList<FPData>(0);
        }
    }

    /**
     * Get list of fingerprints matching query at levels 0, 1 and 2.
     * @param fp0
     * @param fp1
     * @param fp2
     * @return
     */
    private List<FPData> findLevel2(FPLevel fp0, FPLevel fp1, FPLevel fp2) {
        Set<FPData> s0 = level0Index.get(fp0);
        Set<FPData> s1 = level1Index.get(fp1);
        Set<FPData> s2 = level2Index.get(fp2);
        if (s0 != null && s1 != null && s2 != null) {
            if (s0.size() > s1.size()) {
                Set<FPData> s = s1;
                s1 = s0;
                s0 = s;
            }
            if (s1.size() > s2.size()) {
                Set<FPData> s = s2;
                s2 = s1;
                if (s0.size() > s.size()) {
                    s1 = s0;
                    s0 = s;
                } else {
                    s1 = s;
                }
            }
            List<FPData> result = new ArrayList<FPData>();
            for (FPData f : s0) {
                if (s1.contains(f) && s2.contains(f)) {
                    result.add(f);
                }
            }
            return result;
        } else {
            return new ArrayList<FPData>(0);
        }
    }
}
