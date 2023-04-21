package playground.scnadine.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;

public class DeriveNumberOfLinksInChoiceSets {

    static File[] allFiles;

    public static void main(String[] args) throws IOException {
        Config config = ConfigUtils.loadConfig(args[0]);
        final String CONFIG_MODULE = "Tools";
        System.out.println(config.getModule(CONFIG_MODULE));
        File sourcedir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        allFiles = sourcedir.listFiles();
        File targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
        try {
            for (File file : allFiles) {
                String fileName = file.getName();
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                BufferedWriter outR = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "nofLinksRoutes_" + fileName)));
                outR.write("personId\ttripId\tstageId\trouteId\tchosenRouteId\tnofLinks" + "\n");
                BufferedWriter outCS = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "nofLinksCS_" + fileName)));
                outCS.write("personId\ttripId\tstageId\tnofLinks" + "\n");
                String lastPersonId = "none", lastTripId = "none", lastStageId = "none";
                ArrayList<String> linksInCS = new ArrayList<String>();
                in.readLine();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] entries = inputLine.split("\t");
                    int nofLinksInRoute = entries.length - 10;
                    outR.write(entries[0] + "\t" + entries[1] + "\t" + entries[2] + "\t" + entries[7] + "\t" + entries[6] + "\t" + nofLinksInRoute + "\n");
                    if (lastPersonId.equals("none") || (entries[0].equals(lastPersonId) && entries[1].equals(lastTripId) && entries[2].equals(lastStageId))) {
                        for (int i = 9; i < entries.length - 1; i++) {
                            if (!linksInCS.contains(entries[i])) {
                                linksInCS.add(entries[i]);
                            }
                        }
                    } else {
                        outCS.write(lastPersonId + "\t" + lastTripId + "\t" + lastStageId + "\t" + linksInCS.size() + "\n");
                        linksInCS.clear();
                        for (int i = 9; i < entries.length - 1; i++) {
                            linksInCS.add(entries[i]);
                        }
                    }
                    lastPersonId = entries[0];
                    lastTripId = entries[1];
                    lastStageId = entries[2];
                }
                outCS.write(lastPersonId + "\t" + lastTripId + "\t" + lastStageId + "\t" + linksInCS.size() + "\n");
                outR.close();
                outCS.close();
            }
        } catch (IOException e) {
            System.out.println("Error while writing number of links in routes and choice sets.");
        }
        System.out.println("Done deriving number of links in routes and choice sets.");
    }
}
