package snmp;

import java.util.*;

/**
*    Defines class for holding physical 6-byte addresses.
*/
public class SNMPNSAPAddress extends SNMPOctetString {

    /**
    *    Initialize address to 0.0.0.0.0.0.
    */
    public SNMPNSAPAddress() {
        tag = SNMPBERCodec.SNMPNSAPADDRESS;
        data = new byte[6];
        for (int i = 0; i < 6; i++) data[i] = 0;
    }

    public SNMPNSAPAddress(String string) throws SNMPBadValueException {
        tag = SNMPBERCodec.SNMPNSAPADDRESS;
        data = parseNSAPAddress(string);
    }

    /** 
    *    Used to initialize from the BER encoding, as received in a response from 
    *     an SNMP device responding to an SNMPGetRequest, or from a supplied byte array
    *    containing the address components.
    *     @throws SNMPBadValueException Indicates an invalid array supplied: must have length 6.
    */
    public SNMPNSAPAddress(byte[] enc) throws SNMPBadValueException {
        tag = SNMPBERCodec.SNMPNSAPADDRESS;
        if (enc.length == 6) {
            data = enc;
        } else {
            throw new SNMPBadValueException(" NSAPAddress: bad BER encoding supplied to set value ");
        }
    }

    /** 
    *    Used to set the value from a byte array containing the address.
    *     @throws SNMPBadValueException Indicates an incorrect object type supplied, or array of
    *    incorrect size.
    */
    public void setValue(Object newAddress) throws SNMPBadValueException {
        if ((newAddress instanceof byte[]) && (((byte[]) newAddress).length == 6)) data = (byte[]) newAddress; else if (newAddress instanceof String) {
            data = parseNSAPAddress((String) newAddress);
        } else throw new SNMPBadValueException(" NSAPAddress: bad length byte string supplied to set value ");
    }

    /** 
    *     Return pretty-printed (dash-separated) address.
    */
    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer();
        if (data.length > 0) {
            int convert = data[0];
            if (convert < 0) convert += 256;
            returnStringBuffer.append(Integer.toHexString(convert));
            for (int i = 1; i < data.length; i++) {
                convert = data[i];
                if (convert < 0) convert += 256;
                returnStringBuffer.append("-");
                returnStringBuffer.append(Integer.toHexString(convert));
            }
        }
        return returnStringBuffer.toString();
    }

    private byte[] parseNSAPAddress(String addressString) throws SNMPBadValueException {
        try {
            StringTokenizer st = new StringTokenizer(addressString, " .-");
            int size = 0;
            while (st.hasMoreTokens()) {
                size++;
                st.nextToken();
            }
            if (size != 6) {
                throw new SNMPBadValueException(" NSAPAddress: wrong number of components supplied to set value ");
            }
            byte[] returnBytes = new byte[size];
            st = new StringTokenizer(addressString, " .-");
            for (int i = 0; i < size; i++) {
                int addressComponent = (Integer.parseInt(st.nextToken(), 16));
                if ((addressComponent < 0) || (addressComponent > 255)) throw new SNMPBadValueException(" NSAPAddress: invalid component supplied to set value ");
                returnBytes[i] = (byte) addressComponent;
            }
            return returnBytes;
        } catch (NumberFormatException e) {
            throw new SNMPBadValueException(" NSAPAddress: invalid component supplied to set value ");
        }
    }
}
