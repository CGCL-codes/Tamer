package riktree;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class INEXMain {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: [ktree order] [RI dimensions]");
            System.exit(1);
        }
        int m = Integer.parseInt(args[0]);
        int dim = Integer.parseInt(args[1]);
        int seedlength = dim / 10;
        try {
            buildKtree(dim, "entity.text.and.tags.ri");
            addTextAndTags();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<IDVector> loadVectors(String filename, int dim) throws IOException {
        long start, end;
        System.out.println("loading vectors");
        start = System.currentTimeMillis();
        List<IDVector> vectors = new ArrayList<IDVector>();
        DataInputStream is = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
        int docCount = is.readInt();
        int fileDims = is.readInt();
        if (dim != fileDims) {
            throw new IOException("dimensions don't match");
        }
        for (int i = 0; i < docCount; ++i) {
            if (i % 100000 == 0) {
                System.out.println("loaded " + i + " docments in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds...");
            }
            int docid = is.readInt();
            float[] vals = new float[dim];
            for (int j = 0; j < dim; ++j) {
                vals[j] = is.readFloat();
            }
            vectors.add(new IDVector(docid, vals));
        }
        is.close();
        end = System.currentTimeMillis();
        System.out.println("Loading took " + ((end - start) / 1000.0) + " seconds");
        return vectors;
    }

    private static void addTextAndTags() throws IOException {
        long start, end;
        int dim = 1000;
        System.out.println("combining text and tag vectors");
        start = System.currentTimeMillis();
        DataInputStream isText = new DataInputStream(new BufferedInputStream(new FileInputStream("entity.text.ribm25")));
        int textDocCount = isText.readInt();
        int textFileDims = isText.readInt();
        if (dim != textFileDims) {
            throw new IOException("text dimensions don't match");
        }
        DataInputStream isTags = new DataInputStream(new BufferedInputStream(new FileInputStream("entity.tag.ri")));
        int tagDocCount = isTags.readInt();
        int tagFileDims = isTags.readInt();
        if (dim != tagFileDims) {
            throw new IOException("tags dimensions don't match");
        }
        if (textDocCount != tagDocCount) {
            throw new IOException("# of docs doesn't match");
        }
        DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("entity.text.and.tags.ri")));
        os.writeInt(textDocCount);
        os.writeInt(textFileDims);
        for (int i = 0; i < textDocCount; ++i) {
            if (i % 100000 == 0) {
                System.out.println("loaded " + i + " docments in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds...");
            }
            int docid = isText.readInt();
            if (docid != isTags.readInt()) {
                throw new IOException("docids don't match");
            }
            float[] vals = new float[dim];
            for (int j = 0; j < dim; ++j) {
                vals[j] = isText.readFloat();
            }
            for (int j = 0; j < dim; ++j) {
                vals[j] += isTags.readFloat();
            }
            IDVector v = new IDVector(docid, vals);
            v.normalise();
            os.writeInt(docid);
            for (float f : v.get()) {
                os.writeFloat(f);
            }
        }
        isText.close();
        isTags.close();
        os.close();
        end = System.currentTimeMillis();
        System.out.println("Loading took " + ((end - start) / 1000.0) + " seconds");
    }

    private static void buildKtree(int dim, String filename) throws Exception {
        long start, end;
        List<IDVector> vectors = loadVectors(filename, dim);
        int mStart = 15;
        int mStop = 100;
        int[] targets = { 100, 500, 1000, 2500, 5000, 10000 };
        boolean[] targetFound = { false, false, false, false, false, false };
        for (int m = mStart; m <= mStop; ++m) {
            System.out.println("building K-tree");
            start = System.currentTimeMillis();
            RIKTree kt = new RIKTree(m);
            int vecCount = 0;
            for (IDVector v : vectors) {
                kt.add(v);
            }
            end = System.currentTimeMillis();
            System.out.println("K-tree took " + ((end - start) / 1000.0) + " seconds");
            start = System.currentTimeMillis();
            kt.rearrange();
            end = System.currentTimeMillis();
            System.out.println("rearranging took " + ((end - start) / 1000.0) + " seconds");
            System.out.println("K-Tree details:");
            System.out.println("\torder: " + kt.order());
            System.out.println("\tlevels: " + kt.levels());
            for (int i = 1; i < kt.levels(); ++i) {
                LeafClusterCounter cv = new LeafClusterCounter();
                kt.visitLeaves(cv, i);
                System.out.println("\tlevel " + i + ": " + cv.clusters + " clusters");
                for (int t = 0; t < targets.length; ++t) {
                    if (targetFound[t]) {
                        continue;
                    }
                    int target = targets[t];
                    int margin = target / 20;
                    if (Math.abs(cv.clusters - target) <= margin) {
                        System.out.println("within 5% of target " + target);
                        System.out.println("saving level " + i);
                        INEXClusterWriter icw = new INEXClusterWriter("ktree_entity_text_and_tags_" + m + "m_level" + i + ".txt");
                        kt.visitLeaves(icw, i);
                        icw.close();
                        targetFound[t] = true;
                    }
                }
            }
        }
    }

    private static void writeRIBM25Vectors(int dim, int seedlength) throws Exception {
        long start, end;
        start = System.currentTimeMillis();
        BM25RI g = new BM25RI(dim, seedlength);
        g.writeVectors();
        end = System.currentTimeMillis();
        System.out.println("Loading, writing and RI took" + ((end - start) / 1000.0) + " seconds");
    }

    private static void writeRITagVectors(int dim, int seedlength) throws Exception {
        long start, end;
        start = System.currentTimeMillis();
        TagsRI g = new TagsRI(dim, seedlength);
        g.writeVectors();
        end = System.currentTimeMillis();
        System.out.println("Loading tags, writing and RI took" + ((end - start) / 1000.0) + " seconds");
    }
}

class LeafClusterCounter implements ClusterVisitor {

    public int clusters = 0;

    public void accept(Vector[] v, int count) throws Exception {
        if (count > 0) {
            ++clusters;
        }
    }
}

class INEXClusterWriter implements ClusterVisitor {

    BufferedWriter os;

    int clusterNumber;

    public INEXClusterWriter(String filename) throws IOException {
        os = new BufferedWriter(new FileWriter(filename));
        clusterNumber = 1;
    }

    public void accept(Vector[] v, int count) throws Exception {
        for (Vector vec : v) {
            IDVector idv = (IDVector) vec;
            os.write("" + idv.id());
            os.write(",");
            os.write("" + clusterNumber);
            os.newLine();
        }
        ++clusterNumber;
    }

    public void close() throws IOException {
        if (os != null) {
            os.close();
        }
    }
}

class BM25RI {

    public static long riTime;

    int dim;

    int seedlength;

    BM25RI(int dim, int seedlength) {
        this.dim = dim;
        this.seedlength = seedlength;
    }

    public void writeVectors() throws Exception {
        Map<Integer, Integer> docFreq = loadDocFreq();
        double avgDocLen = loadAvgDocLen();
        loadRIBM25Text(docFreq, avgDocLen);
    }

    public static double loadAvgDocLen() throws IOException {
        System.out.println("loading average document length...");
        String fileName = "entity.text.stats";
        BufferedReader is = new BufferedReader(new FileReader(fileName));
        is.readLine();
        String line = null;
        int docCount = 0;
        int totalFreqCount = 0;
        while ((line = is.readLine()) != null) {
            String[] split = line.split(",");
            ++docCount;
            totalFreqCount += Integer.parseInt(split[2]);
        }
        is.close();
        return totalFreqCount / (double) docCount;
    }

    public static Map<Integer, Integer> loadDocFreq() throws IOException {
        System.out.println("loading document freqencies...");
        Map<Integer, Integer> docFreq = new HashMap<Integer, Integer>();
        String fileName = "entity.text.stats";
        BufferedReader is = new BufferedReader(new FileReader(fileName));
        is.readLine();
        String line = null;
        while ((line = is.readLine()) != null) {
            String[] split = line.split(",");
            int term = Integer.parseInt(split[0]);
            int freq = Integer.parseInt(split[3]);
            Integer mapFreq = docFreq.get(term);
            if (mapFreq == null) {
                mapFreq = freq;
            } else {
                mapFreq += freq;
            }
            docFreq.put(term, mapFreq);
        }
        is.close();
        return docFreq;
    }

    public void loadRIBM25Text(Map<Integer, Integer> docFreq, double avgDocLen) throws IOException {
        System.out.println("loading RI BM25...");
        ObjectVector.vecLength = dim;
        String fileName = "/media/disk/inex09/entity.text.freq.id";
        long start, end, insertTime;
        start = System.currentTimeMillis();
        BufferedReader inputStream = null;
        VectorStoreSparseRAM termVectors = new VectorStoreSparseRAM();
        try {
            String line;
            inputStream = new BufferedReader(new FileReader(fileName));
            final int docCount = 2666190;
            int lineCount = 0;
            while ((line = inputStream.readLine()) != null) {
                if (++lineCount % 100000 == 0) {
                    System.out.println("loaded " + lineCount + " docments in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds...");
                }
                String[] split = line.split(" ");
                if (split.length == 0) {
                    throw new IOException(fileName + " contains an empty line");
                }
                int docID = Integer.parseInt(split[0]);
                List<Integer> dims = new ArrayList<Integer>();
                List<Integer> vals = new ArrayList<Integer>();
                for (int i = 1; i < split.length; ++i) {
                    String[] s = split[i].split(":");
                    if (s.length != 2) {
                        throw new IOException(fileName + " has bad token");
                    }
                    dims.add(Integer.parseInt(s[0]));
                    vals.add(Integer.parseInt(s[1]));
                }
                assert (dims.size() == vals.size());
                int docLen = 0;
                for (int tf : vals) {
                    docLen += tf;
                }
                float[] docVector = new float[dim];
                for (int i = 0; i < dims.size(); ++i) {
                    int termID = dims.get(i);
                    int df = docFreq.get(termID);
                    double NDL = bm25.NDL(docLen, avgDocLen);
                    double CFW = bm25.CFW(df, docCount);
                    int TF = vals.get(i);
                    final double K1 = 2;
                    final double b = 0.75;
                    double BM25 = bm25.BM25(CFW, TF, K1, b, NDL);
                    long riStart = System.currentTimeMillis();
                    String term = "" + termID;
                    float[] termVector = termVectors.getVector(term);
                    if (termVector == null) {
                        termVectors.generateAndPutNewVector(term, seedlength);
                        termVector = termVectors.getVector(term);
                    }
                    for (int j = 0; j < dim; j++) {
                        docVector[j] += termVector[j] * BM25;
                    }
                    riTime += System.currentTimeMillis() - riStart;
                }
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
            }
        }
        end = System.currentTimeMillis();
        insertTime = end - start;
    }
}

class TagsRI {

    int dim;

    int seedlength;

    TagsRI(int dim, int seedlength) {
        this.dim = dim;
        this.seedlength = seedlength;
    }

    public void writeVectors() throws Exception {
        loadRITags();
    }

    public void loadRITags() throws IOException {
        System.out.println("loading RI Tags...");
        ObjectVector.vecLength = dim;
        String fileName = "/media/disk/inex09/entity.tag.freq.id";
        long start, end, insertTime;
        start = System.currentTimeMillis();
        BufferedReader inputStream = null;
        DataOutputStream outputStream = null;
        VectorStoreSparseRAM termVectors = new VectorStoreSparseRAM();
        try {
            String line;
            inputStream = new BufferedReader(new FileReader(fileName));
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("entity.tag.ri")));
            final int docCount = 2666190;
            outputStream.writeInt(docCount);
            outputStream.writeInt(dim);
            int lineCount = 0;
            while ((line = inputStream.readLine()) != null) {
                if (++lineCount % 100000 == 0) {
                    System.out.println("loaded " + lineCount + " docments in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds...");
                }
                String[] split = line.split(" ");
                if (split.length == 0) {
                    throw new IOException(fileName + " contains an empty line");
                }
                int docID = Integer.parseInt(split[0]);
                List<Integer> dims = new ArrayList<Integer>();
                List<Integer> vals = new ArrayList<Integer>();
                for (int i = 1; i < split.length; ++i) {
                    String[] s = split[i].split(":");
                    if (s.length != 2) {
                        throw new IOException(fileName + " has bad token");
                    }
                    dims.add(Integer.parseInt(s[0]));
                    vals.add(Integer.parseInt(s[1]));
                }
                assert (dims.size() == vals.size());
                int docLen = 0;
                for (int tf : vals) {
                    docLen += tf;
                }
                float[] docVector = new float[dim];
                for (int i = 0; i < dims.size(); ++i) {
                    int termID = dims.get(i);
                    String term = "" + termID;
                    float[] termVector = termVectors.getVector(term);
                    if (termVector == null) {
                        termVectors.generateAndPutNewVector(term, seedlength);
                        termVector = termVectors.getVector(term);
                    }
                    int tagFreq = vals.get(i);
                    for (int j = 0; j < dim; j++) {
                        docVector[j] += termVector[j] * tagFreq;
                    }
                }
                IDVector v = new IDVector(docID, docVector);
                v.normalise();
                outputStream.writeInt(docID);
                for (int d = 0; d < dim; ++d) {
                    outputStream.writeFloat(v.get(d));
                }
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        end = System.currentTimeMillis();
        insertTime = end - start;
    }
}
