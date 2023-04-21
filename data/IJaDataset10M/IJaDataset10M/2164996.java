package org.openexi.fujitsu.proc.io;

import javax.xml.datatype.XMLGregorianCalendar;

public final class Scribble {

    public int intValue1;

    public int intValue2;

    public long longValue;

    public boolean booleanValue1;

    public boolean booleanValue2;

    public String stringValue1;

    public String stringValue2;

    public Scribble[] listOfScribbles;

    public ValueScriber valueScriber;

    public XMLGregorianCalendar calendar;

    public byte[] binaryValue;

    public Scribble() {
    }

    public Scribble(Scribble scribble) {
        this.intValue1 = scribble.intValue1;
        this.intValue2 = scribble.intValue2;
        this.longValue = scribble.longValue;
        this.booleanValue1 = scribble.booleanValue1;
        this.booleanValue2 = scribble.booleanValue2;
        this.stringValue1 = scribble.stringValue1;
        this.stringValue2 = scribble.stringValue2;
        this.calendar = scribble.calendar;
        final byte[] srcBinaryValue;
        if ((srcBinaryValue = scribble.binaryValue) != null) {
            final int len = srcBinaryValue.length;
            final byte[] binaryValue = new byte[len];
            System.arraycopy(srcBinaryValue, 0, binaryValue, 0, len);
            this.binaryValue = binaryValue;
        } else this.binaryValue = null;
    }
}
