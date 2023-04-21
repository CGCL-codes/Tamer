package allensoft.io;

import java.util.*;
import java.io.*;
import allensoft.util.*;

/** A FileFilter that filters files that match a list of patterns. */
public class PatternBasedFileFilter implements FileFilter {

    public PatternBasedFileFilter() {
    }

    public PatternBasedFileFilter(String sPattern) {
        addPattern(sPattern);
    }

    public PatternBasedFileFilter(String[] patterns) {
        addPatterns(patterns);
    }

    public boolean accept(File file) {
        String sFileName = file.getName();
        boolean bCaseSensitive = FileUtilities.areFileNamesCaseSensitive();
        Iterator i = m_Patterns.iterator();
        while (i.hasNext()) {
            String sPattern = (String) i.next();
            if (StringUtilities.matchesPattern(sFileName, sPattern, bCaseSensitive)) return true;
        }
        return false;
    }

    public void addPattern(String sPattern) {
        m_Patterns.add(sPattern);
    }

    public void addPatterns(String[] patterns) {
        m_Patterns.addAll(Arrays.asList(patterns));
    }

    public void setPattern(String sPattern) {
        m_Patterns.clear();
        m_Patterns.add(sPattern);
    }

    public void removePattern(String sPattern) {
        m_Patterns.remove(sPattern);
    }

    public void removePattern(int nIndex) {
        m_Patterns.remove(nIndex);
    }

    public void removeAllPatterns() {
        m_Patterns.clear();
    }

    public int getPatternCount() {
        return m_Patterns.size();
    }

    public String getPattern(int nIndex) {
        return (String) m_Patterns.get(nIndex);
    }

    private List m_Patterns = new ArrayList(10);
}
