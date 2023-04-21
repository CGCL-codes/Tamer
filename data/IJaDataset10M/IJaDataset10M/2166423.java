package ru.whitesoft;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DesktopFixed {

    private static DesktopFixed instance = new DesktopFixed();

    private DesktopFixed() {
    }

    public static DesktopFixed getDesktopFixed() {
        if (instance == null) {
            instance = new DesktopFixed();
        }
        return instance;
    }

    public void open(final File file) throws IOException {
        if (System.getProperty("os.name", "").toLowerCase().contains("windows")) {
            if (file.isDirectory()) Runtime.getRuntime().exec("explorer \"" + file.getAbsolutePath() + "\""); else if (file.isFile()) Runtime.getRuntime().exec("cmd /C \"\"" + file.getAbsolutePath() + "\"\"");
        } else if (System.getProperty("os.name", "").toLowerCase().contains("linux")) {
            try {
                Runtime.getRuntime().exec(new String[] { "konqueror", file.getAbsolutePath() });
            } catch (IOException e2) {
                if (file.getName().endsWith(".desktop")) {
                    openDesktopFile(file);
                } else {
                    Runtime.getRuntime().exec(new String[] { "xdg-open", file.getAbsolutePath() });
                }
            }
        } else {
            Desktop.getDesktop().open(file);
        }
    }

    /**
     * opens desktop entry specification describes desktop entries - files
     * describing information about an application such as the name, icon, and
     * description. See
     * <a href="http://www.freedesktop.org/wiki/Specifications/desktop-entry-spec">Desktop Entry Specification</a>
     * for details.
     */
    private void openDesktopFile(final File file) throws FileNotFoundException, IOException {
        Map<String, String> entries = new HashMap<String, String>();
        {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            try {
                while (true) {
                    String line = fileReader.readLine();
                    if (line == null) break;
                    int equalPos = line.indexOf('=');
                    if (equalPos == -1) continue;
                    entries.put(line.substring(0, equalPos), line.substring(equalPos + 1));
                }
            } finally {
                fileReader.close();
            }
        }
        if (entries.containsKey("URL")) {
            Desktop.getDesktop().browse(URI.create(entries.get("URL")));
        } else if (entries.containsKey("Exec") || entries.containsKey("TryExec")) {
            try {
                if (entries.containsKey("Exec")) Runtime.getRuntime().exec(resolveExecValue(entries, file, entries.get("Exec")));
            } catch (IOException e3) {
                if (entries.containsKey("TryExec")) Runtime.getRuntime().exec(resolveExecValue(entries, file, entries.get("TryExec")));
            }
        }
    }

    /**
     * Part of {@link #openDesktopFile(File)}.
     */
    private String resolveExecValue(Map<String, String> desktopFileEntries, File desktopFile, String execValue) {
        String result = execValue.replaceAll("%f|%F|%u|%U|%d|%D|%n|%N|%v|%m", "").replaceAll("%c", "").replaceAll("%i", desktopFileEntries.containsKey("Icon") ? "--icon " + desktopFileEntries.get("Icon") : "").replaceAll("%k", desktopFile.getAbsolutePath());
        return result;
    }
}
