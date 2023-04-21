package lv.odylab.evedb.domain;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(Long typeID) {
        super("ID not found: " + typeID);
    }
}
