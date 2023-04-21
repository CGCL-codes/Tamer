package net.sourceforge.jpcap.capture;

/**
 * This exception occurs when an error occurs while capturing data.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 540 $
 * @lastModifiedBy $Author: buchmand $
 * @lastModifiedAt $Date: 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) $
 */
public class CapturePacketException extends Exception {

    /**
   * Create a new invalid capture device exception.
   */
    public CapturePacketException(String message) {
        super(message);
    }

    private static String _rcsId = "$Id: CapturePacketException.java 540 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) buchmand $";
}
