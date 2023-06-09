package clear.util.cluster;

import java.util.HashSet;
import com.carrotsearch.hppc.ObjectDoubleOpenHashMap;

@SuppressWarnings("serial")
public class ProbCluster extends HashSet<ObjectDoubleOpenHashMap<String>> implements Comparable<ProbCluster> {

    public String key;

    public double score;

    public ProbCluster(String key) {
        set(key, 0);
    }

    public void set(String key, double score) {
        this.key = key;
        this.score = score;
    }

    public int compareTo(ProbCluster cluster) {
        if (score - cluster.score > 0) return -1; else if (score - cluster.score < 0) return 1; else return 0;
    }
}
