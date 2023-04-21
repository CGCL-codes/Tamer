package fr.esrf.TangoDs;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevVarLongStringArray;
import org.omg.CORBA.Any;

public class UpdObjPollingPeriodCmd extends Command {

    UpdObjPollingPeriodCmd(String name, int in, int out, String in_desc) {
        super(name, in, out);
        set_in_type_desc(in_desc);
    }

    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out4.println("UpdObjPollingPeriodCmd.execute(): arrived ");
        DevVarLongStringArray argin = extract_DevVarLongStringArray(in_any);
        ((DServer) (device)).upd_obj_polling(argin);
        return insert();
    }
}
