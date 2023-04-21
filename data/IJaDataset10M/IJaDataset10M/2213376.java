package phex.xml;

import java.util.*;

public class XMLSWDownloadSegment {

    private String incompleteFileName;

    private int segmentNumber;

    private long startPosition;

    private long length;

    public XMLSWDownloadSegment() {
    }

    public String getIncompleteFileName() {
        return incompleteFileName;
    }

    public void setIncompleteFileName(String aIncompleteFileName) {
        incompleteFileName = aIncompleteFileName;
    }

    public int getSegmentNumber() {
        return segmentNumber;
    }

    public void setSegmentNumber(int aSegmentNumber) {
        segmentNumber = aSegmentNumber;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long aStartPosition) {
        startPosition = aStartPosition;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long aLength) {
        length = aLength;
    }
}
