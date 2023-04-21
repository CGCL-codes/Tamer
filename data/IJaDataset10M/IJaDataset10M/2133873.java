package de.hattrickorganizer.tools.extension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExtensionManager {

    public static void deleteLineup(String lineupName) {
        LineupCreator.deleteLineup(lineupName);
    }

    public static void extractLineup(String lineupName) {
        LineupCreator.extractLineup(lineupName);
    }

    public static void modelUpdate() {
        PlayerCreator.extractActual();
        EconomyCreator.extractActual();
        TeamCreator.extractActual();
    }

    public static void trainingUpdate() {
        PlayerCreator.extractHistoric();
    }

    public static void economyUpate() {
        EconomyCreator.extractHistoric();
    }

    public static void createDirFile() {
        try {
            File userdir = new File(System.getProperty("user.home"));
            File f = new File(userdir, "ho.dir");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(System.getProperty("user.dir"));
            bw.flush();
            bw.close();
        } catch (IOException e) {
        }
    }
}
