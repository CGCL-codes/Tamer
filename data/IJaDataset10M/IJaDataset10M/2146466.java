package usb.remote;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import usb.core.Host;
import usb.core.HostFactory;

/**
 * Bootstrapping methods.
 *
 * @version $Id: RemoteHostFactory.java,v 1.1 2000/11/20 17:44:31 dbrownell Exp $
 */
public final class RemoteHostFactory extends HostFactory {

    private static Host self;

    /**
     * Not part of the API.
     * This is part of the SPI for the reference implementation.
     */
    public RemoteHostFactory() {
    }

    /**
     * Not part of the API.
     * This is part of the SPI for the reference implementation.
     */
    public Host createHost() throws IOException {
        return getHost();
    }

    /**
     * Returns a client side proxy for a USB Host.
     */
    public static Host getHost() throws IOException {
        synchronized (RemoteHostFactory.class) {
            if (self != null) return self;
            try {
                self = (Host) Naming.lookup("usb.core.Host");
            } catch (ConnectException e) {
            } catch (NotBoundException e) {
            }
        }
        return self;
    }
}
