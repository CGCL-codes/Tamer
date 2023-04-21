package interfaces;

/**
 * This interface depicts the object which gets observed by an observer.
 * --> Gang-of-four behavioral design pattern
 * 
 * @author Thilo Muth
 *
 */
public interface Observable {

    public void notifyObservers();

    public void register(Observer obs);

    public void deregister(Observer obs);
}
