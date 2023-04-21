package org.openrtk.idl.epprtk.domain;

/**
 * Master external interface for the implementor of the EPP Domain Update command.</p>
 * The interface brings together epp_DomainUpdateOperations, epp_Action and standard
 * IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epprtk/domain/epp_DomainUpdate.java,v 1.1 2004/12/07 15:27:50 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 15:27:50 $<br>
 * @see com.tucows.oxrs.epprtk.rtk.xml.EPPDomainUpdate
 */
public interface epp_DomainUpdate extends epp_DomainUpdateOperations, org.openrtk.idl.epprtk.epp_Action, org.omg.CORBA.portable.IDLEntity {
}
