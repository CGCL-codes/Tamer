package utils.logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author mathieunancel
 *
 */
public class Merger {

    static String folderName = "FinalLogs/";

    static String[] logFolders = { "cinematics/", "trials/" };

    static String masterLog = "masterLog.csv";

    static int lineAvoided = 1;

    static int lineIndex = 0;

    static final String Separator = "\t";

    static ArrayList<String[]> dataToBeWritten = new ArrayList<String[]>();

    static BufferedWriter writer;

    static BufferedReader reader;

    static String participant_session;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            if (folderName.length() > 0) {
                for (String lfolder : logFolders) {
                    File directory = new File(folderName + lfolder);
                    writer = new BufferedWriter(new FileWriter(folderName + lfolder + masterLog));
                    for (File f : directory.listFiles()) {
                        if (!f.getAbsolutePath().contains(masterLog) && !f.getName().startsWith(".")) {
                            reader = new BufferedReader(new FileReader(f));
                            System.out.println(f.getAbsolutePath());
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("#")) {
                                    if (line.contains("Participant")) {
                                        participant_session = line.substring(15);
                                    }
                                } else {
                                    if (lineIndex != lineAvoided) {
                                        String parts[];
                                        if (f.getName().endsWith(".log")) {
                                            parts = line.split("\t");
                                        } else {
                                            parts = line.split(",");
                                        }
                                        if (lfolder.contains("trial")) {
                                            participant_session = parts[1];
                                        }
                                        for (String p : parts) {
                                            writer.append(p + ",");
                                        }
                                        writer.append(participant_session.split("-")[0]);
                                        writer.append("\n");
                                    } else System.err.println("Line " + lineIndex + " avoided.");
                                    lineIndex++;
                                }
                            }
                        } else {
                            System.err.println("Bad file name : " + f.getAbsolutePath());
                        }
                    }
                    writer.flush();
                }
                writer.close();
            } else {
                System.err.println("Empty folder name.");
            }
            System.out.println("\nDone");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
