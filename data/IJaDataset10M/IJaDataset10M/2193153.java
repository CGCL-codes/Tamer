package ArchivingManager;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description: Change the mode of an historical archiving.
 */
public class ArchivingModifHdbCmd extends Command implements TangoConst {

    public ArchivingModifHdbCmd(String name, int in, int out) {
        super(name, in, out);
    }

    public ArchivingModifHdbCmd(String name, int in, int out, String in_comments, String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public ArchivingModifHdbCmd(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out2.println("ArchivingModifHdbCmd.execute(): arrived");
        if (!(device instanceof ArchivingManager)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of ArchivingManager", "ArchivingManager");
        }
        String[] argin = extract_DevVarStringArray(in_any);
        ((ArchivingManager) (device)).archiving_modif_hdb(argin);
        return insert();
    }

    @Override
    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.INIT || device.get_state() == DevState.FAULT) {
            return false;
        }
        return true;
    }
}
