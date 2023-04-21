package net.sf.picomapping;

public class PicomappingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PicomappingException(Exception e) {
        super(e);
    }

    public PicomappingException(String message) {
        super(message);
    }
}
