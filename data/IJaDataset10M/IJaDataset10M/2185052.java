package playground.ciarif.modechoice_old;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.matsim.basic.v01.BasicLeg;
import org.matsim.basic.v01.BasicPlanImpl;
import org.matsim.plans.Person;
import org.matsim.plans.Plan;
import org.matsim.plans.algorithms.PersonAlgorithm;

/**
 * @author ciarif
 *
 */
public class ModeChoiceAnalyzer extends PersonAlgorithm {

    TreeMap<String, Integer> modeStatistics = new TreeMap<String, Integer>();

    public ModeChoiceAnalyzer() {
        super();
    }

    @Override
    public void run(Person person) {
        Plan plan = person.getSelectedPlan();
        BasicPlanImpl.LegIterator legIt = plan.getIteratorLeg();
        while (legIt.hasNext()) {
            BasicLeg leg = legIt.next();
            String mode = leg.getMode();
            int modeCount = 0;
            if (modeStatistics.containsKey(mode)) {
                modeCount = modeStatistics.get(mode);
            }
            modeCount++;
            modeStatistics.put(mode, modeCount);
        }
    }

    public void printInformation() {
        Iterator<Map.Entry<String, Integer>> modeIt = modeStatistics.entrySet().iterator();
        while (modeIt.hasNext()) {
            Map.Entry entry = modeIt.next();
            System.out.println("There are " + entry.getValue() + " modes of " + " type " + entry.getKey());
        }
    }

    public void writeStatistics(String filename) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(filename));
            Iterator<Map.Entry<String, Integer>> modeIt = modeStatistics.entrySet().iterator();
            while (modeIt.hasNext()) {
                Map.Entry entry = modeIt.next();
                out.write(entry.getKey() + ";" + entry.getValue() + "\n");
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }
}
