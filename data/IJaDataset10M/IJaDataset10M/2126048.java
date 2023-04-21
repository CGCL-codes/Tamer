package com.aptana.ide.core.io;

/**
 * Event that listnens to log type info from file transfers.
 *
 */
public interface FileTransferListener {

    /**
	 * A transfer event occoured and the text is available.
	 * @param eventText
	 */
    void addText(String eventText);
}
