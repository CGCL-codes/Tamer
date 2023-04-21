package de.schmaller.apps.TreeDataViewer;

import java.util.Properties;

public abstract class AppData {

    private static Properties data = getData();

    public static String getProperty(String key) {
        return data.getProperty(key);
    }

    private static Properties getData() {
        Properties data = new Properties();
        data.setProperty("name", "TreeDataViewer");
        data.setProperty("version", "1.1");
        data.setProperty("contact", "<a href='mailto:dev@schmaller.de'>dev@schmaller.de</a>");
        data.setProperty("author", "Daniel Str�nger");
        data.setProperty("homepage", "<a href='https://sourceforge.net/projects/treedataviewer/'>https://sourceforge.net/projects/treedataviewer/</a>");
        data.setProperty("license", "<a href='doc/License.html'>Q Public License</a>");
        return data;
    }
}
