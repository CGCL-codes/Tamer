package ca.ucalgary.cpsc.agilePlanner.persister;

public class NotConnectedException extends Exception {

    private static final long serialVersionUID = -582627849258483645L;

    public NotConnectedException(String text) {
        super(text);
    }
}
