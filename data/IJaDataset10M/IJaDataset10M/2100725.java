package org.bookshare.document.beans;

import java.io.File;
import java.io.IOException;
import org.bookshare.document.daisy.OPFPackageFile;
import org.jdom.Document;
import org.jdom.JDOMException;

/**
 * Marker for document sets that have OPF document elements.
 * @author Reuben Firmin
 */
public interface HasOPF {

    /**
	 * Return a wrapper around the OPF package file.
	 * @return Never null.
	 * @throws JDOMException If parsing fails.
	 * @throws IOException If file access fails
	 */
    OPFPackageFile getOPFPackageFile() throws JDOMException, IOException;

    /**
	 * Return opf document.
	 * @return Never null
	 */
    Document getOPFDocument();

    /**
	 * Return opf file.
	 * @return Never null
	 */
    File getOPFFile();
}
