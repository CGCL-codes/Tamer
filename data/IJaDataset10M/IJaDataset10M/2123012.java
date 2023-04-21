package TdbArchivingWatcher;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 *  Class Description:
 *  
*/
public class AdminReportClass extends Command implements TangoConst {

    public AdminReportClass(String name, int in, int out) {
        super(name, in, out);
    }

    public AdminReportClass(String name, int in, int out, String in_comments, String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public AdminReportClass(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out2.println("AdminReportClass.execute(): arrived");
        return insert(((TdbArchivingWatcher) (device)).get_admin_report());
    }

    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
            return false;
        }
        return true;
    }
}
