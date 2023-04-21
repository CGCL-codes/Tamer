package allensoft.io;

import java.io.*;

/**
 * A file filter that accpets files that are accepted by two file filters.
 * @author Nicholas Allen
 */
public class AndFileFilter implements FileFilter {

    public AndFileFilter(FileFilter filter1, FileFilter filter2) {
        m_Filter1 = filter1;
        m_Filter2 = filter2;
    }

    public boolean accept(File file) {
        return m_Filter1.accept(file) && m_Filter2.accept(file);
    }

    private FileFilter m_Filter1, m_Filter2;
}
