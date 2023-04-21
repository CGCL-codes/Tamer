package com.rbnb.admin;

public class ShortcutData {

    /**
     * 
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 02/12/2002
     */
    public String destinationAddress;

    /**
     * 
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 02/12/2002
     */
    public String name;

    /**
     * 
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 02/12/2002
     */
    public double cost;

    /**
     * 
     * <p>
     *
     * @author John P. Wilson
     *
     * @since V2.0
     * @version 02/12/2002
     */
    public static final double MAX_COST_NOT_INCLUSIVE = Double.MAX_VALUE / 1000000;

    public ShortcutData(String destinationAddressI, String nameI, double costI) {
        destinationAddress = destinationAddressI;
        name = nameI;
        cost = costI;
    }

    public ShortcutData() {
        destinationAddress = "localhost:3333";
        name = "Shortcut";
        cost = 1.0;
    }

    public String toString() {
        return (new String("Shortcut data: destination server address = " + destinationAddress + ", shortcut name = " + name + ", cost = " + cost));
    }
}
