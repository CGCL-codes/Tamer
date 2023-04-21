package net.java.slee.resource.diameter.rx;

import javax.slee.ActivityContextInterface;
import javax.slee.UnrecognizedActivityException;

/**
 * Declares the methods to obtain an ActivityContextInterface for Rx activities.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxActivityContextInterfaceFactory {

    /**
   * Method for obtaining ActivityContextInterface for a Rx client activity.
   *
   * @param cSession the Rx client activity
   * @return the ActivityContextInterface
   * @throws UnrecognizedActivityException
   */
    public ActivityContextInterface getActivityContextInterface(RxClientSessionActivity cSession) throws UnrecognizedActivityException;

    /**
   * Method for obtaining ActivityContextInterface for a Rx server activity.
   *
   * @param sSession the Rx server activity
   * @return the ActivityContextInterface
   * @throws UnrecognizedActivityException
   */
    public ActivityContextInterface getActivityContextInterface(RxServerSessionActivity sSession) throws UnrecognizedActivityException;
}
