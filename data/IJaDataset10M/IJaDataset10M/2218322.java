package org.openrtk.idl.epp0503.contact;

/**
 * Master external interface for the implementor of the EPP Contact Info command.</p>
 * The interface brings together epp_ContactInfoOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/contact/epp_ContactInfo.java,v 1.2 2003/09/10 21:29:56 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:56 $<br>
 * @see com.tucows.oxrs.epp0503.rtk.xml.EPPContactInfo
 */
public interface epp_ContactInfo extends epp_ContactInfoOperations, org.openrtk.idl.epp0503.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
