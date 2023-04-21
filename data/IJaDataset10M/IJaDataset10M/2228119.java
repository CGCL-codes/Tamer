package com.hisham.creditcard;

import com.hisham.transaction.*;
import java.io.*;

/**
 *
 * <p>Title: Web Services for Parking</p>
 *
 * <p>Description: Contains information about the item i.e Item ID,
 * Item Description, unit Price of Item, Item Quantity and if Item taxable or
 * not.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p> </p>
 *
 * @author Ali Hisham Malik
 * @version 2.0
 */
public class CcTransactionItemInfo extends TransactionItemInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8042550214938075524L;

    /**
	 * Default Constructor.
	 */
    public CcTransactionItemInfo() {
        itemDescription = new String();
        itemPrice = 0;
        itemQuantity = 0;
        isItemTaxable = false;
    }

    protected String itemDescription;

    /**
	 *
	 * @return String
	 */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
	 *
	 * @param tmpDescription String
	 */
    public void setItemDescription(String tmpDescription) {
        itemDescription = tmpDescription;
    }

    /**
	 *
	 * @return double
	 */
    public double getTotalPrice() {
        return itemPrice * itemQuantity;
    }

    protected int itemQuantity;

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int tmpQuantity) {
        itemQuantity = tmpQuantity;
    }

    protected boolean isItemTaxable;

    public boolean getIsItemTaxable() {
        return isItemTaxable;
    }

    public void setIsItemTaxable(boolean flag) {
        isItemTaxable = flag;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}
