package org.openrtk.idl.epp0604.host;

/**
 * Master external interface for the implementor of the EPP Host Info command.</p>
 * The interface brings together epp_HostInfoOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/host/epp_HostInfo.java,v 1.2 2003/09/10 21:29:58 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:58 $<br>
 * @see com.tucows.oxrs.epp0604.rtk.xml.EPPHostInfo
 */
public interface epp_HostInfo extends epp_HostInfoOperations, org.openrtk.idl.epp0604.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
