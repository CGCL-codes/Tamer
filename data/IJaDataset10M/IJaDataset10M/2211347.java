package dovetaildb.dbservice;

public abstract class AbstractTermEncoder implements TermEncoder {

    public Object decode(String key, byte[] encoded) {
        return decode(key, encoded, 0, encoded.length);
    }

    public Object decode(String key, byte[] encoded, int offset) {
        return decode(key, encoded, offset, encoded.length - offset);
    }
}
