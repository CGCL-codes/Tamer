package com.carsongee.audiom;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 * @author     Carson Gee
 * @created    February 1, 2007
 */
public class M3UFilter extends FileFilter {

    /**
	 *  //Accept all directories and all M3U files
	 *
	 * @param  f  A file for testing if it is a M3U file
	 * @return    Whether the f is a M3U File
	 */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext != null) {
            if (ext.equals("m3u")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
	 *  Gets the description attribute of the M3UFilter object
	 *
	 * @return    The description value
	 */
    public String getDescription() {
        return "M3U Playlist Files";
    }
}
