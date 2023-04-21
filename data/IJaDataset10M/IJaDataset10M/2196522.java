package common;

import java.io.*;

public class Settings {

    private static String settingsLocation = "Settings.txt";

    public static String getInformation(String title) {
        File file = new File(settingsLocation);
        if (file.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line;
                while ((line = in.readLine()) != null) {
                    int index = line.indexOf(title);
                    if (index != -1) {
                        in.close();
                        return line.substring(index + title.length());
                    }
                }
                in.close();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static void updateInformation(String title, String data) {
        File file = new File(settingsLocation);
        try {
            if (file.exists()) {
                File tempFile = new File("Settings.temp");
                BufferedWriter outputTemp = null;
                BufferedReader input = null;
                try {
                    outputTemp = new BufferedWriter(new FileWriter(tempFile));
                    input = new BufferedReader(new FileReader(file));
                    String line;
                    boolean found = false;
                    while ((line = input.readLine()) != null) {
                        if (line.indexOf(title) == -1) outputTemp.write(line); else {
                            outputTemp.write(title + data);
                            found = true;
                        }
                        outputTemp.newLine();
                        outputTemp.flush();
                    }
                    if (found == false) {
                        outputTemp.write(title + data);
                        outputTemp.newLine();
                        outputTemp.flush();
                    }
                } finally {
                    if (outputTemp != null) {
                        outputTemp.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                }
                file.delete();
                tempFile.renameTo(file);
            } else {
                file.createNewFile();
                BufferedWriter output = null;
                try {
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(title + data);
                    output.flush();
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
