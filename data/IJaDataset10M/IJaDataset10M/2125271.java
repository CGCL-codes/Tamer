package my.LifeRockEditor;

import java.util.*;
import java.io.*;

/**
 *
 * @author timwestlake
 */
public class DfRecord {

    HashMap hm = new HashMap();

    String recordName;

    public DfRecord(String objectName, BufferedReader inputBuffer) {
        String inputLine = null;
        hm.put("objectName", objectName);
        try {
            inputLine = inputBuffer.readLine();
            while (!inputLine.equals("")) {
                String[] lineItems = inputLine.split("\t");
                switch(lineItems.length) {
                    case 2:
                        hm.put(lineItems[1], "");
                        break;
                    case 3:
                        hm.put(lineItems[1], lineItems[2]);
                        break;
                    default:
                        System.out.println("Invalid record data: > " + inputLine + " <");
                }
                inputLine = inputBuffer.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public DfRecord(String newRecordName, HashMap newHM) {
        recordName = newRecordName;
        hm = newHM;
    }

    public String getString(String attribute) {
        return (String) this.hm.get(attribute);
    }

    public Boolean getBoolean(String attribute) {
        if (this.hm.get(attribute).equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public int getInt(String attribute) {
        String interimValue = (String) this.hm.get(attribute);
        try {
            int i = Integer.parseInt(interimValue.trim());
            return i;
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
            return 0;
        }
    }

    public Boolean writeRecord(BufferedWriter outFile) {
        Boolean didWrite = true;
        Set set = hm.entrySet();
        Iterator i = set.iterator();
        try {
            outFile.write(recordName + "\n");
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                outFile.write("\t" + me.getKey() + "\t" + me.getValue() + "\n");
            }
            outFile.write("\n");
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            didWrite = false;
        }
        return didWrite;
    }
}
