package net.mitnet.tools.pdf.book.io;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 * File Name Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see FilenameUtils
 */
public class FileNameHelper {

    public static String rewriteFileNameSuffix(File sourceFile, String newFileNameExtension) {
        String newFileName = rewriteFileNameSuffix(sourceFile.getName(), newFileNameExtension);
        return newFileName;
    }

    public static String rewriteFileNameSuffix(File sourceFile, String newFileNameSuffix, String newFileNameExtension) {
        String newFileName = rewriteFileNameSuffix(sourceFile.getName(), newFileNameSuffix, newFileNameExtension);
        return newFileName;
    }

    public static String rewriteFileNameSuffix(String sourceFileName, String newFileNameExtension) {
        String baseFileName = FilenameUtils.getBaseName(sourceFileName);
        String newFileName = baseFileName + newFileNameExtension;
        return newFileName;
    }

    public static String rewriteFileNameSuffix(String sourceFileName, String newFileNameSuffix, String newFileNameExtension) {
        String baseFileName = FilenameUtils.getBaseName(sourceFileName);
        if (newFileNameSuffix != null) {
            baseFileName = baseFileName + newFileNameSuffix;
        }
        String newFileName = baseFileName + newFileNameExtension;
        return newFileName;
    }
}
