package org.jpedal.jbig2.examples.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFSegment {

    private ByteArrayOutputStream header = new ByteArrayOutputStream();

    private ByteArrayOutputStream data = new ByteArrayOutputStream();

    private int segmentDataLength;

    public void writeToHeader(short bite) {
        header.write(bite);
    }

    public void writeToHeader(short[] bites) throws IOException {
        for (int i = 0; i < bites.length; i++) header.write(bites[i]);
    }

    public void writeToData(short bite) {
        data.write(bite);
    }

    public ByteArrayOutputStream getHeader() {
        return header;
    }

    public ByteArrayOutputStream getData() {
        return data;
    }

    public void setDataLength(int segmentDataLength) {
        this.segmentDataLength = segmentDataLength;
    }

    public int getSegmentDataLength() {
        return segmentDataLength;
    }
}
