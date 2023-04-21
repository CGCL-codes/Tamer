package uk.ac.cisban.saint.client.structure.records;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Stores attributes common to all records of annotation used by the Model Annotator
 */
public abstract class SimpleRecord implements Serializable {

    private HashSet<SourceRecord> sourceRecords;

    private BiologicalQualifierType biologicalQualifierType;

    public enum BiologicalQualifierType {

        UNKNOWN("http://biomodels.net/model-qualifiers/unknown"), IS("http://biomodels.net/model-qualifiers/is"), IS_VERSION_OF("http://biomodels.net/biology-qualifiers/isVersionOf"), HAS_PART("http://biomodels.net/biology-qualifiers/hasProperty");

        private String uri;

        BiologicalQualifierType(String uri) {
            this.uri = uri;
        }

        public String toString() {
            return uri;
        }
    }

    protected SimpleRecord() {
        this.sourceRecords = new HashSet<SourceRecord>();
        biologicalQualifierType = BiologicalQualifierType.IS;
    }

    protected SimpleRecord(SourceRecord sourceRecord) {
        if (sourceRecord != null) {
            if (this.sourceRecords == null) {
                this.sourceRecords = new HashSet<SourceRecord>();
            }
            addSourceRecord(sourceRecord);
        }
        biologicalQualifierType = BiologicalQualifierType.IS;
    }

    public HashSet<SourceRecord> getSourceRecords() {
        return sourceRecords;
    }

    public void setSourceRecords(HashSet<SourceRecord> sourceRecords) {
        this.sourceRecords = sourceRecords;
    }

    public void addSourceRecord(SourceRecord sourceRecord) {
        this.sourceRecords.add(sourceRecord);
    }

    public StringBuffer displaySourceRecords() {
        StringBuffer display = new StringBuffer();
        for (SourceRecord sourceRecord : sourceRecords) {
            display.append(sourceRecord.display());
            display.append("; ");
        }
        if (display.length() >= 2) {
            display.delete(display.length() - 2, display.length());
        }
        return display;
    }

    public BiologicalQualifierType getBiologicalQualifierType() {
        return biologicalQualifierType;
    }

    public void setBiologicalQualifierType(BiologicalQualifierType biologicalQualifierType) {
        this.biologicalQualifierType = biologicalQualifierType;
    }

    public abstract String displayHtml();

    public abstract String displayText();
}
