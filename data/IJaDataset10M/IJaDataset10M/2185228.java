package com.ohioedge.j2ee.api.org.order.ejb;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.rmi.*;
import javax.ejb.*;

/**
*
* @author Sandeep Dixit
* @version 1.3.1
* @since Ohioedge Component API 1.2
*/
public interface CustomerRequirementVendorHome extends EJBLocalHome {

    public CustomerRequirementVendor create(Integer customerRequirementVendorID, Integer vendorID, Integer customerRequirementID, Integer employeeID, String name, String description, Integer customerRequirementVendorStatusID, java.sql.Timestamp customerRequirementVendorStatusDate, Integer createdBy, java.sql.Timestamp createdOn) throws CreateException;

    public CustomerRequirementVendor findByPrimaryKey(CustomerRequirementVendorPK primaryKey) throws FinderException;

    public Collection findByCustomerID(Integer customerID) throws FinderException;

    public Collection findByCustomerRequirementID(Integer customerRequirementID) throws FinderException;
}
