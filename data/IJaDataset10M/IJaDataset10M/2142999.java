package org.j2eebuilder.util.ejb;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import java.util.Collection;
import org.j2eebuilder.model.ManagedTransientObject;
import org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)UtilityEJB.java	1.350 01/12/03
 * Stateless session bean. Simple factory for generating unique IDs
 *
 * @version 1.350, 01/12/03
 * @see     org.j2eebuilder.view.ValueObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
 */
public class UtilityEJB implements org.j2eebuilder.util.ejb.Utility {

    private static transient LogManager log = new LogManager(UtilityEJB.class);

    /**
	* From Hunter's book on Servlet Programming
    * String uid = new java.rmi.server.UID.toString();
    * guaranteed unique return java.net.URLEncoder.encode(uid);
    * encode any special chars.
    * Preferably, you should overwrite this method with your own
    * implementation of unique ID. To access database, either you
    * could change this bean to an EJB or use ConnectionFactory
    * class to directly access the database.
    */
    public Integer getUniqueID() {
        return new Integer((new java.rmi.server.UID()).hashCode());
    }
}
