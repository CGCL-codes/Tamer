package com.hisham.parking.accountaccess.util;

import java.io.*;

/**
 * <p>Title: Parking Services Online</p>
 * <p>Description: Tools for online account access, sales, citations and appeals</p>
 * @author Ali Hisham Malik
 * @version 1.3
 */
public class CustomerStatusInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1160205065408465889L;

    private int customerIdPp = -1;

    private String campusId = "";

    /**
	 * Default empty constructor
	 */
    public CustomerStatusInfo() {
    }

    public CustomerStatusInfo(int customerIdPp, String campusId) {
        this.customerIdPp = customerIdPp;
        this.campusId = campusId;
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
	 * returns the account# of the customer in powerpark
	 * @return int
	 */
    public int getCustomerIdPp() {
        return this.customerIdPp;
    }

    /**
	 * sets the account# of the customer in powerpark
	 * @param customerIdPp int
	 */
    public void setCustomerIdPp(int customerIdPp) {
        this.customerIdPp = customerIdPp;
    }

    /**
	 *
	 * @return String
	 */
    public String getCampusId() {
        return campusId;
    }

    /**
	 *
	 * @param campusId String
	 */
    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }
}
