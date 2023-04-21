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
 * Class Description: Gets whole list of the attributes registered in TDB and
 * that belong to the current facility.
 */
public class GetAttNameFacilityClass extends Command implements TangoConst {

    public GetAttNameFacilityClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetAttNameFacilityClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetAttNameFacilityClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetAttNameFacilityClass.execute(): arrived");
        if (!(device instanceof TdbExtractor)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of TdbExtractor", "TdbExtractor");
        }
        return insert(((TdbExtractor) device).get_att_name_facility());
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
