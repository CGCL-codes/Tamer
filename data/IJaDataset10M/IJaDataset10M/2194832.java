package gov.lanl.registryclient;

public class ItemNotExist extends Exception {

    public ItemNotExist(String ex) {
        super("Tape Not Exist" + ex);
    }

    public ItemNotExist() {
        super("Tape Not Exist");
    }
}
