package HdbExtractor;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description: Returns the number of data lower than the given value x OR
 * higher than the given value y.
 */
public class GetAttDataInfOrSupThanCountClass extends Command implements TangoConst {

    public GetAttDataInfOrSupThanCountClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetAttDataInfOrSupThanCountClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetAttDataInfOrSupThanCountClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetAttDataInfOrSupThanCountClass.execute(): arrived");
        final String[] argin = extract_DevVarStringArray(in_any);
        if (!(device instanceof HdbExtractor)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbExtractor", "HdbExtractor");
        }
        return insert(((HdbExtractor) device).get_att_data_inf_or_sup_than_count(argin));
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
