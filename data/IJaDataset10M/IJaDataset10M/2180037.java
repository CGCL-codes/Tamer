package org.openrtk.idl.epp0503.host;

/**
 * Master external interface for the implementor of the EPP Host Delete command.</p>
 * The interface brings together epp_HostDeleteOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/host/epp_HostDelete.java,v 1.2 2003/09/10 21:29:57 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:57 $<br>
 * @see com.tucows.oxrs.epp0503.rtk.xml.EPPHostDelete
 */
public interface epp_HostDelete extends epp_HostDeleteOperations, org.openrtk.idl.epp0503.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
