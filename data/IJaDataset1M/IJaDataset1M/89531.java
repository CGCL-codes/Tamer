package de.ios.kontor.sv.address.co;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * CountryController deals with a set of Countrys
 * within the kontor framework.
 */
public interface CountryController extends BasicController {

    /**
   * Create a new Country
   *
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the creation of Country failed.
   */
    public Country createCountry() throws java.rmi.RemoteException, KontorException;

    /**
   * Create a new Country with full description
   *
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the creation of Country failed.
   */
    public Country createCountry(String _name, String _shortName, String _phonePrefix) throws java.rmi.RemoteException, KontorException;

    /**
   * Get the Country with the matching oid
   *
   * @param oid - Object-ID of the Country searched
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of Country failed.
   */
    public Country getCountryByOId(long oid) throws java.rmi.RemoteException, KontorException;

    /**
   * Find Countrys by a filled Country-template
   *
   * @param o - Country with these attributes set which should be searched for.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of Countrys failed.
   */
    public Iterator getCountrys(Country comp) throws java.rmi.RemoteException, KontorException;

    /**
   * Get the default country
   * @return the name of the default country
   */
    public String getDefaultCountryName() throws java.rmi.RemoteException, KontorException;

    /**
   * Load all countries as DCs.
   * 
   * @exception de.ios.kontor.utils.KontorException if the loading of states failed.
   */
    public Iterator getCountryDC() throws java.rmi.RemoteException, KontorException;
}

;
