package org.openrtk.idl.epp0604.contact;

/**
 * Master external interface for the implementor of the EPP Contact Transfer command.</p>
 * The interface brings together epp_ContactTransferOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/contact/epp_ContactTransfer.java,v 1.2 2003/09/10 21:29:57 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:57 $<br>
 * @see com.tucows.oxrs.epp0604.rtk.xml.EPPContactTransfer
 */
public interface epp_ContactTransfer extends epp_ContactTransferOperations, org.openrtk.idl.epp0604.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
