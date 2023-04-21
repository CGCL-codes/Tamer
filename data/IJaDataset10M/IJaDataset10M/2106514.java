package org.tcpfile.fileio;

import java.io.File;
import org.tcpfile.main.EntryPoint;

public class CFile extends File {

    public CFile(String pathname) {
        super(EntryPoint.getConfigFilePath(pathname));
    }

    private CFile(String parent, String child) {
        super(EntryPoint.getConfigFilePath(parent), child);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
}
