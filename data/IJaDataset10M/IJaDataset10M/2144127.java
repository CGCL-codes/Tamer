package org.apache.hadoop.record.meta;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.record.RecordInput;

/**
 * Various utility functions for Hadooop record I/O platform.
 */
public class Utils {

    /** Cannot create a new instance of Utils */
    private Utils() {
    }

    /**
   * read/skip bytes from stream based on a type
   */
    public static void skip(RecordInput rin, String tag, TypeID typeID) throws IOException {
        switch(typeID.typeVal) {
            case TypeID.RIOType.BOOL:
                rin.readBool(tag);
                break;
            case TypeID.RIOType.BUFFER:
                rin.readBuffer(tag);
                break;
            case TypeID.RIOType.BYTE:
                rin.readByte(tag);
                break;
            case TypeID.RIOType.DOUBLE:
                rin.readDouble(tag);
                break;
            case TypeID.RIOType.FLOAT:
                rin.readFloat(tag);
                break;
            case TypeID.RIOType.INT:
                rin.readInt(tag);
                break;
            case TypeID.RIOType.LONG:
                rin.readLong(tag);
                break;
            case TypeID.RIOType.MAP:
                org.apache.hadoop.record.Index midx1 = rin.startMap(tag);
                MapTypeID mtID = (MapTypeID) typeID;
                for (; !midx1.done(); midx1.incr()) {
                    skip(rin, tag, mtID.getKeyTypeID());
                    skip(rin, tag, mtID.getValueTypeID());
                }
                rin.endMap(tag);
                break;
            case TypeID.RIOType.STRING:
                rin.readString(tag);
                break;
            case TypeID.RIOType.STRUCT:
                rin.startRecord(tag);
                StructTypeID stID = (StructTypeID) typeID;
                Iterator<FieldTypeInfo> it = stID.getFieldTypeInfos().iterator();
                while (it.hasNext()) {
                    FieldTypeInfo tInfo = it.next();
                    skip(rin, tag, tInfo.getTypeID());
                }
                rin.endRecord(tag);
                break;
            case TypeID.RIOType.VECTOR:
                org.apache.hadoop.record.Index vidx1 = rin.startVector(tag);
                VectorTypeID vtID = (VectorTypeID) typeID;
                for (; !vidx1.done(); vidx1.incr()) {
                    skip(rin, tag, vtID.getElementTypeID());
                }
                rin.endVector(tag);
                break;
            default:
                throw new IOException("Unknown typeID when skipping bytes");
        }
    }
}
