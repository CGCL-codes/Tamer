package org.apache.axis2.jaxws.sample.addressbook;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "AddressBook", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook")
public interface AddressBook {

    /**
     * 
     * @param entry
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(name = "status", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook")
    @RequestWrapper(localName = "addEntry", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook", className = "org.apache.axis2.jaxws.sample.addressbook.AddEntry")
    @ResponseWrapper(localName = "addEntryResponse", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook", className = "org.apache.axis2.jaxws.sample.addressbook.AddEntryResponse")
    public boolean addEntry(@WebParam(name = "entry", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook") AddressBookEntry entry);

    /**
     * 
     * @param firstname
     * @param lastname
     * @return
     *     returns org.apache.axis2.jaxws.sample.addressbook.AddressBookEntry
     */
    @WebMethod
    @WebResult(name = "entry", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook")
    @RequestWrapper(localName = "findEntryByName", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook", className = "org.apache.axis2.jaxws.sample.addressbook.FindEntryByName")
    @ResponseWrapper(localName = "findEntryByNameResponse", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook", className = "org.apache.axis2.jaxws.sample.addressbook.FindEntryByNameResponse")
    public AddressBookEntry findEntryByName(@WebParam(name = "firstname", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook") String firstname, @WebParam(name = "lastname", targetNamespace = "http://org/apache/axis2/jaxws/sample/addressbook") String lastname);
}
