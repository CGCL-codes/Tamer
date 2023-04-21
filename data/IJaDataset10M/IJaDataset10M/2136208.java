package org.openrtk.idl.epp0402;

/**
 * Master external interface for the implementor of the EPP Logout command.</p>
 * The interface brings together epp_LogoutOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/epp_Logout.java,v 1.2 2003/09/10 21:29:55 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:55 $<br>
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPLogout
 */
public interface epp_Logout extends epp_LogoutOperations, org.openrtk.idl.epp0402.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
