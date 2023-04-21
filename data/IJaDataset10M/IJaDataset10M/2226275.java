package net.dawnmist.sims2tracker;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class Sims2TrackerFileFilter extends FileFilter {

    private static String sFileSuffix = ".s2t";

    public Sims2TrackerFileFilter() {
    }

    @Override
    public boolean accept(File f) {
        boolean accept = (f.isDirectory()) ? true : f.getName().endsWith(sFileSuffix);
        return accept;
    }

    @Override
    public String getDescription() {
        return "Sims2Tracker files (" + sFileSuffix + ")";
    }

    public static String getFileSuffix() {
        return sFileSuffix;
    }
}
