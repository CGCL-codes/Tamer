package HdbArchivingWatcher.Commands.AdminReport;

import org.omg.CORBA.Any;
import HdbArchivingWatcher.HdbArchivingWatcher;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description:
 * 
 */
public class GetListOfKOAttributesClass extends Command implements TangoConst {

    public GetListOfKOAttributesClass(String name, int in, int out) {
        super(name, in, out);
    }

    public GetListOfKOAttributesClass(String name, int in, int out, String in_comments, String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetListOfKOAttributesClass(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out2.println("GetListOfKOAttributesClass.execute(): arrived");
        if (!(device instanceof HdbArchivingWatcher)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbArchivingWatcher", "HdbArchivingWatcher");
        }
        return insert(((HdbArchivingWatcher) (device)).get_ko_attributes());
    }

    @Override
    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
            return false;
        }
        return true;
    }
}
