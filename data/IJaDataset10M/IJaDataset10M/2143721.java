package net.sf.myway.gps.garmin.protocol;

import net.sf.myway.gps.DeviceDataType;
import net.sf.myway.gps.garmin.datatype.CommandId;
import net.sf.myway.gps.garmin.unit.GarminData;
import net.sf.myway.gps.garmin.unit.GarminDownloadHandler;
import net.sf.myway.gps.garmin.unit.GarminMessage;
import net.sf.myway.gps.garmin.unit.GarminProductData;

/**
 * @version $Revision: 1.3 $
 * @author andreas
 */
public class ProductDataProtocol extends Protocol {

    private final LinkProtocol _linkProtocol;

    private final ExtraDataProtocol _productExtraDataProtocol;

    public ProductDataProtocol(final String name, final LinkProtocol linkProtocol, final ExtraDataProtocol productExtraDataProtocol) {
        super(name);
        _linkProtocol = linkProtocol;
        _productExtraDataProtocol = productExtraDataProtocol;
    }

    /** @return <code>true</code> wenn Message zu Protokol paßt */
    @Override
    public boolean addMessage(final GarminMessage message) {
        if (_linkProtocol.getType(message).equals(DataTypeName.ACKNOWLEDGE)) {
            final CommandId cmd = message.getData().getCommandId();
            if (cmd.getId() == _linkProtocol.getId(DataTypeName.PRODUCT_REQUEST)) return true;
            return false;
        }
        if (_linkProtocol.getType(message).equals(DataTypeName.PRODUCT_DATA)) {
            _result = new GarminProductData(message.getData());
            final CommandId cmd = new CommandId(_linkProtocol.getId(DataTypeName.PRODUCT_DATA));
            _linkProtocol.sendPackage(DataTypeName.ACKNOWLEDGE, cmd.getData(), false);
            checkComplete(cmd.getData());
            return true;
        }
        return false;
    }

    /**
	 * @param data
	 * @return
	 */
    private void checkComplete(final GarminData data) {
        _complete = true;
        setNextProtocol(_productExtraDataProtocol);
    }

    /**
	 * @see net.sf.myway.gps.garmin.protocol.sourceforge.myway.gps.garmin.Protocol#getResultType()
	 */
    @Override
    public DeviceDataType getResultType() {
        return DeviceDataType.PRODUCT_DATA;
    }

    /**
	 * Method start.
	 * 
	 * @param downloadHandler
	 */
    @Override
    public void startReceive(final GarminDownloadHandler downloadHandler) {
        downloadHandler.setProtocol(this);
        _linkProtocol.sendPackage(DataTypeName.PRODUCT_REQUEST, true);
    }
}
