package org.devyant.magicbeans.observer;

/**
 * SubjectBehaviour is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision: 1.1 $ $Date: 2005/11/16 22:03:52 $ ($Author: ftavares $)
 * @since 19/Jun/2005 5:48:57
 */
public interface SubjectBehaviour extends Subject {

    public void notifyObservers(Subject subject);
}
