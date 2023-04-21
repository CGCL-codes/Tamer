package ckjm.plugin;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Nicolas Dordet
 * @version 0.1 béta
 */
public class Filter implements FilenameFilter {

    public Filter() {
    }

    /**
 * @param dir: the directory to inspect
 * @param name: the name of the file
 * @return true if the file is a .class, false in the other case
 */
    public boolean accept(File dir, String name) {
        return (name.endsWith(".class"));
    }
}
