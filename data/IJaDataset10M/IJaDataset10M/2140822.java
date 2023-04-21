package org.pixory.pxfoundation;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * filters on the basis of a regex applied to the filename
 */
public final class PXRegexFilenameFilter extends Object implements FilenameFilter, FileFilter {

    private static final Log LOG = LogFactory.getLog(PXRegexFilenameFilter.class);

    private Pattern _pattern;

    public PXRegexFilenameFilter(String regex) {
        if (regex != null) {
            _pattern = Pattern.compile(regex);
        } else {
            throw new IllegalArgumentException("PXRegexFilenameFilter does not accept null args");
        }
    }

    public String getRegex() {
        String getRegex = null;
        if (_pattern != null) {
            getRegex = _pattern.pattern();
        }
        return getRegex;
    }

    /**
	 * Tests if a specified file should be included in a file list.
	 * 
	 * @param dir
	 *           the directory in which the file was found.
	 * @param name
	 *           the name of the file.
	 * @return <code>true</code> if and only if the name should be included in
	 *         the file list; <code>false</code> otherwise.
	 *  
	 */
    public boolean accept(File dir, String filename) {
        boolean accept = false;
        if (filename != null) {
            Matcher aMatcher = _pattern.matcher(filename);
            if ((aMatcher != null) && aMatcher.matches()) {
                accept = true;
            }
        }
        return accept;
    }

    public boolean accept(File file_) {
        boolean accept = false;
        if (file_ != null) {
            accept = this.accept(file_.getParentFile(), file_.getName());
        }
        return accept;
    }

    public boolean equals(Object object) {
        boolean equals = false;
        if (this == object) {
            equals = true;
        } else if (object instanceof PXRegexFilenameFilter) {
            PXRegexFilenameFilter aFilter = (PXRegexFilenameFilter) object;
            String thisRegex = this.getRegex();
            String aRegex = aFilter.getRegex();
            equals = (thisRegex == null ? aRegex == null : thisRegex.equals(aRegex));
        }
        return equals;
    }

    public int hashCode() {
        int hashCode = 17;
        String aRegex = this.getRegex();
        hashCode = 37 * hashCode + (aRegex == null ? 0 : aRegex.hashCode());
        return hashCode;
    }

    public String toString() {
        StringBuffer aBuffer = new StringBuffer("PXRegexFilenameFilter@");
        aBuffer.append(Integer.toHexString(hashCode()));
        aBuffer.append("[regex=");
        aBuffer.append(this.getRegex());
        aBuffer.append(']');
        return aBuffer.toString();
    }
}
