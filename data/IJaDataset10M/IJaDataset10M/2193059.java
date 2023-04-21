package org.openrtk.idl.epp0705.domain;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_DomainCreate is implemented by EPPDomainCreate.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/domain/epp_DomainCreateOperations.java,v 1.1 2003/03/20 22:42:29 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:29 $<br>
 * @see org.openrtk.idl.epp0705.domain.epp_DomainCreate
 */
public interface epp_DomainCreateOperations extends org.openrtk.idl.epp0705.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Domain Create EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Domain Create EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epp0705.domain.epp_DomainCreateReq parms);

    /**
   * Accessor for the data representing EPP response from a server for the
   * domain create command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_DomainCreateRsp.
   * @returns The Domain Create response
   */
    org.openrtk.idl.epp0705.domain.epp_DomainCreateRsp getResponseData();
}
