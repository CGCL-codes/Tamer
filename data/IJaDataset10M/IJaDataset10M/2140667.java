package org.dcm4che2.net.service;

import java.io.IOException;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DicomServiceException;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2006-03-11 20:15:30 -0500 (Sat, 11 Mar 2006) $
 * @since Oct 3, 2005
 * 
 */
public interface NSetSCP {

    void nset(Association as, int pcid, DicomObject cmd, DicomObject data) throws DicomServiceException, IOException;
}
