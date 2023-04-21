package fbench;

import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * IEC 61499 compactable File Type Library
 * @author WD
 *
 * @version 20061126/WD
 */
public class FBLibrary extends FileFilter {

    public static int getSuffixNumber(File f) {
        return getSuffixNumber(f.getName());
    }

    public static int getSuffixNumber(String filename) {
        for (int i = 0; i < suffixes.length; i++) if (filename.endsWith(suffixes[i])) return i;
        return -1;
    }

    public FBLibrary() {
    }

    public static final String suffixes[] = { ".fbt", ".fnt", ".adp", ".res", ".dev", ".sys", ".dtp", ".xml", ".src", ".db", ".trace" };

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        return getSuffixNumber(f) >= 0;
    }

    public String getDescription() {
        return "IEC 61499 Compactable Types";
    }
}
