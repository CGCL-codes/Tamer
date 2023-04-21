package com.flagstone.transform;

/**
FSSerialNumber is used to add a user-defined serial number into a Flash file. 
 
<p>The serial number is simply a string and can contain arbitrary information.</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr>
<td><a name="FSSerialNumber_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSSerialNumber_1">serialNumber</a></td>
<td>A string containing the serial number.</td>
</tr>
</table>

<h1 class="datasheet">Example</h1>

<pre>
movie.add(new FSSerialNumber("1234-DGF-2354"));
</pre>

<h1 class="datasheet">History</h1>

<p>The FSSerialNumber represents the SerialNumber tag in the Macromedia Flash (SWF) File Format Specification. It is not known when this tag was introduced.</p>
 */
public class FSSerialNumber extends FSMovieObject {

    private String serialNumber = null;

    /**
     * Construct an FSSerialNumber object, initalizing it with values decoded from
     * an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSSerialNumber(FSCoder coder) {
        super(SerialNumber);
        decode(coder);
    }

    /** Constructs an FSSerialNumber action with the specified string. 

        @param aString an arbitrary string containing the serial number.
        */
    public FSSerialNumber(String aString) {
        super(SerialNumber);
        setSerialNumber(aString);
    }

    /**
     * Constructs an FSSerialNumber object by copying values from an existing 
     * object.
     *
     * @param obj an FSSerialNumber object.
     */
    public FSSerialNumber(FSSerialNumber obj) {
        super(obj);
        serialNumber = new String(obj.serialNumber);
    }

    /** Gets the serial number.

        @return the serial number string.
        */
    public String getSerialNumber() {
        return serialNumber;
    }

    /** Sets the serial number.

        @param aString an arbitrary string containing the serial number.
        */
    public void setSerialNumber(String aString) {
        serialNumber = aString;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSSerialNumber typedObject = (FSSerialNumber) anObject;
            if (serialNumber != null) result = serialNumber.equals(typedObject.serialNumber); else result = serialNumber == typedObject.serialNumber;
        }
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "serialNumber", serialNumber);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        super.length(coder);
        length += coder.strlen(serialNumber, true);
        return length;
    }

    public void encode(FSCoder coder) {
        super.encode(coder);
        coder.writeString(serialNumber);
        coder.writeWord(0, 1);
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        super.decode(coder);
        serialNumber = coder.readString(length - 1);
        coder.readWord(1, false);
        coder.endObject(name());
    }
}
