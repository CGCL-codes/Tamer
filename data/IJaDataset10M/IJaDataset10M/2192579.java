package org.expasy.jpl.dev.quickmod.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.expasy.jpl.dev.quickmod.params.SearchParams;

public class ClusterExport {

    public static void writeFile(double[][] clusterMatrix, String[] expFiles, SearchParams sp) throws IOException {
        String outputFileFullPath = sp.getResDirectory() + "cluster.txt";
        int dimension = expFiles.length;
        try {
            FileWriter fstream = new FileWriter(outputFileFullPath);
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 0; i < dimension; i++) {
                out.write(expFiles[i].substring(expFiles[i].length() - 17, expFiles[i].length() - 4));
                if (i + 1 < dimension) {
                    out.write("\t");
                }
            }
            out.write("\n");
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    out.write(clusterMatrix[j][i] + "");
                    if (j + 1 < dimension) {
                        out.write("\t");
                    }
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            throw new IOException("Can't create file " + outputFileFullPath);
        }
    }
}
