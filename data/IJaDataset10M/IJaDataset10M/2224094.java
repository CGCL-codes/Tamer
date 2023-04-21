package pt.iscte.dsi.taa.policies.state;

public class IllegalModificationException extends RuntimeException {

    protected IllegalModificationException() {
    }

    protected IllegalModificationException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 5563839492335110077L;
}
