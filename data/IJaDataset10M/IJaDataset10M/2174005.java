package misc;

import java.util.*;
import java.io.*;
import bio301.dataproc.*;

public class BlatReaderWithIntronFilter extends MappingResultIteratorAdaptor {

    public static String methodName = "intronFilterBLAT";

    private BlatReader blatIterator = null;

    protected Set intronFilterSet = null;

    private float currentIdentity = (float) 0;

    private String currentReadID = null;

    private int currentReadLength = 0;

    private ArrayList currentBlatIteration = null;

    private ArrayList nextBlatIteration = null;

    public BlatReaderWithIntronFilter(String inParameterStr) throws FileNotFoundException {
        String tokens[] = inParameterStr.split(System.getProperty("path.separator"));
        String blatFilename = tokens[0];
        String filterFilename = tokens[1];
        blatIterator = new BlatReader(blatFilename);
        intronFilterSet = getIntronFilterSet(filterFilename);
        getNextNonemptyFilteredBlatIteration();
    }

    protected void filter(ArrayList list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            AlignmentRecord record = (AlignmentRecord) list.get(i);
            for (int j = 0; j < record.numBlocks - 1; j++) {
                int testIntronStart = record.tStarts[j] + record.tBlockSizes[j] - 1;
                int testIntronStop = record.tStarts[j + 1];
                GenomeInterval testIntron = new GenomeInterval(record.chr, testIntronStart, testIntronStop);
                if (intronFilterSet.contains(testIntron)) {
                    list.remove(i);
                    break;
                }
            }
        }
    }

    private ArrayList getNextNonemptyFilteredBlatIteration() {
        while (blatIterator.hasNext()) {
            ArrayList testIteration = (ArrayList) blatIterator.next();
            filter(testIteration);
            if (testIteration.size() > 0) {
                nextBlatIteration = testIteration;
                return testIteration;
            }
        }
        nextBlatIteration = null;
        return null;
    }

    private Set getIntronFilterSet(String filename) throws FileNotFoundException {
        Set ansSet = new HashSet();
        BufferedReader fr = new BufferedReader(new FileReader(filename));
        try {
            while (fr.ready()) {
                String line = fr.readLine();
                if (line.startsWith("#")) continue;
                String tokensLv1[] = line.split("\t");
                String tokensLv2[] = tokensLv1[0].split("[:.]+");
                int start = Integer.parseInt(tokensLv2[1]);
                int stop = Integer.parseInt(tokensLv2[2]);
                GenomeInterval interval = new GenomeInterval(tokensLv2[0].toLowerCase().intern(), start, stop);
                ansSet.add(interval);
            }
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return ansSet;
    }

    public float getBestIdentity() {
        return currentIdentity;
    }

    public String getReadID() {
        return currentReadID;
    }

    public int getReadLength() {
        return currentReadLength;
    }

    public int getNumMatch() {
        return currentBlatIteration.size();
    }

    public boolean hasNext() {
        if (nextBlatIteration == null) {
            return false;
        } else {
            return true;
        }
    }

    public Object next() {
        currentBlatIteration = nextBlatIteration;
        currentIdentity = recomputeBestIdentity();
        currentReadID = blatIterator.getReadID();
        currentReadLength = blatIterator.getReadLength();
        getNextNonemptyFilteredBlatIteration();
        return currentBlatIteration;
    }

    private float recomputeBestIdentity() {
        float ans = 0;
        for (int i = 0; i < currentBlatIteration.size(); i++) {
            AlignmentRecord record = (AlignmentRecord) currentBlatIteration.get(i);
            if (record.identity > ans) ans = record.identity;
        }
        return ans;
    }

    public static void main(String[] args) throws FileNotFoundException {
        for (BlatReaderWithIntronFilter iterator = new BlatReaderWithIntronFilter(args[0]); iterator.hasNext(); ) {
            ArrayList mappingRecords = (ArrayList) iterator.next();
            System.out.println(iterator.getReadID() + "\t" + iterator.getReadLength() + "\t" + mappingRecords.size() + "\t" + iterator.getNumMatch() + "\t" + iterator.getBestIdentity());
        }
    }
}
