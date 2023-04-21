package playground.benjamin.scenarios.munich.testroad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.matsim.core.utils.io.tabularFileParser.TabularFileParser;
import org.matsim.core.utils.io.tabularFileParser.TabularFileParserConfig;
import playground.benjamin.utils.CheckingTabularFileHandler;

/**
 * @author benjamin
 *
 */
public class CalculateAvgTravelTimesForTestVehicles {

    static String testVehicleDataPath = "../../detailedEval/teststrecke/testVehicle/";

    static String dataFileName = "_travelTimes.csv";

    static String testVehicleSimPath = "../../detailedEval/teststrecke/sim/output/";

    static String simFileName = "enterTimes2travelTimes.txt";

    static Integer[] days = { 20060127, 20060131, 20090317, 20090318, 20090319, 20090707, 20090708, 20090709 };

    public static void main(String[] args) {
        calculateAvgTravelTimesFromData(testVehicleDataPath, dataFileName, days);
        calculateAvgTravelTimesFromSim(testVehicleSimPath, simFileName, days);
    }

    private static void calculateAvgTravelTimesFromData(String testVehicleDataPath, String dataFileName, Integer[] days) {
        SortedMap<Integer, SortedMap<Integer, Integer>> realData = new TreeMap<Integer, SortedMap<Integer, Integer>>();
        for (int day : days) {
            SortedMap<Integer, Integer> inflowTimes2TravelTimes = getInflowTimes2TravelTimes(testVehicleDataPath + day + dataFileName);
            realData.put(day, inflowTimes2TravelTimes);
        }
        SortedMap<Integer, Double> hours2AvgTravelTimes = calculateAvgTravelTimesPerHour(realData);
        writeAvgTravelTimesPerHour(hours2AvgTravelTimes, testVehicleDataPath);
    }

    private static void calculateAvgTravelTimesFromSim(String testVehicleSimPath, String simFileName, Integer[] days) {
        SortedMap<Integer, SortedMap<Integer, Integer>> simData = new TreeMap<Integer, SortedMap<Integer, Integer>>();
        for (int day : days) {
            SortedMap<Integer, Integer> inflowTimes2TravelTimes = getInflowTimes2TravelTimes(testVehicleSimPath + day + "/" + simFileName);
            simData.put(day, inflowTimes2TravelTimes);
        }
        SortedMap<Integer, Double> hours2AvgTravelTimes = calculateAvgTravelTimesPerHour(simData);
        writeAvgTravelTimesPerHour(hours2AvgTravelTimes, testVehicleSimPath);
    }

    private static SortedMap<Integer, Integer> getInflowTimes2TravelTimes(String inputFile) {
        final SortedMap<Integer, Integer> inflowTimes2TravelTimes = new TreeMap<Integer, Integer>();
        TabularFileParserConfig tabFileParserConfig = new TabularFileParserConfig();
        tabFileParserConfig.setFileName(inputFile);
        tabFileParserConfig.setDelimiterTags(new String[] { ";" });
        new TabularFileParser().parse(tabFileParserConfig, new CheckingTabularFileHandler() {

            private static final int INFLOWTIME = 0;

            private static final int TRAVELTIME = 1;

            public void startRow(String[] row) {
                first = false;
                numColumns = row.length;
                check(row);
                addDepartureTime(row);
            }

            private void addDepartureTime(String[] row) {
                Integer inflowTime = new Integer(row[INFLOWTIME]);
                Integer travelTime = new Integer(row[TRAVELTIME]);
                inflowTimes2TravelTimes.put(inflowTime, travelTime);
            }
        });
        System.out.println(inflowTimes2TravelTimes);
        System.out.println("=====");
        return inflowTimes2TravelTimes;
    }

    private static SortedMap<Integer, Double> calculateAvgTravelTimesPerHour(SortedMap<Integer, SortedMap<Integer, Integer>> data) {
        SortedMap<Integer, Double> hours2TravelTimes = new TreeMap<Integer, Double>();
        for (int i = 6; i < 20; i++) {
            List<Integer> travelTimes = new ArrayList<Integer>();
            Integer lowerBound = i * 3600;
            Integer upperBound = (i + 1) * 3600;
            for (Entry<Integer, SortedMap<Integer, Integer>> entry : data.entrySet()) {
                SortedMap<Integer, Integer> inflowTimes2TravelTimes = entry.getValue();
                for (Entry<Integer, Integer> entroy : inflowTimes2TravelTimes.entrySet()) {
                    Integer inflowTime = entroy.getKey();
                    Integer travelTime = entroy.getValue();
                    if (inflowTime > lowerBound && inflowTime <= upperBound) {
                        travelTimes.add(travelTime);
                    } else {
                    }
                }
            }
            System.out.println(travelTimes);
            Integer sum = 0;
            for (int iterator : travelTimes) {
                sum = sum + iterator;
            }
            Integer mediumHour = (lowerBound + upperBound) / 2;
            Double avgTravelTime = (double) sum / (double) travelTimes.size();
            hours2TravelTimes.put(mediumHour, avgTravelTime);
        }
        return hours2TravelTimes;
    }

    private static void writeAvgTravelTimesPerHour(SortedMap<Integer, Double> hours2AvgTravelTimes, String outputPath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath + "averageTravelTimes.txt")));
            bw.write("hour" + "\t" + "avgTravelTime");
            bw.newLine();
            for (Entry<Integer, Double> entry : hours2AvgTravelTimes.entrySet()) {
                Integer hour = entry.getKey();
                Double avgTravelTime = entry.getValue();
                bw.write(hour.toString());
                bw.write("\t");
                bw.write(avgTravelTime.toString());
                bw.newLine();
            }
            bw.close();
            System.out.println("Wrote average travel times to " + outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
