package arm.beans;

import java.io.FileNotFoundException;

public class MissingBondException extends RuntimeException {

    public MissingBondException(FileNotFoundException e) {
        super(e);
    }

    public MissingBondException(String msg) {
        super(msg);
    }
}
