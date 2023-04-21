package org.databene.commons.file;

import org.databene.commons.FileUtil;
import java.io.File;

/**
 * Matches files by checking their suffix to be one of the specified values.<br/>
 * <br/>
 * Created: 23.04.2007 07:59:34
 * @author Volker Bergmann
 */
public class MultiFileSuffixFilter implements FileFilter {

    private String[] suffixes;

    private boolean caseSensitive;

    public MultiFileSuffixFilter(boolean caseSensitive, String... suffixes) {
        this.suffixes = suffixes;
        this.caseSensitive = caseSensitive;
    }

    public boolean accept(File file) {
        for (String suffix : suffixes) if (FileUtil.hasSuffix(file, suffix, caseSensitive)) return true;
        return false;
    }
}
