package net.infonode.properties.propertymap.value;

import java.io.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.7 $
 */
public class ValueDecoder {

    public static final int SIMPLE = 0;

    public static final int REF = 1;

    private ValueDecoder() {
    }

    public static void skip(ObjectInputStream in) throws IOException {
        int type = in.readInt();
        switch(type) {
            case SIMPLE:
                SimplePropertyValue.skip(in);
                break;
            case REF:
                PropertyRefValue.skip(in);
                break;
            default:
                throw new IOException("Invalid value type!");
        }
    }
}
