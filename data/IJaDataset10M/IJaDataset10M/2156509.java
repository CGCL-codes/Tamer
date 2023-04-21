package org.openrtk.idl.epprtk.domain;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_DomainCreate is implemented by EPPDomainCreate.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epprtk/domain/epp_DomainCreateOperations.java,v 1.1 2004/12/07 15:27:50 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 15:27:50 $<br>
 * @see org.openrtk.idl.epprtk.domain.epp_DomainCreate
 */
public interface epp_DomainCreateOperations extends org.openrtk.idl.epprtk.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Domain Create EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Domain Create EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epprtk.domain.epp_DomainCreateReq parms);

    /**
   * Accessor for the data representing EPP response from a server for the
   * domain create command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_DomainCreateRsp.
   * @returns The Domain Create response
   */
    org.openrtk.idl.epprtk.domain.epp_DomainCreateRsp getResponseData();
}
