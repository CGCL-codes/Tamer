package com.c2b2.ipoint.model;

/**
  * This class is the persistent representation of an address
  * <p>
  * $Date: 2006/09/23 21:21:10 $
  * 
  * $Id: Address.java,v 1.2 2006/09/23 21:21:10 steve Exp $<br>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.2 $
  */
public class Address extends PersistentObject {

    private long ID;

    private String line1;

    private String line2;

    private String line3;

    private String town;

    private String region;

    private String country;

    private String postCode;

    /**
   * Default Constructor required by Hibernate
   */
    public Address() {
    }

    /**
   * The Primary Key for the Address
   * @return 
   */
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    /**
   * First line of the address
   * @return 
   */
    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
   * Second line of the address
   * @return 
   */
    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
   * Third line of the address
   * @return 
   */
    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    /**
   * The town
   * @return 
   */
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    /**
   * The region
   * @return 
   */
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
   * The country
   * @return 
   */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
   * The postal code
   * @return 
   */
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
   * Deletes the current Address
   */
    public void delete() throws PersistentModelException {
        super.delete();
    }

    /**
   * Creates a new Address object in the database
   * @param line1 The first line of the address
   * @param line2 The second line of the address
   * @param line3 The third line of the address
   * @param town The town 
   * @param region The region
   * @param country The country
   * @param postCode The post code
   * @return 
   * @throws PersistentModelException 
   */
    public static Address create(String line1, String line2, String line3, String town, String region, String country, String postCode) throws PersistentModelException {
        Address result = new Address();
        result.setLine1(line1);
        result.setLine2(line2);
        result.setLine3(line3);
        result.setTown(town);
        result.setRegion(region);
        result.setCountry(country);
        result.setPostCode(postCode);
        save(result);
        return result;
    }

    /**
   * Finds the Address with the specified ID
   * @param ID 
   * @return The Address
   * @throws PersistentModelException if no Address is found
   */
    public static Address find(long ID) throws PersistentModelException {
        Address result = (Address) get(ID, Address.class);
        return result;
    }

    /**
   * Finds the Address with the specified Primary key
   * @param IDStr 
   * @return 
   * @throws PersistentModelException If the Address is not found
   */
    public static Address find(String IDStr) throws PersistentModelException {
        try {
            long ID = Long.parseLong(IDStr);
            return find(ID);
        } catch (NumberFormatException e) {
            throw new PersistentModelException("Unable to find address with ID " + IDStr);
        }
    }
}
