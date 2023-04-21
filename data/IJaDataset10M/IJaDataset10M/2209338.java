package com.textflex.spreadshift;

import java.util.ArrayList;

/**
 * Detailed headers storage class for accessing and printing header information.
 * Objects of this class usually contain all the headers for a given worksheet
 * or a given subset of headers, such as the selected headers from the worksheet.
 * @author David
 */
public class Headers {

    private ArrayList<String> list = new ArrayList<String>();

    private String name = "";

    /**
	 * Creates an empty headers storage class.
	 */
    public Headers() {
    }

    /**
	 * Gets the list of headers
	 * @return headers list
	 */
    public ArrayList<String> getList() {
        return list;
    }

    /**
	 * Gets the name of the headers, such as the group headers name.
	 * @return headers overall name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the list of headers
	 * @param aList a list of headers
	 */
    public void setList(ArrayList<String> aList) {
        list = aList;
    }

    /**
	 * Sets the overall name of the headers.
	 * @param aName headers overall name
	 */
    public void setName(String aName) {
        name = aName;
    }
}
