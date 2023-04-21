package snmp;

import java.net.*;

/**
*    SNMPv2InformRequestListener is an interface that must be implemented by any class which wishes to act as 
*    a "listener" for SNMPv2 inform request messages sent from remote SNMP entities to an instance of the 
*    SNMPTrapListenerInterface class. The SNMPInformRequestListenerInterface class listens for inform request
*  	 messages, and passes any it receives on to SNMPv2InformRequestListener implementations that have registered 
* 	 with it through its addvInformRequestListener() method.
*/
public interface SNMPv2InformRequestListener {

    public void processv2InformRequest(SNMPv2InformRequestPDU informRequestPDU, String communityName, InetAddress senderIPAddress);
}
