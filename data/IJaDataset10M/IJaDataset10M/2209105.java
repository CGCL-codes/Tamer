package HdbArchiver;

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
 * Class Description:
 * 
 */
public class GetErrorDomainsInternal extends Command implements TangoConst {

    public GetErrorDomainsInternal(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetErrorDomainsInternal(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetErrorDomainsInternal(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetErrorDomainsCurrentClass.execute(): arrived");
        if (!(device instanceof HdbArchiver)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbArchiver", "HdbArchiver");
        }
        return insert(((HdbArchiver) device).get_error_domains_internal());
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
            return false;
        }
        return true;
    }
}
