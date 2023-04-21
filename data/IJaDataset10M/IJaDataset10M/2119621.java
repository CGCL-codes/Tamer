package org.jfonia.connect5.basics;

/**
 * @author wijnand.schepens@hogent.be
 */
public interface Node {

    public void addObserver(Observer o);

    public void removeObserver(Observer o);

    public boolean containsObserver(Observer o);

    public void notifyObservers();

    public void notifyObserversExcept(Observer source);
}
