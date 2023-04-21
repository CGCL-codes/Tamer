package it.xargon.marshal;

import it.xargon.util.Bitwise;

public class MarShort extends AbstractMarshaller {

    public MarShort() {
        super("SHORT", Source.MEMORY, Short.class);
    }

    public byte[] marshalToMemory(Object obj) {
        return Bitwise.shortToByteArray(((Short) obj).shortValue());
    }

    public Object unmarshalFromMemory(byte[] contents) {
        return new Short(Bitwise.byteArrayToShort(contents));
    }
}
