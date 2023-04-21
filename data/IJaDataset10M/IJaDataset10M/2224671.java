package javatex.editor;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;

public class KeywordManager {

    public static Vector keywords;

    public static void loadKeywordFiles() {
        keywords = new Vector();
        File path = new File("keywords");
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if (filename.endsWith(".keywords")) {
                String name = filename.substring(0, filename.length() - 9);
                KeywordList keys = new KeywordList(name);
                keys.readFile(files[i].toString());
                keywords.add(keys);
            }
        }
    }

    public static void setStyles(StyledDocument doc) {
        for (int i = 0; i < keywords.size(); i++) {
            KeywordList keys = (KeywordList) keywords.get(i);
            String name = keys.getName();
            Color foreground = keys.getForeground();
            Color background = keys.getBackground();
            Style style = doc.addStyle(name, null);
            StyleConstants.setForeground(style, foreground);
            StyleConstants.setBackground(style, background);
        }
    }

    public static String getStyleName(String word) {
        for (int i = 0; i < keywords.size(); i++) {
            KeywordList keys = (KeywordList) keywords.get(i);
            if (keys.isMember(word)) {
                return keys.getName();
            }
        }
        return null;
    }
}
