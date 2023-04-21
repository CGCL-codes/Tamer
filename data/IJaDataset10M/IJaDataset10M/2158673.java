package ArchivingManager;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.Util;

/**
 * Class Description: This command configures the connection to the historical
 * and temporary databases. It then needs 4 parameters : a login and password
 * for Hdb + a login and password for Tdb
 */
public class ArchivingConfigureCmd extends Command {

    public ArchivingConfigureCmd(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public ArchivingConfigureCmd(final String name, final int in, final int out, final String inComments, final String outComments) {
        super(name, in, out, inComments, outComments);
    }

    public ArchivingConfigureCmd(final String name, final int in, final int out, final String inComments, final String outComments, final DispLevel level) {
        super(name, in, out, inComments, outComments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("ArchivingConfigureCmd.execute(): arrived");
        if (!(device instanceof ArchivingManager)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of ArchivingManager", "ArchivingManager");
        }
        final String[] argin = extract_DevVarStringArray(in_any);
        ((ArchivingManager) device).archiving_configure(argin);
        return insert();
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any dataIn) {
        return true;
    }
}
