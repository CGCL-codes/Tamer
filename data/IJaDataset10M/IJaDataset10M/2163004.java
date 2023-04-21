package net.sf.nxtassembler.exception;

public class AssemblerException extends Exception {

    private static final long serialVersionUID = 143223L;

    public AssemblerException(Exception e) {
        super(e);
    }

    public AssemblerException(String msg) {
        super(msg);
    }

    public AssemblerException(String msg, Exception e) {
        super(msg, e);
    }
}
