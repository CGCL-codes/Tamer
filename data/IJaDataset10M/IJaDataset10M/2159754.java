package com.jvito.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import com.jvito.util.Resources;

/**
 * A FileFilter for GIF-Files. This means for files with the extension ".gif".
 * 
 * @author Daniel Hakenjos
 * @version $Id: GIFFileFilter.java,v 1.3 2008/04/12 14:28:11 djhacker Exp $
 */
public class GIFFileFilter extends FileFilter {

    /**
	 * 
	 */
    public GIFFileFilter() {
        super();
    }

    /**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
    @Override
    public boolean accept(File file) {
        if (file == null) return false;
        if (!file.exists()) return false;
        if (file.isDirectory()) return true;
        String name = file.getName();
        if (name.endsWith(".gif")) return true;
        return false;
    }

    /**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
    @Override
    public String getDescription() {
        return Resources.getString("GIFFILE_DESCRIPTION");
    }
}
