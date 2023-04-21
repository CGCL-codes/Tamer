package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * SMS-COMMAND pdu
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SmsCommandTpdu extends SmsTpdu {

    /**
	 * @return TP-UDHI field
	 */
    public boolean getUserDataHeaderIndicator();

    /**
	 * @return TP-SRR field
	 */
    public boolean getStatusReportRequest();

    /**
	 * @return TP-MR field
	 */
    public int getMessageReference();

    /**
	 * @return TP-PID field
	 */
    public ProtocolIdentifier getProtocolIdentifier();

    /**
	 * @return TP-CT field
	 */
    public CommandType getCommandType();

    /**
	 * @return TP-MN field
	 */
    public int getMessageNumber();

    /**
	 * @return TP-DA field
	 */
    public AddressField getDestinationAddress();

    /**
	 * @return TP-CDL field
	 */
    public int getCommandDataLength();

    /**
	 * @return TP-CD field
	 */
    public CommandData getCommandData();
}
