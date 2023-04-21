package snmp;

import java.util.*;
import java.math.*;

/**
*    The SNMPPDU class represents an SNMP Bulk PDU from RFC 1905, as indicated below. This
*    forms the payload of an SNMP message.

     GetBulkRequest-PDU ::=
         [5]
             IMPLICIT BulkPDU

 
     BulkPDU ::=                     -- MUST be identical in
         SEQUENCE {                  -- structure to PDU
             request-id
                 Integer32,

             non-repeaters
                 INTEGER (0..max-bindings),

             max-repetitions
                 INTEGER (0..max-bindings),

             variable-bindings       -- values are ignored
                 VarBindList
         }


*/
public class SNMPBulkPDU extends SNMPSequence {

    /**
    *    Create a new Bulk PDU with given request ID, non-repeaters count, and max-repetitions,
    *    and containing the supplied SNMP sequence as data.
    */
    public SNMPBulkPDU(int requestID, int nonRepeaters, int maxRepetitions, SNMPSequence varList) throws SNMPBadValueException {
        super();
        Vector contents = new Vector();
        tag = SNMPBERCodec.SNMPv2BULKREQUEST;
        contents.insertElementAt(new SNMPInteger(requestID), 0);
        contents.insertElementAt(new SNMPInteger(nonRepeaters), 1);
        contents.insertElementAt(new SNMPInteger(maxRepetitions), 2);
        contents.insertElementAt(varList, 3);
        this.setValue(contents);
    }

    /**
    *    Create a new Bulk PDU from the supplied BER encoding.
    *    @throws SNMPBadValueException Indicates invalid SNMP PDU encoding supplied in enc.
    */
    protected SNMPBulkPDU(byte[] enc) throws SNMPBadValueException {
        tag = SNMPBERCodec.SNMPv2BULKREQUEST;
        extractFromBEREncoding(enc);
        Vector contents = (Vector) (this.getValue());
        if (contents.size() != 4) {
            throw new SNMPBadValueException("Bad PDU");
        }
        if (!(contents.elementAt(0) instanceof SNMPInteger)) {
            throw new SNMPBadValueException("Bad PDU: bad request ID");
        }
        if (!(contents.elementAt(1) instanceof SNMPInteger)) {
            throw new SNMPBadValueException("Bad PDU: bad error status");
        }
        if (!(contents.elementAt(2) instanceof SNMPInteger)) {
            throw new SNMPBadValueException("Bad PDU: bad error index");
        }
        if (!(contents.elementAt(3) instanceof SNMPSequence)) {
            throw new SNMPBadValueException("Bad PDU: bad variable binding list");
        }
        SNMPSequence varBindList = this.getVarBindList();
        for (int i = 0; i < varBindList.size(); i++) {
            SNMPObject element = varBindList.getSNMPObjectAt(i);
            if (!(element instanceof SNMPSequence)) {
                throw new SNMPBadValueException("Bad PDU: bad variable binding at index" + i);
            }
            SNMPSequence varBind = (SNMPSequence) element;
            if ((varBind.size() != 2) || !(varBind.getSNMPObjectAt(0) instanceof SNMPObjectIdentifier)) {
                throw new SNMPBadValueException("Bad PDU: bad variable binding at index" + i);
            }
        }
    }

    /** 
    *    A utility method that extracts the variable binding list from the pdu. Useful for retrieving
    *    the set of (object identifier, value) pairs returned in response to a request to an SNMP
    *    device. The variable binding list is just an SNMP sequence containing the identifier, value pairs.
    *    @see snmp.SNMPVarBindList
    */
    public SNMPSequence getVarBindList() {
        Vector contents = (Vector) (this.getValue());
        return (SNMPSequence) (contents.elementAt(3));
    }

    /** 
    *    A utility method that extracts the request ID number from this PDU.
    */
    public int getRequestID() {
        Vector contents = (Vector) (this.getValue());
        return ((BigInteger) ((SNMPInteger) (contents.elementAt(0))).getValue()).intValue();
    }

    /** 
    *    A utility method that extracts the non-repeater count for this PDU.
    */
    public int getNonRepeaters() {
        Vector contents = (Vector) (this.getValue());
        return ((BigInteger) ((SNMPInteger) (contents.elementAt(1))).getValue()).intValue();
    }

    /** 
    *    A utility method that returns the max-repetitions count for this PDU.
    */
    public int getMaxRepetitions() {
        Vector contents = (Vector) (this.getValue());
        return ((BigInteger) ((SNMPInteger) (contents.elementAt(2))).getValue()).intValue();
    }

    /** 
    *    A utility method that returns the PDU type of this PDU.
    */
    public byte getPDUType() {
        return tag;
    }
}
