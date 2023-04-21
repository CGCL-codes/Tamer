package playground.scnadine.gpsProcessingV2.GPSPersonAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSPerson;
import playground.scnadine.gpsProcessingV2.GPSPersons;

public class GPSPersonWriteOverallStatistics extends GPSPersonAlgorithm {

    private File targetdir;

    public GPSPersonWriteOverallStatistics(Config config, String CONFIG_MODULE) {
        super();
        targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
    }

    @Override
    public void run(GPSPersons persons) {
        int numberOfPersons = persons.getPersons().size();
        long numberOfPoints = 0;
        double numberOfDays = 0, numberOfStages = 0, numberOfStopPoints = 0;
        double totalStageDistance = 0, totalStageDuration = 0, totalStopPointDuration = 0;
        Iterator<GPSPerson> it = persons.getPersons().iterator();
        while (it.hasNext()) {
            GPSPerson person = it.next();
            numberOfPoints += person.getNumberOfGPSPoints();
            numberOfDays += person.getNumberOfDays();
            numberOfStages += person.getNumberOfStages();
            numberOfStopPoints += person.getNumberOfStopPoints();
            totalStageDistance += person.getTotalStageDistance();
            totalStageDuration += person.getTotalStageDuration();
            totalStopPointDuration += person.getTotalStopPointDuration();
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "OverallStatistics.txt")));
            out.write("NumberOfPersons: " + numberOfPersons + "\n");
            out.write("TotalNumberOfGPSPoints\t" + numberOfPoints + "\n");
            out.write("AvNumberOfGPSPoints\t" + numberOfPoints / numberOfPersons + "\n");
            out.write("TotalNumberOfGPSDays\t" + numberOfDays + "\n");
            out.write("AvNumberOfGPSDays\t" + numberOfDays / numberOfPersons + "\n");
            out.write("TotalNumberOfStages\t" + numberOfStages + "\n");
            out.write("AvNumberOfStagesPerPerson\t" + numberOfStages / numberOfPersons + "\n");
            out.write("AvNumberOfStagesPerDay\t" + numberOfStages / numberOfDays + "\n");
            out.write("TotalNumberOfStopPoints\t" + numberOfStopPoints + "\n");
            out.write("AvNumberOfStopPointsPerPerson\t" + numberOfStopPoints / numberOfPersons + "\n");
            out.write("AvNumberOfStopPointsPerDay\t" + numberOfStopPoints / numberOfDays + "\n");
            out.write("AverageStageLength\t" + totalStageDistance / numberOfStages + "\n");
            out.write("AverageStageDuration\t" + totalStageDuration / numberOfStages + "\n");
            out.write("AverageStopPointDuration\t" + totalStopPointDuration / numberOfStopPoints + "\n");
            out.newLine();
            out.close();
        } catch (IOException e) {
            System.out.println("Error while writing OverallStatistics.txt");
        }
    }
}
