package org.snmp4ant;

import java.math.*;
import java.net.*;
import org.snmp4ant.snmp.*;

public class SNMPSample {

    public static void main(String args[]) {
        try {
            InetAddress hostAddress = InetAddress.getByName("10.0.1.1");
            String community = "public";
            int version = 0;
            SNMPv1CommunicationInterface comInterface = new SNMPv1CommunicationInterface(version, hostAddress, community);
            String itemID = "1.3.6.1.2.1.1.1.0";
            System.out.println("Retrieving value corresponding to OID " + itemID);
            SNMPVarBindList newVars = comInterface.getMIBEntry(itemID);
            SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
            SNMPObject snmpValue = pair.getSNMPObjectAt(1);
            System.out.println("Retrieved value: type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            @SuppressWarnings("unused") byte[] javaByteArrayValue = (byte[]) snmpValue.getValue();
            itemID = "1.3.6.1.2.1.1.3.0";
            System.out.println("Retrieving value corresponding to OID " + itemID);
            newVars = comInterface.getMIBEntry(itemID);
            pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
            snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
            snmpValue = pair.getSNMPObjectAt(1);
            System.out.println("Retrieved value: type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            @SuppressWarnings("unused") BigInteger javaIntegerValue = (BigInteger) snmpValue.getValue();
            String[] itemIDs = { "1.3.6.1.2.1.1.1.0", "1.3.6.1.2.1.1.2.0", "1.3.6.1.2.1.1.3.0", "1.3.6.1.2.1.1.4.0", "1.3.6.1.2.1.1.5.0" };
            System.out.println("Retrieving value corresponding to OIDs: ");
            for (int i = 0; i < itemIDs.length; i++) {
                System.out.println("  " + itemIDs[i]);
            }
            newVars = comInterface.getMIBEntry(itemIDs);
            for (int i = 0; i < newVars.size(); i++) {
                pair = (SNMPSequence) (newVars.getSNMPObjectAt(i));
                snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
                snmpValue = pair.getSNMPObjectAt(1);
                System.out.println("Retrieved value: type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            }
            System.out.println("Retrieving values _following_ OIDs: ");
            for (int i = 0; i < itemIDs.length; i++) {
                System.out.println("  " + itemIDs[i]);
            }
            newVars = comInterface.getNextMIBEntry(itemIDs);
            for (int i = 0; i < newVars.size(); i++) {
                pair = (SNMPSequence) (newVars.getSNMPObjectAt(i));
                snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
                snmpValue = pair.getSNMPObjectAt(1);
                System.out.println("Retrieved value: type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            }
            String baseID = "1.3.6.1.2.1.2.2.1";
            System.out.println("Retrieving table corresponding to base OID " + baseID);
            SNMPVarBindList tableVars = comInterface.retrieveMIBTable(baseID);
            System.out.println("Number of table entries: " + tableVars.size());
            for (int i = 0; i < tableVars.size(); i++) {
                pair = (SNMPSequence) (tableVars.getSNMPObjectAt(i));
                snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
                snmpValue = pair.getSNMPObjectAt(1);
                System.out.println("Retrieved OID: " + snmpOID + ", type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            }
            String[] baseIDs = { "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.2.2.1.2", "1.3.6.1.2.1.2.2.1.3", "1.3.6.1.2.1.2.2.1.4" };
            System.out.println("Retrieving table columns corresponding to base OIDs ");
            for (int i = 0; i < baseIDs.length; i++) {
                System.out.println("  " + baseIDs[i]);
            }
            tableVars = comInterface.retrieveMIBTable(baseIDs);
            System.out.println("Number of table entries: " + tableVars.size());
            for (int i = 0; i < tableVars.size(); i++) {
                pair = (SNMPSequence) (tableVars.getSNMPObjectAt(i));
                snmpOID = (SNMPObjectIdentifier) pair.getSNMPObjectAt(0);
                snmpValue = pair.getSNMPObjectAt(1);
                System.out.println("Retrieved OID: " + snmpOID + ", type " + snmpValue.getClass().getName() + ", value " + snmpValue.toString());
            }
            itemID = "1.3.6.1.2.1.1.4.0";
            SNMPOctetString newValue = new SNMPOctetString("Jon S");
            System.out.println("Setting value corresponding to OID " + itemID);
            System.out.println("New value: " + newValue.toString());
            newVars = comInterface.setMIBEntry(itemID, newValue);
            String[] setItemIDs = { "1.3.6.1.2.1.1.4.0", "1.3.6.1.2.1.1.5.0" };
            SNMPOctetString[] newValues = { new SNMPOctetString("Jon"), new SNMPOctetString("Jon's device") };
            System.out.println("Setting value corresponding to OIDs " + itemID);
            for (int i = 0; i < setItemIDs.length; i++) {
                System.out.println("  " + setItemIDs[i] + ", new values " + newValues[i]);
            }
            newVars = comInterface.setMIBEntry(setItemIDs, newValues);
        } catch (Exception e) {
            System.out.println("Exception during SNMP operation:  " + e + "\n");
        }
    }
}
