package fr.esrf.TangoApi;

import fr.esrf.Tango.DevFailed;
import java.util.Hashtable;

/**
 *	Class Description:
 *	This class manage a static HashTable of admin device objects used for DeviceProxy.
 *
 * @author  verdier
 * @version  $Revision: 11699 $
 */
class DServer {

    private static DServer instance = null;

    private static Hashtable adm_dev_table = null;

    private DServer() {
        adm_dev_table = new Hashtable();
    }

    static DServer get_instance() {
        if (instance == null) instance = new DServer();
        return instance;
    }

    DeviceProxy get_adm_dev(String devname) throws DevFailed {
        if (adm_dev_table.containsKey(devname)) return (DeviceProxy) adm_dev_table.get(devname); else {
            DeviceProxy dev = new DeviceProxy(devname);
            adm_dev_table.put(devname, dev);
            return dev;
        }
    }
}
