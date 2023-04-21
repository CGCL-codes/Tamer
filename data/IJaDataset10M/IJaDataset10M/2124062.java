package it.xargon.marshal;

import it.xargon.util.Bitwise;

public class MarCharacter extends AbstractMarshaller {

    public MarCharacter() {
        super("CHAR", Source.MEMORY, Character.class);
    }

    public byte[] marshalToMemory(Object obj) {
        return Bitwise.charToByteArray(((Character) obj).charValue());
    }

    public Object unmarshalFromMemory(byte[] contents) {
        return new Character(Bitwise.byteArrayToChar(contents));
    }

    public float getAffinity(Class<?> javaclass) {
        if (Character.class.isAssignableFrom(javaclass)) return 1f;
        return 0f;
    }
}
