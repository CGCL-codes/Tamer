package fr.irisa.asap.debug.manager;

import java.io.*;
import java.util.*;

public class Scenario implements Serializable {

    /**
	 * for the serialization
	 */
    private static final long serialVersionUID = 1L;

    private String appliName;

    private HashMap<String, LinkedList<String>> pcList;

    private int pcNumber;

    private String date;

    private String logLink;

    public Scenario() {
        Calendar c = Calendar.getInstance();
        String res = "";
        res += String.valueOf(c.get(Calendar.YEAR));
        res += "_";
        res += String.valueOf(c.get(Calendar.MONTH));
        res += "_";
        res += String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        res += "-";
        res += String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        res += "_";
        res += String.valueOf(c.get(Calendar.MINUTE));
        date = res;
        pcList = new HashMap<String, LinkedList<String>>();
    }

    /**
	 * Return the name of the application associate to the Scenario
	 */
    public String getAppliName() {
        return appliName;
    }

    /**
	 * Return the list of the computers associate to the Scenario
	 */
    public HashMap<String, LinkedList<String>> getPcList() {
        return pcList;
    }

    /**
	 * Return the number of the computers associate to the Scenario
	 */
    public int getPcNumber() {
        return pcNumber;
    }

    /**
	 * Return the date associate to the Scenario
	 */
    public String getDate() {
        return date;
    }

    /**
	 * Return the link of the log's file associate to the Scenario
	 */
    public String getLogLink() {
        return logLink;
    }

    /**
	 * @param appliName The appliName to set.
	 */
    public void setAppliName(String appliName, String num) {
        String[] tab = appliName.split("/");
        String r = tab[tab.length - 1];
        this.appliName = num + "_" + r;
    }

    /**
	 * @param date The date to set.
	 */
    public void setDate(String date) {
        this.date = date;
    }

    /**
	 * @param logLink The logLink to set.
	 */
    public void setLogLink(String logLink) {
        this.logLink = logLink;
    }

    /**
	 * @param pcList The pcList to set.
	 */
    public void setPcList(HashMap<String, LinkedList<String>> pcList) {
        this.pcList = pcList;
        this.pcNumber = pcList.size();
    }

    /**
	 * @param pcNumber The pcNumber to set.
	 */
    public void setPcNumber(int pcNumber) {
        this.pcNumber = pcNumber;
    }

    /**
	 * Method wich add a computer on the current scenario's list of computers
	 * @param pcName
	 */
    public void addPcName(String pcName) throws Exception {
        String[] tabKeyVal = pcName.split(":");
        String key = tabKeyVal[0];
        String vals = tabKeyVal[1];
        String[] tabVal = vals.split(",");
        LinkedList<String> listVal = new LinkedList<String>();
        for (int i = 0; i < tabVal.length; i++) {
            listVal.add(tabVal[i]);
        }
        this.pcList.put(key, listVal);
        this.pcNumber++;
    }

    /**
	 * Method which remove a computer from the scenario's list of the computers
	 * @param pcName the name of the computer to delete
	 */
    public void removePcFromList(String pcName) {
        this.pcList.remove(pcName);
        this.pcNumber--;
    }

    /**
	 * Method which set the current scenario's list of computers from a file
	 * @param fileName name of the file wich contain the computer's list
	 * @throws Exception 
	 */
    public void setPcListFromFile(String fileName) throws Exception {
        String enough = null;
        File f = new File(fileName);
        FileReader fr = null;
        fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        while ((enough = br.readLine()) != null) {
            enough = enough.replace(';', ' ').toLowerCase().trim();
            addPcName(enough);
        }
    }

    public static void main(String[] args) {
        Scenario sc = new Scenario();
        System.out.println(sc.pcList.get(0));
        System.out.println(sc.pcList.get(1));
        System.out.println(sc.pcList.get(2));
    }
}
