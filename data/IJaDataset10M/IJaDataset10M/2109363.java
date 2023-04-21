package jhomenet.server.net;

import java.util.List;
import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.BaseItem;
import gnu.cajo.utils.extra.ClientProxy;
import jhomenet.commons.hw.UnregisteredHardware;
import jhomenet.commons.hw.mngt.HardwareManager;

/**
 * This class wraps a HardwareManager object in a server item suitable for
 * responding to remote client requests for HardwareManager information. While
 * this classes doesn't implement the IHardwareManager interface, it does
 * implement the methods defined in this interface. <br>
 * Id: $Id: $
 * 
 * @author dirwin
 */
public class HardwareManagerItem extends BaseItem {

    /**
	 * A reference to the HardwareManager proxy object.
	 */
    private Object proxy;

    /**
	 * Reference to the ClientProxy object.
	 */
    private ClientProxy clientProxy;

    /**
	 * Reference to the hardware manager object.
	 */
    private final HardwareManager hardwareManager;

    /**
	 * Constructor.
	 * 
	 * @param hardwareManager
	 */
    public HardwareManagerItem(HardwareManager hardwareManager) {
        super();
        this.hardwareManager = hardwareManager;
    }

    /**
	 * Get a reference to the ClientProxy object.
	 * 
	 * @return A reference to the item's ClientProxy object
	 * @throws Exception
	 */
    public Remote getCP() throws Exception {
        clientProxy = new ClientProxy();
        return clientProxy.remoteThis;
    }

    /**
	 * @see gnu.cajo.utils.BaseItem#getDescription()
	 */
    @Override
    public String getDescription() {
        return "This method defines XXX application specific methods:\n\n" + "   List<UnregisteredHardware> getUnregisteredHardwareList();\n\n" + "It is expected to be called by its proxy, following its arrival\n" + "at its remote host.";
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "Hardware manager server item";
    }

    /**
	 * Get the list of currently unregistered hardware.
	 * 
	 * @param proxy
	 *            Reference to the remote client proxy object
	 * @return A list of currently unregistered hardware
	 */
    public List<UnregisteredHardware> getUnregisteredHardwareList(Object proxy) {
        this.proxy = proxy;
        return null;
    }
}
