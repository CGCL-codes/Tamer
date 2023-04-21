package org.kadosu.document.plugin.text;

import java.io.File;
import org.kadosu.document.IDocumentHandler;
import org.kadosu.exception.KDSAccessException;

/**
 *  
 */
public class DocumentTextHandler implements IDocumentHandler {

    public boolean isDocument(String path) throws KDSAccessException {
        boolean result = false;
        File temp = new File(path);
        if (temp.isFile() && matchPatterns(temp.getName())) {
            result = true;
            if (!temp.canRead()) {
                throw new KDSAccessException(temp.getAbsolutePath() + " is not readable.");
            }
        }
        return result;
    }

    private boolean matchPatterns(String fileName) {
        boolean result = false;
        String[] patterns = DocumentTextPP.getFilePatterns();
        for (int i = 0; i < patterns.length; i++) {
            if (fileName.matches(patterns[i])) {
                result = true;
                break;
            }
        }
        return result;
    }
}
