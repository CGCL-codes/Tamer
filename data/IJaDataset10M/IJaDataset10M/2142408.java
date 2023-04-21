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
 * Class Description: Retrieves data beetwen two dates (date_1 & date_2) - Data
 * are lower than the given value x. Create a dynamic attribute, retrieve the
 * corresponding data from database and prepare result for attribute_history
 * call.
 */
public class GetAttDataInfThanBetweenDatesClass extends Command implements TangoConst {

    public GetAttDataInfThanBetweenDatesClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetAttDataInfThanBetweenDatesClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetAttDataInfThanBetweenDatesClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetAttDataInfThanBetweenDatesClass.execute(): arrived");
        final String[] argin = extract_DevVarStringArray(in_any);
        final Any result = null;
        if (!(device instanceof HdbExtractor)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbExtractor", "HdbExtractor");
        }
        return insert(((HdbExtractor) device).get_att_data_inf_than_between_dates(argin));
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
