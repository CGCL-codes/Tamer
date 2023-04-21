package org.dicom4j.classcreator;

import java.io.Writer;
import org.dicom4j.dicom.dictionary.DicomDictionary;

public abstract class BaseGenerator {

    protected Writer fWriter;

    protected DicomDictionary dicomDictionary;

    public BaseGenerator() throws Exception {
        fWriter = createWriter();
    }

    protected void append(String aString) throws Exception {
        fWriter.write(aString + "\n");
    }

    protected void appendBlankLine() throws Exception {
        append("");
    }

    protected abstract Writer createWriter() throws Exception;

    public DicomDictionary getDicomDictionary() {
        return dicomDictionary;
    }

    public void setDicomDictionary(DicomDictionary dicomDictionary) {
        this.dicomDictionary = dicomDictionary;
    }
}
