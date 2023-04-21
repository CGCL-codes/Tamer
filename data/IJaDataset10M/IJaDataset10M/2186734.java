package ktree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author chris
 */
public class INEX10Main {

    String subsetFile = "inex_xml_mining_subset_2010.txt";

    String docidFile = "doclist.aspt";

    String byteRIFile = "RI1024sp24.txt";

    Set<Integer> loadSubset() throws Exception {
        Set<Integer> subset = new TreeSet<Integer>();
        BufferedReader is = new BufferedReader(new FileReader(subsetFile));
        String line;
        while ((line = is.readLine()) != null) {
            subset.add(Integer.parseInt(line));
        }
        return subset;
    }

    List<ByteVector> loadRIByteVectors(Set<Integer> subset) throws Exception {
        List<ByteVector> vectors = new ArrayList<ByteVector>();
        BufferedReader isIds = new BufferedReader(new FileReader(docidFile));
        BufferedReader isVectors = new BufferedReader(new FileReader(byteRIFile));
        String idline, vectorline;
        int dims = -1;
        while ((idline = isIds.readLine()) != null && (vectorline = isVectors.readLine()) != null) {
            int id = Integer.parseInt(idline);
            if (subset.contains(id)) {
                String[] tokens = vectorline.split(",");
                if (dims != -1 && tokens.length != dims) {
                    throw new Exception("dimensions do not match");
                }
                byte[] data = new byte[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    data[i] = Byte.parseByte(tokens[i]);
                }
                vectors.add(new ByteVector(data, id));
            }
        }
        return vectors;
    }

    List<BitVector> loadRIBitVectors(Set<Integer> subset) throws Exception {
        List<BitVector> vectors = new ArrayList<BitVector>();
        BufferedReader isIds = new BufferedReader(new FileReader(docidFile));
        BufferedReader isVectors = new BufferedReader(new FileReader(byteRIFile));
        String idline, vectorline;
        int dims = -1;
        Set<Integer> seen = new TreeSet<Integer>();
        while ((idline = isIds.readLine()) != null && (vectorline = isVectors.readLine()) != null) {
            int id = -1;
            try {
                id = Integer.parseInt(idline);
            } catch (Exception e) {
                System.out.println("docid parsing failed: " + idline);
                continue;
            }
            if (id == 0) {
                System.out.println("line with 0 docid");
                continue;
            }
            if (seen.contains(id)) {
                throw new Exception("document id already seen: " + id);
            } else {
                seen.add(id);
            }
            String[] tokens = vectorline.split(",");
            if (dims != -1 && tokens.length != dims) {
                throw new Exception("dimensions do not match");
            }
            dims = tokens.length;
            BitVector v = new BitVector(dims, id);
            for (int i = 0; i < dims; ++i) {
                int value = Integer.parseInt(tokens[i]);
                if (value >= 0) {
                    v.set(i);
                }
            }
            vectors.add(v);
        }
        return vectors;
    }

    void elapsed(String task, long start) {
        System.out.printf("%s took %f seconds%n", task, (System.nanoTime() - start) / 1e9);
    }

    void print(KTree<BitVector> kt) throws Exception {
        System.out.println("K-Tree details:");
        System.out.println("\torder: " + kt.order());
        System.out.println("\tlevels: " + kt.levels());
        for (int i = 1; i <= kt.levels(); ++i) {
            CounterVisitor cv = new CounterVisitor();
            kt.visit(cv, i);
            System.out.println("\tlevel " + i + ": " + cv.nodes() + " nodes, " + cv.vectors() + " vectors");
        }
        System.out.println("\tinternal nodes: " + (kt.nodes() - kt.leafNodes()));
        System.out.println("\tleaf nodes: " + kt.leafNodes());
        System.out.println("\ttotal nodes: " + kt.nodes());
        System.out.println("\tvectors contained in leaves: " + kt.size());
        System.out.println("\tleaf node RMSE: " + kt.RMSE());
    }

    void run(String[] args) throws Exception {
        long start = System.nanoTime();
        Set<Integer> subset = loadSubset();
        List<BitVector> vectors = loadRIBitVectors(subset);
        elapsed("loading " + vectors.size() + " vectors", start);
        KTree<BitVector> kt = KTreeFactory.bitKTree(500);
        int inserted = 0;
        start = System.nanoTime();
        for (BitVector v : vectors) {
            kt.add(v);
            if (++inserted % 1e5 == 0) {
                System.out.printf("inserted %d vectors%n", inserted);
            }
        }
        elapsed("Building K-tree", start);
        print(kt);
        System.out.println("Rearranging K-tree...");
        start = System.nanoTime();
        kt.rearrange();
        elapsed("Rearranging K-tree", start);
        print(kt);
    }

    public static void main(String[] args) throws Exception {
        new INEX10Main().run(args);
    }
}
