package org.dcm4che2.tool.dcmof;

import java.io.File;
import java.io.IOException;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DicomServiceException;
import org.dcm4che2.net.PDVInputStream;
import org.dcm4che2.net.Status;
import org.dcm4che2.net.service.StorageService;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 5544 $ $Date: 2007-11-26 08:50:17 -0500 (Mon, 26 Nov 2007) $
 * @since Feb 2, 2006
 *
 */
class SCNSCP extends StorageService {

    protected final DcmOF dcmOF;

    protected File destination;

    public SCNSCP(DcmOF dcmOF) {
        super(UID.BasicStudyContentNotificationSOPClassRetired);
        this.dcmOF = dcmOF;
    }

    public final void setDestination(File destination) {
        destination.mkdirs();
        this.destination = destination;
    }

    @Override
    protected void doCStore(Association as, int pcid, DicomObject rq, PDVInputStream dataStream, String tsuid, DicomObject rsp) throws DicomServiceException, IOException {
        DicomObject data = dataStream.readDataset();
        String iuid = rq.getString(Tag.AffectedSOPInstanceUID);
        data.initFileMetaInformation(UID.BasicStudyContentNotificationSOPClassRetired, iuid, UID.ExplicitVRLittleEndian);
        try {
            store(iuid, data);
        } catch (Exception e) {
            throw new DicomServiceException(rq, Status.ProcessingFailure, e.getMessage());
        }
    }

    protected void store(String iuid, DicomObject data) throws Exception {
        dcmOF.storeAsDICOM(new File(destination, iuid), data);
    }

    static class XML extends SCNSCP {

        public XML(DcmOF dcmOF) {
            super(dcmOF);
        }

        @Override
        protected void store(String iuid, DicomObject data) throws Exception {
            dcmOF.storeAsXML(new File(destination, iuid + ".xml"), data);
        }
    }
}
