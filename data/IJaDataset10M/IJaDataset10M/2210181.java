package org.mndacs.kernel.interfaces;

import java.util.EventListener;
import org.mndacs.kernel.events.DALEvent;

/**
 * Interface to listen for driver events send by DAL.
 * @author christopherwagner
 */
public interface DALListener extends EventListener {

    /**
      * driver was added to DAL list
      * @param e
      */
    public void addedDriver(DALEvent e);

    /**
      * driver was removed from DAL list
      * @param e
      */
    public void removedDriver(DALEvent e);

    /**
      *  driver was updated at DAL list
      * @param e
      */
    public void updatedDriver(DALEvent e);

    /**
      *  driver was marked offline at DAL list
      * @param e
      */
    public void isofflineDriver(DALEvent e);
}
