package org.zkoss.zhtml;

import org.zkoss.util.media.Media;

/**
 * A fileupload dialog used to let user upload a file.
 *
 * <p>You don't create {@link Fileupload} directly. Rather, use {@link #get()}
 * or {@link #get(String, String)}.
 *
 * <p>A non-XHTML extension.
 * 
 * @author tomyeh
 */
public class Fileupload extends org.zkoss.zul.Fileupload {

    /** Opens a modal dialog with the default message and title,
	 * and let user upload a file.
	 * @return the uploaded content, or null if not ready.
	 */
    public static Media get() throws InterruptedException {
        return get(null, null);
    }

    /** Opens a modal dialog with the specified message and title,
	 * and let user upload a file.
	 *
	 * @param message the message. If null, the default is used.
	 * @param title the title. If null, the default is used.
	 * @return the uploaded content, or null if not ready.
	 */
    public static Media get(String message, String title) throws InterruptedException {
        return org.zkoss.zul.Fileupload.get(message, title);
    }
}
