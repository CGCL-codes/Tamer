package net.mchaplin.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;
import net.mchaplin.commons.WmindObject;
import org.apache.commons.io.FileUtils;

/**
 * @author mchaplin@users.sourceforge.net
 * 
 * $Header: /cvsroot/iky-container/iky-container/src/net/mchaplin/commons/io/IOUtils.java,v 1.1 2005/05/06 22:23:47 mchaplin Exp $Revision: 1.1 $Date:
 */
public class IOUtils extends WmindObject {

    public static final String PATH_SEPARATOR = "\\";

    public static final String LINE_BREAK = "\n";

    /**
	 * Copy a file from a base source directory to a base target directory. File
	 * might be located in a subdirectory of source directory.
	 * 
	 * @param file
	 *            file to copy. Might be in a subdirectory
	 * @param sourceDir
	 *            base source directory
	 * @param targetDir
	 *            base target directory
	 * 
	 * @return the file package
	 */
    public static String copyToDirectory(File file, File sourceDir, File targetDir) {
        String absPath = file.getAbsolutePath();
        String relPath = absPath.substring(sourceDir.getAbsolutePath().length());
        log.debug("processing file : [" + file.getAbsolutePath() + "]");
        int fNamePos = relPath.lastIndexOf(PATH_SEPARATOR);
        String pkgPath = relPath.substring(0, fNamePos);
        String targetPath = targetDir + PATH_SEPARATOR + pkgPath;
        File fTargetPathDir = new File(targetPath);
        if (!fTargetPathDir.exists()) {
            fTargetPathDir.mkdirs();
        }
        try {
            FileUtils.copyFileToDirectory(file, fTargetPathDir);
        } catch (IOException e1) {
            log.error("I/O exception occured while copying file [" + file.getName() + "] to [" + targetDir + "]");
            e1.printStackTrace();
        }
        return pkgPath;
    }

    /**
     * Insert a String in a file
     * 
     * @param inFile file to insert String to
     * @param lineno position in file
     * @param lineToBeInserted String to be inserted
     * @param replace replace/append mode
     * 
     */
    public static boolean insertStringInFile(File inFile, int lineno, String lineToBeInserted, boolean replace) {
        System.out.println("Inserting line [" + lineToBeInserted + "] into [" + inFile.getAbsolutePath() + "] at pos [" + lineno + "]");
        boolean success = true;
        File outFile = new File("~.tmp");
        try {
            FileInputStream fis = new FileInputStream(inFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            FileOutputStream fos = new FileOutputStream(outFile);
            PrintWriter out = new PrintWriter(fos);
            String thisLine = "";
            int i = 1;
            while ((thisLine = in.readLine()) != null) {
                if (i == lineno) {
                    out.println(lineToBeInserted);
                }
                if (!replace) {
                    out.println(thisLine);
                }
                i++;
            }
            out.flush();
            in.close();
            fos = new FileOutputStream(inFile);
            out = new PrintWriter(fos);
            fis = new FileInputStream(outFile);
            in = new BufferedReader(new InputStreamReader(fis));
            while ((thisLine = in.readLine()) != null) {
                out.println(thisLine);
            }
            out.flush();
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            log.error("File [" + inFile.getAbsolutePath() + "] not found !");
            success = false;
        } catch (IOException e) {
            log.error("I/O exception occured while inserting line into [" + inFile.getAbsolutePath() + "]");
            success = false;
        }
        return success;
    }

    /**
	 * @param targetFile
	 * @param vFileContent
	 */
    public static void loadFileToVector(File targetFile, Vector<String> vFileContent) {
        FileInputStream fis = null;
        BufferedReader in = null;
        String sLineContent = null;
        try {
            fis = new FileInputStream(targetFile);
            in = new BufferedReader(new InputStreamReader(fis));
        } catch (FileNotFoundException e) {
            log.error("Unable to load file [" + targetFile.getAbsolutePath() + "]. File not found !");
        }
        try {
            while ((sLineContent = in.readLine()) != null) {
                vFileContent.add(sLineContent);
            }
            in.close();
        } catch (IOException e1) {
            log.error("I/O exception occured while reading file [" + targetFile.getName() + "]");
        }
    }

    /**
	 * @param targetFile
	 * @param vFileContent
	 */
    public static void writeVectorToFile(File targetFile, Vector<String> vFileContent) {
        PrintWriter out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e2) {
            log.error("Unable to write modified tags to file [" + targetFile.getAbsolutePath() + "]. File not found !");
        }
        out = new PrintWriter(fos);
        Iterator<String> itr = vFileContent.iterator();
        while (itr.hasNext()) {
            out.println(itr.next());
        }
        out.flush();
        out.close();
    }

    public static int getInstanceCount(String string, String value) {
        int offset = 0;
        int index = string.indexOf(value);
        while (index != -1) {
            string = string.substring(index + value.length());
            offset++;
            index = string.indexOf(value);
        }
        return offset;
    }
}
