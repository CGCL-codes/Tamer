package allensoft.javacvs.client;

import java.io.*;
import java.util.*;
import allensoft.util.*;
import allensoft.io.*;

/** Defines a pattern based file filter whose patterns are taken from a file. The patterns are separated
	by white space. Typically, these will be .cvsignore files in the user's home directory or directories
 being recursed by an import or add command. The file is automatically reparsed when it is modified externally.
 The patterns in this filter reqpresent the patterns in the file. Note that this filter accepts files only if
 a pattern does not match the file. This is because the patterns specify files to be ignored. */
public class CVSIgnoreFile extends PatternBasedFileFilter {

    private CVSIgnoreFile(File file) {
        m_File = file;
    }

    public static synchronized CVSIgnoreFile getCVSIgnoreFile(File file) {
        if (!file.exists() || file.isDirectory()) return null;
        CVSIgnoreFile ignoreFile = (CVSIgnoreFile) g_IgnoreFiles.get(file);
        if (ignoreFile == null) g_IgnoreFiles.put(file, ignoreFile = new CVSIgnoreFile(file));
        return ignoreFile;
    }

    public boolean accept(File file) {
        loadPatterns();
        return (!super.accept(file));
    }

    public void removePattern(String sPattern) {
        super.removePattern(sPattern);
        savePatterns();
    }

    public void removeAllPatterns() {
        super.removeAllPatterns();
        savePatterns();
    }

    public void addPattern(String sPattern) {
        super.addPattern(sPattern);
        savePatterns();
    }

    public void addPattern(String[] patterns) {
        super.addPatterns(patterns);
        savePatterns();
    }

    public File getFile() {
        return m_File;
    }

    private void loadPatterns() {
        if (m_File.lastModified() == m_nLastModified) return;
        super.removeAllPatterns();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(m_File));
            String sLine = null;
            while ((sLine = in.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(sLine);
                while (tokenizer.hasMoreTokens()) super.addPattern(tokenizer.nextToken());
            }
            m_nLastModified = m_File.lastModified();
        } catch (IOException e) {
            System.err.println("Error reading CVS ignore patterns from file \"" + m_File.getPath() + "\":\n" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void savePatterns() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(m_File)));
            for (int i = 0; i < getPatternCount(); i++) out.println(getPattern(i));
        } catch (IOException e) {
            System.err.println("Error saving CVS ignore patterns to file \"" + m_File.getPath() + "\":\n" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
                m_nLastModified = m_File.lastModified();
            }
        }
    }

    private File m_File;

    private long m_nLastModified = -1;

    /** Cache of already loaded files. Maps File to CVSIgnoreFile. */
    private static Map g_IgnoreFiles = new HashMap(10);
}
