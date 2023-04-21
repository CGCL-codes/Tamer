package subsearch.index.features;

import java.util.HashMap;
import java.util.HashSet;
import subsearch.graph.Graph;
import subsearch.index.features.extractor.PathExtractor;
import subsearch.index.features.extractor.RingExtractor;
import subsearch.index.features.extractor.SubtreeExtractor;
import subsearch.index.features.storage.CountHashTable;
import subsearch.index.features.storage.FeatureStorage;
import subsearch.index.features.storage.HashKeyFingerprint;
import subsearch.index.features.storage.IntHashSet;

/**
 * Allows common hash key fingerprint creation based on
 * different features classes: path, subtree, rings
 */
public class FingerprintBuilder {

    private boolean findPaths = false;

    private boolean findSubtrees = true;

    private boolean findRings = true;

    private int pathSize = 8;

    private int subtreeSize = 5;

    private int ringSize = 8;

    private int fingerprintSize;

    /**
	 * Note: fingerprintSize should be a power of 2
	 */
    public FingerprintBuilder(int fingerprintSize) {
        this.fingerprintSize = fingerprintSize;
    }

    /**
	 * Note: fingerprintSize should be a power of 2
	 */
    public FingerprintBuilder(int fingerprintSize, boolean findPaths, boolean findSubtrees, boolean findRings, int pathSize, int subtreeSize, int ringSize) {
        this.fingerprintSize = fingerprintSize;
        this.findPaths = findPaths;
        this.findSubtrees = findSubtrees;
        this.findRings = findRings;
        this.pathSize = pathSize;
        this.subtreeSize = subtreeSize;
        this.ringSize = ringSize;
    }

    private void extractFeatures(FeatureStorage<? super String, ?> fs, Graph graph) {
        if (findPaths) {
            PathExtractor pe = new PathExtractor(graph, fs, pathSize, PathExtractor.SIMPLE_PATHS);
            pe.extractFeatures();
        }
        if (findSubtrees) {
            SubtreeExtractor se = new SubtreeExtractor(graph, fs, subtreeSize);
            se.extractFeatures();
        }
        if (findRings) {
            RingExtractor re = new RingExtractor(graph, fs, ringSize);
            re.extractFeatures();
        }
    }

    public Fingerprint getFingerprint(Graph graph) {
        HashKeyFingerprint fp = new HashKeyFingerprint(fingerprintSize);
        extractFeatures(fp, graph);
        return fp.getResult();
    }

    public int getFingerprintSize() {
        return fingerprintSize;
    }

    public HashSet<Integer> getIntHashSet(Graph graph) {
        IntHashSet ihs = new IntHashSet();
        extractFeatures(ihs, graph);
        return ihs.getResult();
    }

    public HashMap<String, Integer> getCountHashTable(Graph graph) {
        CountHashTable<String> cht = new CountHashTable<String>();
        extractFeatures(cht, graph);
        return cht.getResult();
    }

    public String toString() {
        return (findPaths ? "Paths " + pathSize + " " : "") + (findSubtrees ? "Subtrees " + subtreeSize + " " : "") + (findRings ? "Rings " + ringSize + " " : "");
    }
}
