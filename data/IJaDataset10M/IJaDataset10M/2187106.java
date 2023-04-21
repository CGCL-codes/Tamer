package org.binarydom.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import org.binarydom.Attr;
import org.binarydom.BDOMException;
import org.binarydom.Document;
import org.binarydom.Element;
import org.binarydom.Node;
import org.binarydom.parsers.impl.DocParserImpl;

/**
 * @author Mattias
 */
public class AttrImpl extends NodeImpl implements Attr, Serializable {

    private static final long serialVersionUID = -9163779469152023743L;

    private int length;

    private int idSize;

    private int lenSize;

    private String strValue;

    private byte[] binValue;

    private byte[] strArray;

    private int intValue;

    private boolean isString;

    private boolean isBinary;

    private boolean isInt;

    private boolean isLong;

    private long longValue;

    private boolean isShort;

    private boolean isByte;

    public AttrImpl(Document document, Element parent, short id) {
        super(document, parent, id);
        idSize = DocParserImpl.ID_LENGTH;
        if (id > Byte.MAX_VALUE) {
            idSize = DocParserImpl.ID_LENGTH_L;
        }
    }

    public String getValue() throws BDOMException {
        String result = null;
        if (strValue != null) {
            result = strValue;
        } else if (binValue != null) {
            result = new String(binValue);
        } else if (isByte || isShort || isInt) {
            result = String.valueOf(intValue);
        } else if (isLong) {
            result = String.valueOf(longValue);
        } else {
            throw new BDOMException("No value is set");
        }
        return result;
    }

    public byte[] getValueAsByteArray() throws BDOMException {
        byte[] result = null;
        if (binValue != null) {
            result = binValue;
        } else if (strValue != null) {
            result = strArray;
        } else if (isInt) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            try {
                dataOut.writeInt(intValue);
                result = out.toByteArray();
            } catch (IOException e) {
                throw new BDOMException("getNodeValueAssByteArray", e);
            }
        } else if (isLong) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            try {
                dataOut.writeLong(longValue);
                result = out.toByteArray();
            } catch (IOException e) {
                throw new BDOMException("getNodeValueAssByteArray", e);
            }
        } else {
            throw new BDOMException("No value is set");
        }
        return result;
    }

    public int getValueAsInt() throws BDOMException {
        if (!isInt && !isByte && !isShort) {
            throw new BDOMException("No value is set");
        }
        return intValue;
    }

    public long getValueAsLong() throws BDOMException {
        if (!isLong) {
            return getValueAsInt();
        }
        return longValue;
    }

    public void setValue(int value) throws BDOMException {
        isInt = true;
        length = 4;
        lenSize = 0;
        intValue = value;
    }

    public void setValue(long value) {
        isLong = true;
        length = 8;
        lenSize = 0;
        longValue = value;
    }

    public void setValue(byte[] value) {
        lenSize = DocParserImpl.SIZE_LENGTH;
        isBinary = true;
        length = value.length;
        setLengthSize(length);
        binValue = value;
    }

    public void setValue(String value) throws BDOMException {
        isString = true;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream out2 = new DataOutputStream(out);
        strValue = value;
        try {
            out2.writeUTF(strValue);
        } catch (IOException e) {
            throw new BDOMException(e.getMessage());
        }
        strArray = out.toByteArray();
        length = strArray.length;
        setLengthSize(length);
    }

    private void setLengthSize(int length) {
        lenSize = DocParserImpl.SIZE_LENGTH;
        if (length > Byte.MAX_VALUE) {
            lenSize = DocParserImpl.SIZE_LENGTH_L;
        }
    }

    public boolean isBinary() {
        return isBinary;
    }

    public boolean isByte() {
        return isByte;
    }

    public boolean isShort() {
        return isShort;
    }

    public boolean isInt() {
        return isInt;
    }

    public boolean isLong() {
        return isLong;
    }

    public boolean isString() {
        return isString;
    }

    public void addLength(Counter counter) {
        counter.addLength(DocParserImpl.ATTR_HEADER_SIZE + idSize + lenSize + contentLength());
    }

    public int contentLength() {
        return length;
    }

    public String toString() {
        String result = String.valueOf(getId()) + "=\"";
        try {
            if (!isBinary) {
                result += getValue();
            } else if (binValue != null) {
                result += "Binary value. Size=" + binValue.length;
            }
        } catch (BDOMException e) {
            result += "No valid attribute value";
        }
        return result + "\"";
    }
}
