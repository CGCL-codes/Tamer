package taustratego;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ThemesManager {

    public String[] availableThemse;

    public String[] themesNames;

    private String selectedTheme;

    private static class ThemesFileFilter implements FilenameFilter {

        public boolean accept(File dir, String name) {
            File f = new File(dir, name + File.separator + "theme");
            if (f.exists()) System.out.println("found new theme: " + f);
            return f.exists();
        }
    }

    public ThemesManager() {
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration<URL> files = null;
        try {
            files = cl.getResources("images");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't get the images resources!!!");
        }
        while (files.hasMoreElements()) {
            URL url = (URL) files.nextElement();
        }
        availableThemse = new String[3];
        availableThemse[0] = new String("Classic");
        availableThemse[1] = new String("HumansAliens");
        availableThemse[2] = new String("IsraelWorld");
        themesNames = new String[3];
        themesNames[0] = new String("Classic");
        themesNames[1] = new String("Humans vs. Aliens");
        themesNames[2] = new String("Israel vs. Arab World");
        selectedTheme = availableThemse[0];
    }

    public void setTheme(String newThemeName) {
        if (selectedTheme.equals(newThemeName)) return;
        selectedTheme = newThemeName;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public String getImagesDirectory() {
        return "images" + "/" + selectedTheme + "/";
    }
}
