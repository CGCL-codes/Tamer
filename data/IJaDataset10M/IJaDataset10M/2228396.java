package net.sf.myway.gps.garmin.protocol;

import net.sf.myway.gps.DeviceDataType;
import net.sf.myway.gps.unit.CommandName;

/**
 * @version $Revision: 1.3 $
 * @author andreas
 */
public class DateAndTimeInitializationProtocol extends SingleMessageProtocol {

    /**
	 * Method DateAndTimeInitializationProtocol.
	 * 
	 * @param name
	 * @param linkProtocol
	 * @param deviceCommandProtocol
	 */
    public DateAndTimeInitializationProtocol(final String name, final LinkProtocol linkProtocol, final DeviceCommandProtocol deviceCommandProtocol) {
        super(name, linkProtocol, deviceCommandProtocol);
    }

    /**
	 * @see net.sf.myway.gps.garmin.protocol.SingleMessageProtocol#getCommandName()
	 */
    @Override
    protected CommandName getCommandName() {
        return CommandName.TRANSFER_TIME;
    }

    /**
	 * @see net.sf.myway.gps.garmin.protocol.SingleMessageProtocol#getDataTypename()
	 */
    @Override
    protected DataTypeName getDataTypename() {
        return DataTypeName.DATE_TIME_DATA;
    }

    /**
	 * @see net.sf.myway.gps.garmin.protocol.sourceforge.myway.gps.garmin.Protocol#getResultType()
	 */
    @Override
    public DeviceDataType getResultType() {
        return DeviceDataType.TIME;
    }
}
