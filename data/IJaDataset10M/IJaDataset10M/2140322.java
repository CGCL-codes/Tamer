package net.sourceforge.jpcap.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A thread that populates a host name in a host renderer.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 540 $
 * @lastModifiedBy $Author: buchmand $
 * @lastModifiedAt $Date: 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) $
 */
public class HostNameLookupThread extends Thread {

    HostNameLookupThread(String ipAddress, HostRenderer renderer) {
        this.ipAddress = ipAddress;
        this.renderer = renderer;
        start();
    }

    public void run() {
        try {
            InetAddress ia = InetAddress.getByName(ipAddress);
            String name = ia.getHostName();
            renderer.erase();
            renderer.setHostName(name);
            renderer.paint();
        } catch (UnknownHostException e) {
        }
    }

    String ipAddress;

    HostRenderer renderer;

    private String _rcsid = "$Id: HostNameLookupThread.java 540 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) buchmand $";
}
