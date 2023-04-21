package org.robocup.msl.refbox;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class TeamSetupFileFileFilter extends FileFilter {

    private static final String RSF = ".rsf";

    public TeamSetupFileFileFilter() {
        super();
    }

    @Override
    public boolean accept(final File pathname) {
        boolean accept;
        String fn = pathname.toString();
        String suffixPathName = fn.substring(fn.length() - RSF.length(), fn.length());
        if (suffixPathName.equalsIgnoreCase(RSF)) {
            accept = true;
        } else {
            if (pathname.isDirectory()) {
                accept = true;
            } else {
                accept = false;
            }
        }
        return accept;
    }

    @Override
    public String getDescription() {
        return "RSF filter";
    }

    public static String getExtension() {
        return RSF;
    }
}
