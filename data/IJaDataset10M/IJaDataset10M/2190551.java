package org.xpresso.xml;

/**
 *
 * Thrown if content is tried to be added to a closed Document. A Document is closed if<br />
 * the {@link org.xpresso.xml.Document#finish() Docment.finish()} method is called or if<br />
 * the {@link org.xpresso.xml.Document#unstackElement() Document.unstackElement()} method is called enough times<br />
 * to unstack all {@link org.xpresso.xml.Element Elements}.
 * This code is under the <a href="http://www.gnu.org/licenses/lgpl.html">LGPL v3 licence</a>.
 * @author Alexis Dufrenoy
 *
 */
public class ClosedDocumentException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8332697087949081305L;

    /**
	 *
	 */
    public ClosedDocumentException() {
        super();
    }

    /**
	 *
	 * @param message
	 */
    public ClosedDocumentException(String message) {
        super(message);
    }

    /**
	 *
	 * @param message
	 * @param cause
	 */
    public ClosedDocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 *
	 * @param cause
	 */
    public ClosedDocumentException(Throwable cause) {
        super(cause);
    }
}
