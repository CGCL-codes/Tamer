package com.kishimemo.pdf;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author kishimoto kazuya
 * @version 1.0
 *
 */
public class PDFFileFilter implements FilenameFilter {

    private final String FILTER_KEYWORD = ".pdf";

    /**
	 * 
	 */
    public PDFFileFilter() {
    }

    @Override
    public boolean accept(File dir, String name) {
        File file = new File(name);
        if (file.isDirectory()) {
            return false;
        }
        return (name.endsWith(FILTER_KEYWORD));
    }
}
