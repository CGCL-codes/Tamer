package TdbExtractor;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description: Retrieves data beetwen two dates, for a given scalar
 * attribute. Create a dynamic attribute, retrieve data from database and
 * prepare result for attribute_history call.
 */
public class GetAttDataBetweenDatesSamplingClass extends Command implements TangoConst {

    public GetAttDataBetweenDatesSamplingClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetAttDataBetweenDatesSamplingClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetAttDataBetweenDatesSamplingClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetAttDataBetweenDatesSamplingClass.execute(): arrived");
        final String[] argin = extract_DevVarStringArray(in_any);
        if (!(device instanceof TdbExtractor)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of TdbExtractor", "TdbExtractor");
        }
        return insert(((TdbExtractor) device).get_att_data_between_dates_sampling(argin));
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
