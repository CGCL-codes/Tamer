package org.dicom4j.module.directory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dicom4j.data.DataSet;
import org.dicom4j.data.elements.ElementNotFoundException;
import org.dicom4j.dicom.DicomException;
import org.dicom4j.dicom.DicomTag;
import org.dicom4j.dicom.DicomTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyDirectoryRecordModule extends AbstractDirectoryRecordModule {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StudyDirectoryRecordModule.class);

    public StudyDirectoryRecordModule(DataSet dataSet) {
        super(dataSet);
    }

    @Override
    protected void createsEntries() {
        super.createsEntries();
        addEntry(DicomTags.StudyDate);
        addEntry(DicomTags.StudyTime);
        addEntry(DicomTags.StudyDescription);
        addEntry(DicomTags.StudyInstanceUID);
        addEntry(DicomTags.StudyID);
        addEntry(DicomTags.AccessionNumber);
    }

    public Iterator<DicomTag> getTags() {
        List<DicomTag> tags = new ArrayList<DicomTag>();
        tags.add(DicomTags.StudyDate);
        tags.add(DicomTags.StudyTime);
        tags.add(DicomTags.StudyDescription);
        tags.add(DicomTags.StudyInstanceUID);
        tags.add(DicomTags.StudyID);
        tags.add(DicomTags.AccessionNumber);
        return tags.iterator();
    }

    public boolean hasStudyDate() {
        return getDataSet().hasElement(DicomTags.StudyDate);
    }

    public String getStudyDateSingleValue() throws ElementNotFoundException, DicomException {
        return getDataSet().getSingleStringValue(DicomTags.StudyDate);
    }

    public String getStudyDateSingleValue(String defaultValue) {
        return getDataSet().getSingleStringValue(DicomTags.StudyDate, defaultValue);
    }

    public String getStudyDateSingleValueOrEmptyString() {
        return getDataSet().getSingleStringValueOrEmptyString(DicomTags.StudyDate);
    }

    public boolean hasStudyTime() {
        return getDataSet().hasElement(DicomTags.StudyTime);
    }

    public String getStudyTimeSingleValue() throws ElementNotFoundException, DicomException {
        return getDataSet().getSingleStringValue(DicomTags.StudyTime);
    }

    public String getStudyTimeSingleValue(String defaultValue) {
        return getDataSet().getSingleStringValue(DicomTags.StudyTime, defaultValue);
    }

    public String getStudyTimeSingleValueOrEmptyString() {
        return getDataSet().getSingleStringValueOrEmptyString(DicomTags.StudyTime);
    }
}
