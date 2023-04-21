package jcontrol.conect.data.database;

import jcontrol.conect.tools.*;
import jcontrol.conect.data.settings.Settings;
import java.io.*;
import java.io.File;
import java.util.*;

/**
 * A file filter for ets2.vd_ database files
 */
public class Ets2Vd_FileFilter extends SuffixAwareFilter {

    protected final String INITIAL_BUNDLE = Settings.PROPERTIES_PATH + "data/Ets2Vd_FileFilter";

    protected ResourceBundle resources = ResourceBundle.getBundle(INITIAL_BUNDLE);

    public boolean accept(File f) {
        boolean accept = f.isDirectory();
        if (!accept) {
            String suffix = getSuffix(f);
            if (suffix != null) accept = super.accept(f) || suffix.equals(resources.getString("filter.ets2.file.vd"));
        }
        return accept;
    }

    public String getDescription() {
        return resources.getString("filter.ets2.file.description");
    }
}
