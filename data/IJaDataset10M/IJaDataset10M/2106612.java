package com.ohioedge.j2ee.api.org.prod.ejb;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

/**
 * @(#)ProductTypeHome.java	1.3.1 10/15/2002
 * @author Sandeep Dixit
 * @version 1.3.1 * @since Ohioedge Component API 1.2
 */
public interface ProductTypeHome extends EJBLocalHome {

    public ProductType create(Integer id, String name, String description, Integer createdBy, Integer orgID, java.sql.Timestamp createdOn) throws CreateException;

    public ProductType findByPrimaryKey(ProductTypePK id) throws FinderException;

    public Collection findByOrganizationID(Integer orgID) throws FinderException;

    public Collection findByName(Integer orgID, String name) throws FinderException;

    public Collection findByDescription(Integer orgID, String description) throws FinderException;
}
