package csiebug.util;

/**
 * @author George_Tsai
 * @version 2010/1/20
 */
public interface Observable {

    void addObserver(Observer observer);

    void addObservers(Observer[] observers);

    void removeObserver(Observer observer);

    void notifyObservers();
}
