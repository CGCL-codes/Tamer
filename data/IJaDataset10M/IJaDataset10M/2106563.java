package org.openrtk.idl.epp0503.contact;

/**
 * Master external interface for the implementor of the EPP Contact Create command.</p>
 * The interface brings together epp_ContactCreateOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/contact/epp_ContactCreate.java,v 1.2 2003/09/10 21:29:56 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:56 $<br>
 * @see com.tucows.oxrs.epp0503.rtk.xml.EPPContactCreate
 */
public interface epp_ContactCreate extends epp_ContactCreateOperations, org.openrtk.idl.epp0503.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
