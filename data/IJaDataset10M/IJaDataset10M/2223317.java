package bean;

/**
 * 
 * Product Class for Travel Experts Data Management System.
 * 
 * @author Michael Beckett
 * 
 * Assignment:	Final Project - Java
 * Date:		Mar 4 2011
 * Instructor: 	Harv Peters
 * 
 */
public class Supplier {

    private int suppId;

    private String suppName;

    public int getSuppId() {
        return suppId;
    }

    public void setSuppId(int suppId) {
        this.suppId = suppId;
    }

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    @Override
    public String toString() {
        return String.format("%s", suppName);
    }
}
