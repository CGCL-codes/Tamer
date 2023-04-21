package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
*
* ReportSM-DeliveryStatusRes ::= SEQUENCE {
* 	storedMSISDN	ISDN-AddressString	OPTIONAL,
* 	extensionContainer	ExtensionContainer	OPTIONAL,
* 	...}
* 
* 
* 
* @author sergey vetyutnev
* 
*/
public interface ReportSMDeliveryStatusResponseIndication extends SmsMessage {

    public ISDNAddressString getStoredMSISDN();

    public MAPExtensionContainer getExtensionContainer();
}
