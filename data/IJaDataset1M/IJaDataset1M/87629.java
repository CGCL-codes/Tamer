package jreceiver.common.rec.driver;

import java.util.Vector;
import jreceiver.common.rec.Rec;
import jreceiver.common.rec.driver.Driver;

/**
 * An interface representing a single driverbind.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:06 $
 */
public interface DriverBinding extends Rec {

    public static final String HKEY_SIGNALS = "SIGNALS";

    public static final String HKEY_MASTER_DRV = "MASTER_DRV";

    public static final String HKEY_SLAVE_DRV = "SLAVE_DRV";

    /**
    * minimally populate members of a source bean
    */
    public static final String POPULATE_SIGNALS = "POP_SIGNALS";

    /**
    * populate driverbind driver
    */
    public static final String POPULATE_DRIVER_MASTER = "POP_DRIVER_MASTER";

    /**
    * populate controlled driver
    */
    public static final String POPULATE_DRIVER_SLAVE = "POP_DRIVER_SLAVE";

    public DriverBindingKey getDriverBindingKey();

    public Vector getSignals();

    public Driver getDriverMaster();

    public Driver getDriverSlave();
}
