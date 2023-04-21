package de.tobiasmaasland.voctrain.business.persister.xml.util;

import java.io.IOException;

public class FileExistsException extends IOException {

    private static final long serialVersionUID = 5113702514592554913L;

    /**
	 * 
	 * @param s The given String to show.
	 */
    public FileExistsException(String s) {
        super(s);
    }
}
