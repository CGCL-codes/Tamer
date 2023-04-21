package org.snipsnap.database;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.radeox.util.logging.Logger;
import snipsnap.api.config.Configuration;
import snipsnap.api.snip.SnipSpace;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThemeHelper {

    public static final String THEME_PREFIX = "SnipSnap/themes/";

    public static final int FILES = 0;

    public static final int DOCUMENTS = 1;

    public static final int CONTENT = 2;

    public static Map getInstalledThemes() {
        SnipSpace space = (snipsnap.api.snip.SnipSpace) snipsnap.api.container.Components.getComponent(snipsnap.api.snip.SnipSpace.class);
        snipsnap.api.snip.Snip[] themeSnips = space.match(THEME_PREFIX);
        Map themes = new HashMap();
        for (int t = 0; t < themeSnips.length; t++) {
            String name = themeSnips[t].getName();
            if (name.indexOf('/', THEME_PREFIX.length()) == -1) {
                themes.put(name.substring(THEME_PREFIX.length()), themeSnips[t]);
            }
        }
        return themes;
    }

    public static Map getThemeDocuments(Configuration config, int valueType) {
        File themeDir = new File(config.getWebInfDir(), "themes");
        File[] files = themeDir.listFiles(new FilenameFilter() {

            public boolean accept(File file, String s) {
                return s.endsWith(".snip");
            }
        });
        Map themeDocs = new HashMap();
        SAXReader saxReader = new SAXReader();
        for (int f = 0; f < files.length; f++) {
            try {
                Document themeDoc = saxReader.read(new FileReader(files[f]));
                Iterator it = themeDoc.getRootElement().elementIterator("snip");
                while (it.hasNext()) {
                    Element snipEl = (Element) it.next();
                    String tagName = snipEl.element("name").getText();
                    if (tagName.startsWith(THEME_PREFIX) && tagName.indexOf('/', THEME_PREFIX.length()) == -1) {
                        String themeName = tagName.substring(tagName.lastIndexOf('/') + 1);
                        switch(valueType) {
                            case FILES:
                                themeDocs.put(themeName, files[f]);
                                break;
                            case DOCUMENTS:
                                themeDocs.put(themeName, themeDoc);
                                break;
                            case CONTENT:
                                themeDocs.put(themeName, snipEl.elementText("content"));
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.warn("Error reading potential theme file", e);
            }
        }
        return themeDocs;
    }
}
