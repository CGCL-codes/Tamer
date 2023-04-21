package gem;

import gem.util.ArrayUtils;
import java.util.*;

/**
 * @author Ozgun Babur
 */
public class Hierarchical {

    static int nextID = -1;

    public static List<Integer> getCancerSubtype(Collection<Gene> genes, boolean[] posNormal, boolean[] posCancer) {
        boolean[] expPos = CellTypeMatcher.takeOR(posNormal, posCancer);
        int tc = ArrayUtils.countTrue(expPos);
        List<Sample> list = new ArrayList<Sample>(tc);
        for (int i = 0; i < expPos.length; i++) {
            if (expPos[i]) list.add(new Sample(genes, i));
        }
        Map<String, Double> distMap = new HashMap<String, Double>();
        while (list.size() > 1) {
            int[] ind = getClosestTwo(list, distMap);
            unite(list, list.get(ind[0]), list.get(ind[1]));
        }
        Sample s = list.get(0);
        s = removeOutlier(s);
        int cnt0 = s.child[0].countElement(posNormal);
        int cnt1 = s.child[1].countElement(posNormal);
        int size0 = s.child[0].collectLeaf(null).size();
        int size1 = s.child[1].collectLeaf(null).size();
        double rat0 = cnt0 / (double) size0;
        double rat1 = cnt1 / (double) size1;
        s = rat0 < rat1 ? s.child[0] : s.child[1];
        ArrayList<Integer> ids = new ArrayList<Integer>();
        s.collectIDs(ids);
        Collections.sort(ids);
        return ids;
    }

    static Sample removeOutlier(Sample s) {
        int size0 = s.child[0].collectLeaf(null).size();
        int size1 = s.child[1].collectLeaf(null).size();
        if (size0 < 10) return removeOutlier(s.child[1]);
        if (size1 < 10) return removeOutlier(s.child[0]);
        return s;
    }

    static int[] getClosestTwo(List<Sample> list, Map<String, Double> distMap) {
        int index1 = -1;
        int index2 = -1;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Sample s1 = list.get(i);
                Sample s2 = list.get(j);
                double dist = s1.getDistance(s2, distMap);
                if (dist < minDist) {
                    minDist = dist;
                    index1 = i;
                    index2 = j;
                }
            }
        }
        return new int[] { index1, index2 };
    }

    static void unite(List<Sample> list, Sample s1, Sample s2) {
        Sample s = new Sample(s1, s2);
        list.remove(s1);
        list.remove(s2);
        list.add(s);
    }

    static class Sample {

        int id;

        double[] value;

        Sample[] child;

        Sample(Collection<Gene> genes, int id) {
            this.id = id;
            value = new double[genes.size()];
            int i = 0;
            for (Gene gene : genes) {
                value[i++] = gene.value[id];
            }
        }

        Sample(Sample s0, Sample s1) {
            child = new Sample[2];
            child[0] = s0;
            child[1] = s1;
            id = (nextID--);
        }

        public double getDistance(Sample s, Map<String, Double> distMap) {
            String key = id + "|" + s.id;
            if (distMap.containsKey(key)) return distMap.get(key);
            List<Sample> list1 = collectLeaf(null);
            List<Sample> list2 = s.collectLeaf(null);
            double sum = 0;
            for (Sample s1 : list1) {
                for (Sample s2 : list2) {
                    String k = s1.id + "|" + s2.id;
                    double dist;
                    if (distMap.containsKey(k)) {
                        dist = distMap.get(k);
                    } else {
                        dist = s1.calcDistance(s2);
                        distMap.put(k, dist);
                    }
                    sum += dist;
                }
            }
            sum /= list1.size() * list2.size();
            return sum;
        }

        public double calcDistance(Sample s) {
            assert id >= 0 && s.id >= 0;
            double sum = 0;
            for (int i = 0; i < value.length; i++) {
                double d = value[i] - s.value[i];
                sum += d * d;
            }
            return Math.sqrt(sum);
        }

        public int countElement(boolean[] pos) {
            if (id >= 0) return pos[id] ? 1 : 0; else {
                int sum = 0;
                for (Sample c : child) {
                    sum += c.countElement(pos);
                }
                return sum;
            }
        }

        public void collectIDs(List<Integer> ids) {
            if (id >= 0) ids.add(id); else {
                for (Sample c : child) {
                    c.collectIDs(ids);
                }
            }
        }

        public List<Sample> collectLeaf(List<Sample> samples) {
            if (samples == null) samples = new ArrayList<Sample>();
            if (id >= 0) samples.add(this); else {
                for (Sample c : child) {
                    c.collectLeaf(samples);
                }
            }
            return samples;
        }
    }
}
