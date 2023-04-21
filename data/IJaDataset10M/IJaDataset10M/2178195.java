package riktree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class Option {

    public abstract String flag();

    public abstract List<String> incompatibleFlags();

    public abstract int params();

    public abstract String addParams(List<String> params);

    public void run() throws Exception {
        execute();
        for (Option o : postOptions) {
            o.run();
        }
    }

    public void addPostOption(Option o) {
        postOptions.add(o);
    }

    protected abstract void execute() throws Exception;

    private ArrayList<Option> postOptions = new ArrayList<Option>();
}

/**
 * Provides a K-tree that options can read or modify.
 * The concrete type is typically an option too.
 */
interface KTreeProvider {

    RIKTree ktree();
}

class OptionException extends Exception {

    OptionException(String error) {
        super(error);
    }
}

class OptionFactory {

    public OptionFactory() {
        stages = new ArrayList<List<Option>>();
        allOptions = new ArrayList<Option>();
    }

    public void run(String[] args) throws Exception {
        if (readArgs(args)) {
            for (List<Option> stage : stages) {
                for (Option opt : stage) {
                    opt.run();
                }
            }
        }
    }

    private boolean readArgs(String[] args) {
        if (args.length == 0) {
            printUsage();
            return false;
        }
        try {
            Map<String, List<String>> parsedArgs = parseArgs(args);
            buildStages(parsedArgs);
            testOptions(parsedArgs);
            allFlagsValid(parsedArgs);
            addParams(parsedArgs);
        } catch (OptionException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("Run program with no arguments to see usage.");
            return false;
        }
        return true;
    }

    private void allFlagsValid(Map<String, List<String>> parsedArgs) throws OptionException {
        Set<String> flags = new HashSet<String>();
        for (Option opt : allOptions) {
            flags.add(opt.flag());
        }
        List<String> badFlags = new ArrayList<String>();
        for (String arg : parsedArgs.keySet()) {
            if (!flags.contains(arg)) {
                badFlags.add(arg);
            }
        }
        if (badFlags.size() > 0) {
            String msg = "The following flags are invalid:";
            for (String flag : badFlags) {
                msg += " -" + flag;
            }
            throw new OptionException(msg);
        }
    }

    /**
     * pre: args.length > 0
     * @return null on failure to parse OR map of option -> parameters
     */
    private static Map<String, List<String>> parseArgs(String[] args) throws OptionException {
        Map<String, List<String>> parsedArgs = new HashMap<String, List<String>>();
        boolean seenOption = false;
        String option = null;
        List<String> params = null;
        for (String s : args) {
            boolean isOption = s.charAt(0) == '-';
            if (!seenOption && !isOption) {
                throw new OptionException("First option must be a flag. For example \"-b\".");
            } else {
                if (isOption) {
                    if (seenOption) {
                        parsedArgs.put(option, params);
                    }
                    option = s.substring(1);
                    params = new ArrayList<String>();
                    seenOption = true;
                } else {
                    params.add(s);
                }
            }
        }
        parsedArgs.put(option, params);
        return parsedArgs;
    }

    private static void printUsage() {
        System.out.println("Usage: ktree [options]\n" + "Options:\n" + " Global (0 or more may be selected):\n" + "  -d [string]         Use [string] as delimeter when reading or writing a vector\n" + "                      file. If this parameter is not specified, the default is\n" + "                      whitespace.\n\n" + " Create K-tree (only 1 can be selected):\n" + "  -b [file] [m]       Build a K-tree from vectors in [file] with order [m].\n" + "  -l [file]           Load a K-tree from [file]\n\n" + " Modify K-tree (0 or 1 may be selected):\n" + "  -r                  Rearrange K-tree by placing all data vectors in the\n" + "                      nearest leaf.\n\n" + " Analyze K-tree after creation and modification (0 or more may be selected):\n" + "  -p                  Print K-tree statistics.\n" + "  -s                  Search K-tree for vectors and print extra statistics.\n\n" + " Write K-tree after all steps complete (0 or more may be selected):\n" + "  -w [file]           Write K-tree to [file].\n" + "  -wd [file] [level]  Write IDs for data vectors at K-tree [level] to [file].\n" + "                      Each data vector is identified by the line number of the\n" + "                      file it was loaded from. Line numbers start at 1.\n" + "  -wv [file] [level]  Write vectors at K-tree [level] to [file].\n\n" + "K-tree levels:\n" + "Level 1 is the root. Level 2 is one below the root and so on. Only levels represented by internal nodes may be chosen (a value between 1 and number of levels - 1). A level of 0 indicates the lowest internal level in the tree (codebook vectors). For more information of the structure of the tree see http://sf.net/projects/ktree.\n\n" + "Examples:\n" + " Build a K-tree of order 50 from test.txt (space separated vectors, 1 per line) and write to test.50.ktree:\n" + "  ktree -b test.txt 50 -w test.50.ktree\n\n" + " Build a K-tree of order 20 from test.csv (comma separated vectors, 1 per line) and write to test.20.ktree and print some simple stats:\n" + "  ktree -b test.csv 20 -d , -w test.20.ktree -p\n\n" + " Load a K-tree previously saved in test.50.ktree and print simple stats and stats based on searching the tree:\n" + "  ktree -l test.50.ktree -p -s\n\n" + " Load a K-tree previously saved in test.50.ktree and write the top level cluster centers (vectors) to test.ktree.level1.vec:\n" + "  ktree -l test.50.ktree -wv test.ktree.level1.vec\n\n" + " Load a K-tree previously saved in test.50.ktree, move leaves to nearest neighbours, print simple and search based stats after each load or modification and write the modified tree to test.50.rearrange.ktree:\n" + "  ktree -l test.50.ktree -p -s -r -w test.50.rearrange.ktree\n\n");
    }

    private void addTestFlag(List<Option> stage, Map<String, List<String>> parsedArgs, Option opt) {
        if (parsedArgs.containsKey(opt.flag())) {
            stage.add(opt);
        }
        addTestFlag(parsedArgs, opt);
    }

    private void addTestFlag(Map<String, List<String>> parsedArgs, Option opt) {
        if (parsedArgs.containsKey(opt.flag())) {
            allOptions.add(opt);
        }
    }

    private int size() {
        int size = 0;
        for (List<Option> stage : stages) {
            size += stage.size();
        }
        return size;
    }

    private void buildStages(Map<String, List<String>> parsedArgs) throws OptionException {
        List<Option> stage1 = new ArrayList<Option>();
        Delimeter delim = new Delimeter();
        addTestFlag(stage1, parsedArgs, delim);
        List<Option> stage2 = new ArrayList<Option>();
        addTestFlag(stage2, parsedArgs, new Load());
        addTestFlag(stage2, parsedArgs, new Build(delim));
        KTreeProvider ktp = null;
        boolean found = false;
        for (Option o : stage2) {
            if (parsedArgs.containsKey(o.flag())) {
                if (found) {
                    throw new OptionException("There is more than one argument that produces a K-tree.");
                } else {
                    ktp = (KTreeProvider) o;
                }
            }
        }
        if (ktp == null) {
            throw new OptionException("There are no options that will create a K-tree.");
        }
        Print print = new Print(ktp);
        addTestFlag(parsedArgs, print);
        Search search = new Search(ktp);
        addTestFlag(parsedArgs, search);
        for (Option o : stage2) {
            o.addPostOption(print);
            o.addPostOption(search);
        }
        List<Option> stage3 = new ArrayList<Option>();
        Rearrange rearrange = new Rearrange(ktp);
        addTestFlag(stage3, parsedArgs, rearrange);
        rearrange.addPostOption(print);
        rearrange.addPostOption(search);
        List<Option> stage4 = new ArrayList<Option>();
        addTestFlag(stage4, parsedArgs, new Write(ktp));
        addTestFlag(stage4, parsedArgs, new WriteVector(ktp, delim));
        addTestFlag(stage4, parsedArgs, new WriteDataVector(ktp, delim));
        stages.add(stage1);
        stages.add(stage2);
        stages.add(stage3);
        stages.add(stage4);
    }

    private void testOptions(Map<String, List<String>> parsedArgs) throws OptionException {
        for (Option o : allOptions) {
            List<String> incompat = o.incompatibleFlags();
            if (incompat == null) {
                continue;
            }
            for (Option oo : allOptions) {
                if (o != oo) {
                    for (String s : incompat) {
                        if (parsedArgs.containsKey(s) && s.equals(oo.flag())) {
                            throw new OptionException("The flags -" + s + " and -" + oo.flag() + " are incompatible.");
                        }
                    }
                }
            }
        }
    }

    private void addParams(Map<String, List<String>> parsedArgs) throws OptionException {
        for (Option opt : allOptions) {
            List<String> params = parsedArgs.get(opt.flag());
            if (params.size() != opt.params()) {
                throw new OptionException("Not enough parameters for the flag -" + opt.flag() + ".");
            }
            String err = opt.addParams(params);
            if (err != null) {
                throw new OptionException(err);
            }
        }
    }

    List<List<Option>> stages;

    List<Option> allOptions;
}

class Delimeter extends Option {

    public String flag() {
        return "d";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 1;
    }

    public String addParams(List<String> params) {
        delimeter = params.get(0);
        return null;
    }

    public void execute() throws Exception {
    }

    public String readDelimeter() {
        return delimeter;
    }

    public String writeDelimeter() {
        if (delimeter.equals("\\s")) {
            return " ";
        } else {
            return delimeter;
        }
    }

    private String delimeter = "\\s";
}

class Build extends Option implements KTreeProvider {

    public Build(Delimeter delim) {
        this.delim = delim;
    }

    public String flag() {
        return "b";
    }

    public List<String> incompatibleFlags() {
        List<String> l = new ArrayList<String>();
        l.add("l");
        return l;
    }

    public int params() {
        return 2;
    }

    public String addParams(List<String> params) {
        file = params.get(0);
        try {
            m = Integer.parseInt(params.get(1));
        } catch (NumberFormatException e) {
            return "m is not an integer";
        }
        return null;
    }

    public void execute() throws Exception {
        Vector[] vectors = loadVectors();
        long start, end, insertTime;
        start = System.currentTimeMillis();
        int steps = 10;
        int increment = vectors.length / steps;
        kt = new RIKTree(m);
        System.out.println("Inserting vectors into K-tree.");
        for (int i = 0; i < vectors.length; ++i) {
            kt.add(vectors[i]);
            if (i % increment == 0) {
                System.out.println("inserted " + i + " of " + vectors.length + " vectors.");
            }
        }
        end = System.currentTimeMillis();
        insertTime = end - start;
        System.out.println("K-tree took " + insertTime / 1000.0 + " seconds to build.");
    }

    public RIKTree ktree() {
        return kt;
    }

    private static String[] removeEmpty(String[] sa) {
        List<String> notEmpty = new ArrayList<String>();
        for (String s : sa) {
            if (s.length() != 0) {
                notEmpty.add(s);
            }
        }
        String[] result = new String[notEmpty.size()];
        notEmpty.toArray(result);
        return result;
    }

    private Vector[] loadVectors() throws IOException {
        BufferedReader inputStream = null;
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        try {
            String line;
            int lineNum = 1;
            int dimensions = -1;
            inputStream = new BufferedReader(new FileReader(file));
            while ((line = inputStream.readLine()) != null) {
                String[] split = removeEmpty(line.split(delim.readDelimeter()));
                if (split.length == 0) {
                    throw new IOException(file + " contains an empty line");
                }
                if (dimensions != -1 && split.length != dimensions) {
                    throw new IOException(file + " contains vectors of different dimensions.");
                }
                dimensions = split.length;
                float[] data = new float[dimensions];
                for (int i = 0; i < dimensions; ++i) {
                    data[i] = Float.parseFloat(split[i]);
                }
                vectors.add(new IDVector(lineNum, data));
                ++lineNum;
            }
            if (vectors.size() < 2) {
                throw new IOException(file + " does not contain at least 2 vectors.");
            }
            System.out.println("loaded " + vectors.size() + " " + dimensions + "-dimensional vectors");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Vector[] retval = new Vector[vectors.size()];
        return vectors.toArray(retval);
    }

    private Vector[] loadReutersVectors() throws IOException {
        BufferedReader inputStream = null;
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        VectorStoreSparseRAM termVectors = new VectorStoreSparseRAM();
        vecDimension = 1000;
        ObjectVector.vecLength = vecDimension;
        seedLength = vecDimension / 10;
        try {
            String line;
            int lineNum = 1;
            int dimensions = -1;
            inputStream = new BufferedReader(new FileReader(file));
            int docID = -1;
            int termID = -1;
            float weight = 0.0f;
            float[] termVector = null;
            float[] docVector = null;
            while ((line = inputStream.readLine()) != null) {
                String[] split = line.split(" ");
                if (split.length == 0) {
                    throw new IOException(file + " contains an empty line");
                }
                if (split.length % 2 == 0) {
                    throw new IOException(file + " contains line with incorrect number of entries");
                }
                docID = Integer.parseInt(split[0]);
                docVector = new float[vecDimension];
                for (int i = 1; i < split.length; i += 2) {
                    termID = Integer.parseInt(split[i]);
                    weight = Float.parseFloat(split[i + 1]);
                    termVector = termVectors.getVector(termID);
                    if (termVector == null) {
                        termVectors.generateAndPutNewVector("" + termID, seedLength);
                        termVector = termVectors.getVector(termID);
                    }
                    for (int j = 0; j < vecDimension; j++) {
                        docVector[j] += termVector[j];
                    }
                }
                float norm = VectorUtils.scalarProduct(docVector, docVector);
                for (int i = 0; i < vecDimension; i++) {
                    docVector[i] /= norm;
                }
                vectors.add(new IDVector(docID, docVector));
            }
            if (vectors.size() < 2) {
                throw new IOException(file + " does not contain at least 2 vectors.");
            }
            System.out.println("loaded " + vectors.size() + " " + vecDimension + "-dimensional vectors");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Vector[] retval = new Vector[vectors.size()];
        return vectors.toArray(retval);
    }

    private int seedLength;

    private int vecDimension;

    private Delimeter delim;

    private String file;

    private int m;

    private RIKTree kt;
}

class Load extends Option implements KTreeProvider {

    public String flag() {
        return "l";
    }

    public List<String> incompatibleFlags() {
        List<String> b = new ArrayList<String>();
        b.add("b");
        return b;
    }

    public int params() {
        return 1;
    }

    public String addParams(List<String> params) {
        file = params.get(0);
        return null;
    }

    public void execute() throws Exception {
        System.out.println("Loading K-tree from " + file + ".");
        long start, end, insertTime;
        start = System.currentTimeMillis();
        ObjectInputStream objstream = new ObjectInputStream(new FileInputStream(file));
        Object object = objstream.readObject();
        objstream.close();
        end = System.currentTimeMillis();
        insertTime = end - start;
        System.out.println("Loading took " + insertTime / 1000.0 + " seconds.");
        kt = (RIKTree) object;
    }

    public RIKTree ktree() {
        return kt;
    }

    String file;

    RIKTree kt;
}

class Write extends Option {

    public Write(KTreeProvider ktp) {
        this.ktp = ktp;
    }

    public String flag() {
        return "w";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 1;
    }

    public String addParams(List<String> params) {
        file = params.get(0);
        return null;
    }

    public void execute() throws Exception {
        System.out.println("Saving K-tree to " + file + ".");
        long start, end, insertTime;
        start = System.currentTimeMillis();
        ObjectOutputStream objstream = new ObjectOutputStream(new FileOutputStream(file));
        objstream.writeObject(ktp.ktree());
        objstream.close();
        end = System.currentTimeMillis();
        insertTime = end - start;
        System.out.println("Saving took " + insertTime / 1000.0 + " seconds.");
    }

    private String file;

    private KTreeProvider ktp;
}

class WriteVector extends Option {

    WriteVector(KTreeProvider ktp, Delimeter delim) {
        this.ktp = ktp;
        this.delim = delim;
    }

    public String flag() {
        return "wv";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 2;
    }

    public String addParams(List<String> params) {
        file = params.get(0);
        try {
            level = Integer.parseInt(params.get(1));
        } catch (NumberFormatException e) {
            return "level is not an integer";
        }
        return null;
    }

    protected void execute() throws Exception {
        RIKTree kt = ktp.ktree();
        System.out.println("Saving vectors at K-tree level " + level + " to " + file + ".");
        if (level >= kt.levels() || level < 0) {
            System.out.println("Invalid level " + level + ". Levels must fall between 1 and # of K-tree levels - 1. A level of 0 indicates the lowest level.");
        } else {
            if (level == 0) {
                level = kt.levels() - 1;
            }
            VectorClusterWriter vcw = new VectorClusterWriter(file, delim.writeDelimeter());
            kt.visit(vcw, level);
            vcw.close();
        }
    }

    private int level;

    private String file;

    private KTreeProvider ktp;

    private Delimeter delim;
}

class WriteDataVector extends Option {

    WriteDataVector(KTreeProvider ktp, Delimeter delim) {
        this.ktp = ktp;
        this.delim = delim;
    }

    public String flag() {
        return "wd";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 2;
    }

    public String addParams(List<String> params) {
        file = params.get(0);
        try {
            level = Integer.parseInt(params.get(1));
        } catch (NumberFormatException e) {
            return "level is not an integer";
        }
        return null;
    }

    protected void execute() throws Exception {
        RIKTree kt = ktp.ktree();
        System.out.println("Saving data vectors grouped by K-tree level " + level + " to " + file + ".");
        if (level >= kt.levels() || level < 0) {
            System.out.println("Invalid level " + level + ". Levels must fall between 1 and # of K-tree levels - 1. A level of 0 indicates the lowest level.");
        } else {
            if (level == 0) {
                level = kt.levels() - 1;
            }
            IDVectorClusterWriter ivcw = new IDVectorClusterWriter(file, delim.writeDelimeter());
            kt.visitLeaves(ivcw, level);
            ivcw.close();
        }
    }

    private int level;

    private String file;

    private KTreeProvider ktp;

    private Delimeter delim;
}

class Print extends Option {

    public Print(KTreeProvider ktp) {
        this.ktp = ktp;
    }

    public String flag() {
        return "p";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 0;
    }

    public String addParams(List<String> params) {
        printing = true;
        return null;
    }

    protected void execute() throws Exception {
        RIKTree kt = ktp.ktree();
        if (kt != null && printing) {
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
    }

    private KTreeProvider ktp;

    private boolean printing = false;
}

class Search extends Option {

    public Search(KTreeProvider ktp) {
        this.ktp = ktp;
    }

    public String flag() {
        return "s";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 0;
    }

    public String addParams(List<String> params) {
        search = true;
        return null;
    }

    protected void execute() throws Exception {
        RIKTree kt = ktp.ktree();
        if (kt != null && search) {
            long start, end, searchTime;
            int steps = 10;
            List<Vector> vectors = new ArrayList<Vector>();
            kt.getAllVectors(vectors);
            int increment = vectors.size() / steps;
            start = System.currentTimeMillis();
            int recallErrors = 0;
            float averageRecallDistance = 0;
            float averageQuantizationDistance = 0;
            for (int i = 0; i < vectors.size(); ++i) {
                Vector found = kt.find(vectors.get(i));
                float distance = found.distance(vectors.get(i));
                averageRecallDistance += distance;
                if (distance > 0) {
                    ++recallErrors;
                }
                found = kt.findCodebook(vectors.get(i));
                distance = found.distance(vectors.get(i));
                averageQuantizationDistance += distance;
                if (i % increment == 0) {
                    System.out.println("searched " + i + " of " + vectors.size() + " vectors.");
                }
            }
            averageRecallDistance /= vectors.size();
            averageQuantizationDistance /= vectors.size();
            end = System.currentTimeMillis();
            searchTime = end - start;
            System.out.println("Searching took " + searchTime / 1000.0 + " seconds.");
            System.out.println("K-Tree search details:");
            System.out.println("\tvectors not found in tree: " + recallErrors);
            System.out.println("\trecall RMSE: " + Math.sqrt(averageRecallDistance));
            System.out.println("\tquantization RMSE: " + Math.sqrt(averageQuantizationDistance));
        }
    }

    private KTreeProvider ktp;

    private boolean search = false;
}

class Rearrange extends Option {

    public Rearrange(KTreeProvider ktp) {
        this.ktp = ktp;
    }

    public String flag() {
        return "r";
    }

    public List<String> incompatibleFlags() {
        return null;
    }

    public int params() {
        return 0;
    }

    public String addParams(List<String> params) {
        return null;
    }

    protected void execute() throws Exception {
        System.out.println("Rearranging K-Tree.");
        RIKTree kt = ktp.ktree();
        long start, end, rearrangeTime;
        start = System.currentTimeMillis();
        kt.rearrange();
        end = System.currentTimeMillis();
        rearrangeTime = end - start;
        System.out.println("Rearranging took " + rearrangeTime / 1000.0 + " seconds.");
    }

    KTreeProvider ktp;
}

public class KTreeMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            OptionFactory factory = new OptionFactory();
            factory.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class IDVectorClusterWriter implements ClusterVisitor {

    IDVectorClusterWriter(String fileName, String delim) throws IOException {
        os = new BufferedWriter(new FileWriter(fileName));
        this.delim = delim;
    }

    public void accept(Vector[] v, int count) throws Exception {
        if (count == 0) {
            return;
        }
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < count; ++i) {
            if (i != 0) {
                s.append(delim);
            }
            s.append(((IDVector) v[i]).id());
        }
        os.write(s.toString());
        os.newLine();
    }

    public void close() throws IOException {
        os.close();
    }

    @Override
    protected void finalize() throws Throwable {
        os.close();
    }

    private BufferedWriter os;

    private String delim;
}

class VectorClusterWriter implements ClusterVisitor {

    VectorClusterWriter(String fileName, String delim) throws IOException {
        os = new BufferedWriter(new FileWriter(fileName));
        this.delim = delim;
    }

    public void accept(Vector[] v, int count) throws Exception {
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; ++i) {
            StringBuffer s = new StringBuffer();
            Vector curr = v[i];
            for (int dim = 0; dim < curr.dimensions(); ++dim) {
                if (dim != 0) {
                    s.append(delim);
                }
                s.append(curr.get(dim));
            }
            os.write(s.toString());
            os.newLine();
        }
    }

    public void close() throws IOException {
        os.close();
    }

    @Override
    protected void finalize() throws Throwable {
        os.close();
    }

    private BufferedWriter os;

    private String delim;
}

class CounterVisitor implements ClusterVisitor {

    public void accept(Vector[] v, int count) throws Exception {
        ++nodes;
        vectors += count;
    }

    int nodes() {
        return nodes;
    }

    int vectors() {
        return vectors;
    }

    private int nodes;

    private int vectors;
}
