package com.tomgibara.cluster;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import com.tomgibara.cluster.gvm.GvmResult;

public class ClusterPoints {

    public static void main(String[] args) throws IOException {
        for (Entry<String, Integer> entry : ClusterFiles.files.entrySet()) {
            cluster(entry.getKey(), entry.getValue());
        }
    }

    private static void cluster(String name, int capacity) throws IOException {
        final List<GvmResult<List<double[]>>> results = ClusterFiles.cluster(name, capacity);
        FileWriter writer = new FileWriter("../cluster-common/R/" + name + "-clustered.txt");
        for (int i = 0; i < results.size(); i++) {
            for (double[] pt : results.get(i).getKey()) {
                writer.write(String.format("%3.3f %3.3f %d%n", pt[0], pt[1], i + 1));
            }
        }
        writer.close();
    }
}
