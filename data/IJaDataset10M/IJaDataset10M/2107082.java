package lv.odylab.evedb.domain;

public class NameNotFoundException extends RuntimeException {

    public NameNotFoundException(String typeName) {
        super("Name not found: " + typeName);
    }
}
