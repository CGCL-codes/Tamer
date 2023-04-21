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
 * Class Description: Gets whole list of the attributes registered in HDB
 */
public class GetAttNameAllClass extends Command implements TangoConst {

    public GetAttNameAllClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public GetAttNameAllClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public GetAttNameAllClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("GetAttNameAllClass.execute(): arrived");
        if (!(device instanceof HdbExtractor)) {
            Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbExtractor", "HdbExtractor");
        }
        return insert(((HdbExtractor) device).get_att_name_all());
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
