package test.dicom4j.toolkit;

import org.dicom4j.data.DataElements;
import org.dicom4j.data.DataSet;
import org.dicom4j.dicom.uniqueidentifiers.SOPClass;
import org.dicom4j.dicom.uniqueidentifiers.TransferSyntax;
import org.dicom4j.network.dimse.DimseMessageFactory;
import org.dicom4j.network.dimse.messages.CStoreRequestMessage;
import org.dicom4j.network.dimse.messages.CStoreResponseMessage;
import org.dicom4j.toolkit.media.FileMediaStorage;
import org.dicom4j.toolkit.media.MediaStorage;
import org.dicom4j.toolkit.media.MediaStorageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since 0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class TestMediaStorage extends AbstractTestCase {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(TestMediaStorage.class);

    public TestMediaStorage() {
        super();
    }

    public void testFileMediaStorage() throws Exception {
        logger.info("start testFileMediaStorage");
        FileMediaStorage fms = new FileMediaStorage();
        fms.setDirectoryPath("./store");
        fms.getConfiguration().setListener(new InnerMediaStorageListener());
        String sopClassUID = SOPClass.CTImageStorage.getUID();
        String sopInstanceUID = "1.1.1.1.1.1";
        DimseMessageFactory factory = new DimseMessageFactory();
        CStoreRequestMessage resquest = factory.newCStoreRequest();
        resquest.setAffectedSOPClassUID(sopClassUID);
        resquest.setAffectedSOPInstanceUID(sopInstanceUID);
        DataSet data = new DataSet();
        data.addUniqueIdentifier(DataElements.newStudyInstanceUID(), "StudyInstanceUID");
        data.addUniqueIdentifier(DataElements.newSeriesInstanceUID(), "SeriesInstanceUID");
        data.addUniqueIdentifier(DataElements.newSOPClassUID(), sopClassUID);
        data.addUniqueIdentifier(DataElements.newSOPInstanceUID(), sopInstanceUID);
        data.addPersonName(DataElements.newPatientName(), "John^Doe");
        resquest.setDataSet(data);
        fms.storeDataSet(resquest, TransferSyntax.Default);
        logger.info("testFileMediaStorage finished");
    }

    private class InnerMediaStorageListener implements MediaStorageListener {

        public void mediaStored(MediaStorage mediaStorage, CStoreResponseMessage storeResponse) {
            System.out.println("mediaStored: \n" + storeResponse);
        }
    }
}
