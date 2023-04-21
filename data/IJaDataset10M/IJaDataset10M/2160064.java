package org.dcm4che2.io;

import java.io.IOException;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 488 $ $Date: 2005-12-08 18:59:24 -0500 (Thu, 08 Dec 2005) $
 * @since Nov 27, 2005
 *
 */
public class DicomCodingException extends IOException {

    private static final long serialVersionUID = 2702906687102112917L;

    public DicomCodingException(String s) {
        super(s);
    }
}
