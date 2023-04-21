package org.openrtk.idl.epp0402.host;

/**
 * Master external interface for the implementor of the EPP Host Update command.</p>
 * The interface brings together epp_HostUpdateOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/host/epp_HostUpdate.java,v 1.2 2003/09/10 21:29:56 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:56 $<br>
 * @see com.tucows.oxrs.epp0402.rtk.xml.EPPHostUpdate
 */
public interface epp_HostUpdate extends epp_HostUpdateOperations, org.openrtk.idl.epp0402.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
