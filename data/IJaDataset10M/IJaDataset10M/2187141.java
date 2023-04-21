package TdbExtractor;

import org.omg.CORBA.Any;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description: Remove the dynamic attribute.
 */
public class RemoveDynamicAttributeClass extends Command implements TangoConst {

    public RemoveDynamicAttributeClass(final String name, final int in, final int out) {
        super(name, in, out);
    }

    public RemoveDynamicAttributeClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
        super(name, in, out, in_comments, out_comments);
    }

    public RemoveDynamicAttributeClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    @Override
    public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
        Util.out2.println("RemoveDynamicAttributeClass.execute(): arrived");
        final String argin = extract_DevString(in_any);
        if (device instanceof TdbExtractor) {
            ((TdbExtractor) device).remove_dynamic_attribute(argin);
        }
        return insert();
    }

    @Override
    public boolean is_allowed(final DeviceImpl device, final Any data_in) {
        return true;
    }
}
