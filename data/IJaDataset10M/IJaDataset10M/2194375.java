package TdbArchivingWatcher;

import org.omg.CORBA.*;
import fr.esrf.Tango.*;
import fr.esrf.TangoDs.*;

/**
 *	Class Description:
 *	
*/
public class GetErrorsForDomainCurrentClass extends Command implements TangoConst {

    public GetErrorsForDomainCurrentClass(String name, int in, int out) {
        super(name, in, out);
    }

    public GetErrorsForDomainCurrentClass(String name, int in, int out, String in_comments, String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetErrorsForDomainCurrentClass(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out2.println("GetErrorsForDomainCurrentClass.execute(): arrived");
        String argin = extract_DevString(in_any);
        return insert(((TdbArchivingWatcher) (device)).get_errors_for_domain_current(argin));
    }

    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
            return false;
        }
        return true;
    }
}
