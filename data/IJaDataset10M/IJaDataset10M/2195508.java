package org.dicom4j.network.dimse.services;

import org.dicom4j.data.DataElements;
import org.dicom4j.data.DataSet;
import org.dicom4j.data.elements.UnsignedLong;
import org.dicom4j.dicom.DicomTag;
import org.dicom4j.dicom.DicomTags;
import org.dicom4j.network.dimse.QueryRetrieveLevel;
import org.dicom4j.network.dimse.messages.CFindRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @since 0.0.3
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class CQuerySCP extends CFindSCP {

    private static final Logger fLogger = LoggerFactory.getLogger(CQuerySCP.class);

    protected void addAllMandatoryDataElement(DataSet aDataSet, CFindRequestMessage aRequest) throws Exception {
        if (QueryRetrieveLevel.isPatientLevel(aDataSet)) {
            addAllMandatoryDataElementForStudyLevel(aDataSet);
        }
        if (QueryRetrieveLevel.isStudyLevel(aDataSet)) {
            addAllMandatoryDataElementForStudyLevel(aDataSet);
        }
    }

    protected void addAllMandatoryDataElementForPatientLevel(DataSet aDataSet) throws Exception {
        fLogger.debug("addAllMandatoryDataElementForPatientLevel");
        aDataSet.addElement(DataElements.newPatientName());
        aDataSet.addElement(DataElements.newPatientID());
    }

    protected void addAllMandatoryDataElementForStudyLevel(DataSet aDataSet) throws Exception {
        fLogger.debug("addAllMandatoryDataElementForStudyLevel");
        aDataSet.addElement(new UnsignedLong(new DicomTag(0x0008, 0x0000)));
        aDataSet.addElement(DataElements.newStudyDate());
        aDataSet.addElement(DataElements.newStudyTime());
        aDataSet.addElement(DataElements.newAccessionNumber());
        aDataSet.addElement(new UnsignedLong(new DicomTag(0x0020, 0x0000)));
        aDataSet.addElement(DataElements.newStudyID());
        aDataSet.addElement(DataElements.newStudyInstanceUID());
    }

    protected DataSet newDataSet(CFindRequestMessage aRequest) throws Exception {
        DataSet lDataset = new DataSet();
        lDataset.addElement(DataElements.newQueryRetrieveLevel());
        lDataset.getCodeString(DicomTags.QueryRetrieveLevel).setValue(aRequest.getDataSet().getCodeString(DicomTags.QueryRetrieveLevel).getSingleStringValue());
        addAllMandatoryDataElement(lDataset, aRequest);
        return lDataset;
    }

    @Override
    public String toString() {
        return "CQuery SCP";
    }
}
