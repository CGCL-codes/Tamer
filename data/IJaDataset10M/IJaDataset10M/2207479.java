package jhat_server;

import java.util.ArrayList;
import java.io.*;

public class IconFilter {

    private static String rootDir;

    private static ArrayList<String[]> icons = new ArrayList<String[]>();

    public static void readFromFile(String filename) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String line;
            String root = "";
            while ((line = file.readLine()) != null) {
                if (line.indexOf("#") != 0) {
                    if (line.indexOf("<root>") == 0) {
                        root = line.substring(6);
                    } else {
                        String[] currIcon = new String[2];
                        currIcon[0] = line;
                        currIcon[1] = root + file.readLine();
                        icons.add(currIcon);
                    }
                }
            }
            Log.message("Found " + icons.size() + " icons");
        } catch (FileNotFoundException e) {
            Log.message("icon-configuration-file not found: " + filename);
        } catch (IOException e) {
            Log.message(e.toString());
        }
    }

    public static String createIcons(String str) {
        for (int i = 0; i < icons.size(); i++) str = str.replace(icons.get(i)[0], "<img src=\"" + icons.get(i)[1] + "\">");
        return str;
    }
}
