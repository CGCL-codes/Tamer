package swf9.types;

import swf9.util.ByteUtils;
import swf9.tags.*;

public class LINESTYLEARRAY {

    public int LineStyleCount;

    public int LineStyleCountExtended;

    public LINESTYLE[] LineStyles;

    public int length = 0;

    public LINESTYLEARRAY() {
        LineStyleCount = 0;
        LineStyleCountExtended = 0;
        LineStyles = new LINESTYLE[1];
        length = 1;
    }

    public LINESTYLEARRAY(byte[] data, int offset, Object type) {
        LineStyleCount = getLineStyleCount(data, offset);
        offset++;
        LineStyles = getLineStyles(data, offset, type);
        if (LineStyleCountExtended != 0) length += 5; else length++;
    }

    public void addLineStyle(LINESTYLE style) {
        LINESTYLE[] styles = new LINESTYLE[LineStyles.length + 1];
        for (int i = 0; i < LineStyles.length; i++) styles[i] = LineStyles[i];
        styles[LineStyles.length] = style;
        LineStyles = styles;
        LineStyleCount = LineStyles.length - 1;
        for (int i = 1; i <= LineStyleCount; i++) length += LineStyles[i].length;
    }

    public int getLineStyleCount(byte[] data, int offset) {
        if (data[offset] == 0xFF) {
            LineStyleCountExtended = getLineStyleCountExtended(data);
        }
        return data[offset];
    }

    public int getLineStyleCount(byte[] data, int offset, int offsetBit) {
        if (data[offset] == 0xFF) {
            LineStyleCountExtended = getLineStyleCountExtended(data);
        }
        return data[offset];
    }

    public int getLineStyleCountExtended(byte[] data) {
        byte[] extCount = new byte[2];
        System.arraycopy(data, 1, extCount, 0, 2);
        return ByteUtils.byteArrayToShort(extCount);
    }

    public LINESTYLE[] getLineStyles(byte[] data, int offset, Object type) {
        LINESTYLE[] styles;
        length = offset;
        if (LineStyleCount == 0) {
            styles = new LINESTYLE[1];
            styles[0] = new LINESTYLE();
        } else if (LineStyleCount < 0xFF) {
            styles = new LINESTYLE[LineStyleCount + 1];
        } else {
            styles = new LINESTYLE[LineStyleCountExtended + 1];
            offset += 2;
        }
        for (int i = 1; i < styles.length; i++) {
            if (((Tag) type).type == Tag.DefineShape4) styles[i] = new LINESTYLE2(data, offset, type); else styles[i] = new LINESTYLE(data, offset, type);
            offset += styles[i].length;
        }
        length = offset - length;
        return styles;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[length];
        bytes[0] = (byte) LineStyleCount;
        if (LineStyleCount != 0) {
            int offset = 1;
            int l = LineStyleCount;
            if (bytes[0] == 0xFF) {
                System.arraycopy(ByteUtils.intToByte(LineStyleCountExtended, 2, true), 0, bytes, 1, 2);
                offset += 2;
                l = LineStyleCountExtended;
            }
            for (int i = 1; i <= l; i++) {
                System.arraycopy(LineStyles[i].toByteArray(), 0, bytes, offset, LineStyles[i].length);
                offset += LineStyles[i].length;
            }
        }
        return bytes;
    }
}
