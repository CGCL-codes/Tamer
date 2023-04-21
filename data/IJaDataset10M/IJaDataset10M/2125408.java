package edu.stanford.genetics.treeview;

import java.io.*;

/**
 *  Class to filter through files for .cdt and .pcl files.
 *
 * @author     Alok Saldanha <alok@genome.stanford.edu>
 * @version    $Revision: 1.5 $ $Date: 2004-12-21 03:28:14 $
 */
public class CdtFilter extends javax.swing.filechooser.FileFilter implements FilenameFilter {

    /**
	 *  from the <code>FilenameFilter</code> interface.
	 *
	 * @param  dir   Directory to look in
	 * @param  file  File name
	 * @return       Returns true if file ends with .cdt or .pcl
	 */
    public boolean accept(File dir, String file) {
        dir = null;
        if (file.toLowerCase().endsWith(".cdt")) {
            return true;
        }
        if (file.toLowerCase().endsWith(".pcl")) {
            return true;
        }
        return false;
    }

    /**
	 *  accepts or rejects files and directories
	 *
	 * @param  f  the file in question
	 * @return    returns true if it's a directory, or if it ends in .pcl or .cdt
	 */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return accept(f, f.getName());
    }

    public String getDescription() {
        return "CDT or PCL Files";
    }
}
