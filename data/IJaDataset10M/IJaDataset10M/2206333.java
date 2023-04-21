package jmri.jmrix.powerline;

/**
 * Interface to send/receive serial information
 *
 * @author			Bob Jacobsen Copyright (C) 2001, 2006, 2007, 2008
 * @version			$Revision: 1.1 $
 */
public interface SerialInterface {

    public void addSerialListener(SerialListener l);

    public void removeSerialListener(SerialListener l);

    boolean status();

    void sendSerialMessage(SerialMessage m, SerialListener l);
}
