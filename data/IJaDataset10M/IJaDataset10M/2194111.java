package com.valtech.bootcamp.carRental.web.customer;

import com.valtech.bootcamp.carRental.service.reservationAgent.ReservationAgentHome;
import com.valtech.bootcamp.carRental.service.reservationAgent.ReservationAgent;
import com.valtech.bootcamp.carRental.business.customer.*;
import javax.naming.*;
import javax.rmi.*;
import javax.ejb.*;

/**
 * Client side bean for the Customer<br><br>
 * @author Valtech UK
 */
public class CustomerJB {

    /**
 * the customer name
 */
    private String name = "";

    /**
 * the customer address
 */
    private String address = "";

    /**
 * the card number of the customer
 */
    private String cardNum = "";

    /**
 * the state holder object
 */
    private CustomerStateHolder csh;

    /**
 * @param name the name of the customer
 */
    public CustomerJB(String name) {
        this.name = name;
    }

    public CustomerJB() {
    }

    /**
 * Full arguments constructor.
 * @param name the name of the customer
 * @param address the address of the customer
 * @param cardNum the card number of the customer
 */
    public CustomerJB(String name, String address, String cardNum) {
        this.name = name;
        this.address = address;
        this.cardNum = cardNum;
    }

    /**
 * gets the name of the customer
 */
    public String getName() {
        return name;
    }

    /**
  * sets the customer name
  */
    public void setName(String name) {
        this.name = name;
    }

    /**
 * gets the sddress of the customer
 */
    public String getAddress() {
        return address;
    }

    /**
 * sets the sddress of the customer
 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
 * gets the card number
 */
    public String getCardNum() {
        return cardNum;
    }

    /**
 * sets the card number
 */
    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    /**
  * calls getCustomer() on the ReservationAgent facade and extracts this state holder into the been attribute fields.
  */
    public CustomerStateHolder getCustomer() {
        CustomerStateHolder csh = null;
        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        try {
            Context initial = new InitialContext();
            ReservationAgentHome resAgentHome = (ReservationAgentHome) PortableRemoteObject.narrow(initial.lookup("ReservationAgentBean"), ReservationAgentHome.class);
            ReservationAgent reservationAgent = resAgentHome.create();
            csh = reservationAgent.getCustomer(this.name);
        } catch (Exception e) {
            System.out.print("Test Error");
        }
        if (csh != null) {
            this.address = csh.getAddress();
            this.cardNum = csh.getCardNo();
            this.name = csh.getName();
        }
        return csh;
    }

    /**
 *  calls setCustomer() on the ReservationAgent facade.
 */
    public void setCustomer() {
        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        try {
            Context initial = new InitialContext();
            ReservationAgentHome resAgentHome = (ReservationAgentHome) PortableRemoteObject.narrow(initial.lookup("ReservationAgentBean"), ReservationAgentHome.class);
            ReservationAgent reservationAgent = resAgentHome.create();
            reservationAgent.setCustomer(this.createCustomerStateHolder());
        } catch (Exception e) {
            System.out.print("Test Error");
            e.printStackTrace();
        }
    }

    public CustomerStateHolder createCustomerStateHolder() {
        return new CustomerStateHolder(this.name, this.address, this.cardNum);
    }

    public void removeCustomer(CustomerStateHolder csh) {
        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "localhost:1099");
        try {
            Context initialb = new InitialContext();
            ReservationAgentHome raHome = (ReservationAgentHome) PortableRemoteObject.narrow(initialb.lookup("ReservationAgentBean"), ReservationAgentHome.class);
            ReservationAgent ra = raHome.create();
            ra.removeCustomer(csh.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toXML() {
        return new String("<?xml version=\"1.0\"?>" + "<?cocoon-process type=\"xslt\"?>" + "<?xml-stylesheet href=\"/stylesheet/form-html.xsl\" type=\"text/xsl\"?>" + "<?xml-stylesheet href=\"/stylesheet/form-wml.xsl\" type=\"text/xsl\" media=\"wap\"?>" + "<form action=\"reserve.jsp\">" + "<head>" + "<title>ABC Car Rental</title>" + "</head>" + "<input type=\"text\" name=\"name\" value=\"" + this.getName() + "\" size=\"11\"/>" + "<input type=\"text\" name=\"address\" value=\"" + this.getAddress() + "\" size=\"11\"/>" + "<input type=\"text\" name=\"cardNum\" value=\"" + this.getCardNum() + "\" size=\"11\"/>" + "</form>");
    }
}
