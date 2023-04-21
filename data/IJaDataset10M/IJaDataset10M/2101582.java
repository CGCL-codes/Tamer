package TdbArchivingWatcher.Commands;

import org.omg.CORBA.Any;
import TdbArchivingWatcher.TdbArchivingWatcher;
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
public class GetReportCurrentClass extends Command implements TangoConst {

    public GetReportCurrentClass(String name, int in, int out) {
        super(name, in, out);
    }

    public GetReportCurrentClass(String name, int in, int out, String in_comments, String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetReportCurrentClass(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        if (!(device instanceof TdbArchivingWatcher)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of TdbArchivingWatcher", "TdbArchivingWatcher");
        }
        Util.out2.println("GetReportCurrentClass.execute(): arrived");
        return insert(((TdbArchivingWatcher) (device)).get_report_current());
    }

    @Override
    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
            return false;
        }
        return true;
    }
}
