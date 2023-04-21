package snmp;

/**
*    Exception thrown when attempt to get value of SNMP OID from device fails. Reason could be that 
*    specified variable not supported by device, or that supplied community name has insufficient
*    privileges.
*/
public class SNMPGetException extends SNMPRequestException {

    /**
    *    Create exception with errorIndex and errorStatus
    */
    public SNMPGetException(int errorIndex, int errorStatus) {
        super(errorIndex, errorStatus);
    }

    /**
    *    Create exception with errorIndex, errorStatus and message string
    */
    public SNMPGetException(String message, int errorIndex, int errorStatus) {
        super(message, errorIndex, errorStatus);
    }
}
