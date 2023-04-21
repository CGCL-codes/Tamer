package gelations;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * This class reads datafiles containing real-world test suite 
 * data.
 * 
 * @author conrada
 * @see ConfigurationReader.java
 */
public class ConfigurationReaderReal implements ConfigurationReader, Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<CaseTest> readCaseTests(String coverageFileName, String timeFileName) {
        ArrayList<CaseTest> caseTests = new ArrayList<CaseTest>();
        HashMap<Integer, ArrayList<Integer>> requirementMap = new HashMap<Integer, ArrayList<Integer>>();
        int thisCase, thisReq;
        double thisTime;
        try {
            ArrayList<Integer> requirements;
            Scanner coverage = new Scanner(new BufferedReader(new FileReader(coverageFileName)));
            Scanner time = new Scanner(new BufferedReader(new FileReader(timeFileName)));
            coverage.nextLine();
            time.nextLine();
            while (coverage.hasNextInt()) {
                thisCase = coverage.nextInt();
                thisReq = coverage.nextInt();
                if (!requirementMap.containsKey(thisCase)) {
                    requirementMap.put(thisCase, new ArrayList<Integer>());
                }
                requirementMap.get(thisCase).add(thisReq);
            }
            while (time.hasNextInt()) {
                thisCase = time.nextInt();
                thisTime = time.nextDouble();
                requirements = requirementMap.get(thisCase);
                int[] reqs;
                if (requirements != null) {
                    reqs = new int[requirements.size()];
                    for (int i = 0; i < reqs.length; i++) {
                        reqs[i] = requirements.get(i);
                    }
                } else {
                    reqs = new int[0];
                }
                caseTests.add(new CaseTest(thisTime, reqs, thisCase));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return caseTests;
    }

    public ArrayList<Integer> readSeeds(String seedFileName) {
        Scanner seeds;
        ArrayList<Integer> seedValues = new ArrayList<Integer>();
        try {
            seeds = new Scanner(new BufferedReader(new FileReader(seedFileName)));
            seeds.skip("Seeds");
            while (seeds.hasNextInt()) {
                seedValues.add(seeds.nextInt());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return seedValues;
    }

    public String getSetConfig(String coverageFileName) {
        return coverageFileName.substring(coverageFileName.length() - 14, coverageFileName.length() - 12);
    }
}
