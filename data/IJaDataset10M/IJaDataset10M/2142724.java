package playground.scnadine.GPSProcessResultsMapMatching;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.matsim.gbl.Gbl;

public class GPSParseAndWriteMatchedLinks {

    private GPSMapMatchingCoords mapMatchingCoords;

    private File targetFile;

    public GPSParseAndWriteMatchedLinks(GPSMapMatchingCoords mapMatchingCoords) {
        this.mapMatchingCoords = mapMatchingCoords;
        this.targetFile = new File(Gbl.getConfig().findParam("GPSProcessResultsMapMatching", "targetFile"));
    }

    public void run(File inputFileMatchedLinks) throws IOException {
        BufferedReader oReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileMatchedLinks)));
        BufferedWriter out = new BufferedWriter(new FileWriter(this.targetFile));
        out.write("personID\tripID\tstageID\terror\tlinkID");
        out.newLine();
        oReader.readLine();
        String lineFromInputFile;
        int linesInInput = 0;
        int linesInOutput = 0;
        while ((lineFromInputFile = oReader.readLine()) != null) {
            String[] entries = lineFromInputFile.split(",");
            int linkID = (int) Double.parseDouble(entries[3]);
            int startCoordID = (int) Double.parseDouble(entries[4]);
            int endCoordID = (int) Double.parseDouble(entries[5]);
            double error = Double.parseDouble(entries[6]);
            GPSMapMatchingCoord startCoord = this.mapMatchingCoords.getGPSMapMatchingCoord(startCoordID - 1);
            GPSMapMatchingCoord endCoord = this.mapMatchingCoords.getGPSMapMatchingCoord(endCoordID - 1);
            if (startCoord.isSameStage(endCoord)) {
                out.write(startCoord.getPersonID() + "\t" + startCoord.getTripID() + "\t" + startCoord.getStageID() + "\t" + error + "\t" + linkID);
                out.newLine();
                linesInOutput++;
            } else {
                System.out.println("Different stages in input line " + (linesInInput + 1));
                out.write(startCoord.getPersonID() + "\t" + startCoord.getTripID() + "\t" + startCoord.getStageID() + "\t" + error + "\t" + linkID);
                out.newLine();
                linesInOutput++;
                out.write(endCoord.getPersonID() + "\t" + endCoord.getTripID() + "\t" + endCoord.getStageID() + "\t" + error + "\t" + linkID);
                out.newLine();
                linesInOutput++;
            }
            linesInInput++;
        }
        out.close();
        System.out.println("Number of matched links read: " + linesInInput);
        System.out.println("Number of matched links written: " + linesInOutput);
    }
}
