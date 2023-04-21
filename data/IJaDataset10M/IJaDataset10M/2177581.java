package cn.evilelf.util.exceptions;

public class InvalidOptionException extends Exception {

    private static final long serialVersionUID = 2889558629458756213L;

    public InvalidOptionException() {
    }

    public InvalidOptionException(String curOption) {
        super(curOption);
    }
}
