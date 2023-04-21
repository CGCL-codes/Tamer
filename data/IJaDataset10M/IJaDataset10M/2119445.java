package net.sourceforge.ondex.tools.ziptools;

import java.io.File;

/**
 * This class is for the identification of the type of input file for MEDLINE
 * (xml, oxl, gz)
 * 
 * @author rwinnenb
 * 
 */
public class ZipEndings {

    public static final int XML = 0;

    public static final int GZ = 1;

    public static final int OXL = 2;

    public static final int ZIP = 3;

    public static final int OTHER = 4;

    /**
	 * Returns mapping for postfix of a String.
	 * 
	 * @param s
	 *            String
	 * @return int
	 */
    public static int getPostfix(String s) {
        int ending = s.lastIndexOf(".");
        if (ending == -1) return OTHER;
        String end = s.substring(ending);
        int type = XML;
        if (end.equals(".gz")) type = GZ;
        if (end.equals(".gzip")) type = GZ;
        if (end.equals(".oxl")) type = OXL;
        if (end.equals(".zip")) type = ZIP;
        return type;
    }

    /**
	 * Returns mapping for postfix of a File.
	 * 
	 * @param file
	 *            File
	 * @return int
	 */
    public static int getPostfix(File file) {
        return getPostfix(file.getName());
    }
}
