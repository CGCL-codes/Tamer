package tutorial.DesignPatterns.Observer.observer;

/**
 * Eventul care se publica si datele(metadata) care le contine
 * despre evenimentul in cauza (ce ii si de unde vine)
 * 
 * @author Boogie
 */
public class CaineEvent {

    public static final int HUNGRY = 1, SLEEPY = 2;

    private int eventType;

    private Caine source;

    public CaineEvent(int eventType, Caine source) {
        this.eventType = eventType;
        this.source = source;
    }

    public int getEventType() {
        return eventType;
    }

    public Caine getSource() {
        return source;
    }
}
