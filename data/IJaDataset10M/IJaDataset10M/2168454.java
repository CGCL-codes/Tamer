package org.phymote.control;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.phymote.control.exceptions.Messages;

/**
 * @author Christoph Krichenbauer Class for Filtering the Phymote Save Files
 */
public class PhyMoteDataFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = PhyMoteDataFileFilter.getExtension(f);
        if (extension != null) {
            if (extension.equals(PhyMoteDataFileFilter.phyMoteSaveFileExtension)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return Messages.getString("PhyMoteDataFileFilter.FileType");
    }

    /**
	 * Extracts the filename-extention of any given file
	 * 
	 * @param f File
	 * @return Extention as String
	 */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if ((i > 0) && (i < s.length() - 1)) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext == null) {
            return "";
        }
        return ext;
    }

    public static final String phyMoteSaveFileExtension = "phy";
}
