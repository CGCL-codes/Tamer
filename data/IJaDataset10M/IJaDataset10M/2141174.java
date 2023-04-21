package org.openrtk.idl.epp0402.contact;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_ContactCreate is implemented by EPPContactCreate.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/contact/epp_ContactCreateOperations.java,v 1.1 2003/03/21 16:35:40 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:35:40 $<br>
 * @see org.openrtk.idl.epp0402.contact.epp_ContactCreate
 */
public interface epp_ContactCreateOperations extends org.openrtk.idl.epp0402.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Contact Create EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Contact Create EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epp0402.contact.epp_ContactCreateReq parms);

    /**
   * Accessor for the data representing EPP response from a server for the
   * contact create command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_ContactCreateRsp.
   * @returns The Contact Create response
   */
    org.openrtk.idl.epp0402.contact.epp_ContactCreateRsp getResponseData();
}
