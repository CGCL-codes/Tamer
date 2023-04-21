package dataExtraction;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Kasun
 */
public class DocumentFilter implements FileFilter {

    public boolean accept(File pathname) {
        if (pathname.getName().endsWith(".txt")) {
            return true;
        }
        if (pathname.getName().endsWith(".pdf")) {
            return true;
        }
        if (pathname.getName().endsWith(".rtf")) {
            return true;
        }
        if (pathname.getName().endsWith(".doc")) {
            return true;
        }
        if (pathname.getName().endsWith(".docx")) {
            return true;
        }
        return false;
    }
}
