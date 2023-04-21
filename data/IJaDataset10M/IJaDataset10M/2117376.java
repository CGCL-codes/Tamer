package de.cabanis.unific.test.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import de.cabanis.unific.analysis.algorithm.IStringBasedAlgorithm;

/**
 * ToDo: JavaDoc
 * 
 * @author Nicolas Cabanis
 * @version $revision$
 */
public class TestResult {

    private Logger logger = Logger.getLogger(getClass());

    private Set<IStringBasedAlgorithm> algorithms = new HashSet<IStringBasedAlgorithm>();

    private Set<TestResultEntry> resultEntries = new HashSet<TestResultEntry>();

    private Map<Entry, Map<IStringBasedAlgorithm, TestResultEntry>> entryToResultEntry = new HashMap<Entry, Map<IStringBasedAlgorithm, TestResultEntry>>();

    public void addResultEntry(TestResultEntry resultEntry) {
        algorithms.add(resultEntry.getAlgorithm());
        resultEntries.add(resultEntry);
        Map<IStringBasedAlgorithm, TestResultEntry> resultsForEntry = entryToResultEntry.get(resultEntry.getEntry());
        if (resultsForEntry == null) {
            resultsForEntry = new HashMap<IStringBasedAlgorithm, TestResultEntry>();
            entryToResultEntry.put(resultEntry.getEntry(), resultsForEntry);
        }
        resultsForEntry.put(resultEntry.getAlgorithm(), resultEntry);
    }

    public Iterator allAlgorithms() {
        return algorithms.iterator();
    }

    public Iterator resultEntries() {
        return resultEntries.iterator();
    }

    public void writeToCSV(File csv) {
        IStringBasedAlgorithm[] algorithmsArray = algorithms.toArray(new IStringBasedAlgorithm[algorithms.size()]);
        String titles = "";
        for (int i = 0; i < algorithmsArray.length; i++) {
            titles += algorithmsArray[i].getAbbreviation() + ":";
            titles += "rating:";
        }
        titles += "best:rating:match:category:string1:string2";
        List<String> lines = new ArrayList<String>();
        String line = "";
        String bestAlgorithm = "";
        int bestRating = 0;
        Map resultsForEntry = null;
        TestResultEntry resultEntry = null;
        for (Iterator iterator = entryToResultEntry.values().iterator(); iterator.hasNext(); ) {
            resultsForEntry = (Map) iterator.next();
            for (int i = 0; i < algorithmsArray.length; i++) {
                resultEntry = (TestResultEntry) resultsForEntry.get(algorithmsArray[i]);
                if (resultEntry == null) {
                    line += "x:x:";
                } else {
                    line += resultEntry.getResult() + ":";
                    line += resultEntry.getRating() + ":";
                }
                if (resultEntry.getRating() > bestRating) {
                    bestRating = resultEntry.getRating();
                    bestAlgorithm = resultEntry.getAlgorithm().getAbbreviation();
                }
            }
            line += bestAlgorithm + ":" + bestRating + ":" + resultEntry.getEntry().isMatch() + ":" + resultEntry.getEntry().getCategory().getName() + ":" + resultEntry.getEntry().getString1() + ":" + resultEntry.getEntry().getString2();
            lines.add(line);
            line = "";
            bestAlgorithm = "";
            bestRating = 0;
        }
        try {
            FileWriter writer = new FileWriter(csv);
            writer.write(titles + "\r\n");
            for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
                line = (String) iterator.next();
                writer.write(line + "\r\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
