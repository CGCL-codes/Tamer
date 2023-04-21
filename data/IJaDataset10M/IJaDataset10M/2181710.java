package org.openrtk.idl.epp0503.contact;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_ContactCheck is implemented by EPPContactCheck.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/contact/epp_ContactCheckOperations.java,v 1.1 2003/03/21 16:18:27 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:18:27 $<br>
 * @see org.openrtk.idl.epp0503.contact.epp_ContactCheck
 */
public interface epp_ContactCheckOperations extends org.openrtk.idl.epp0503.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Contact Check EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Contact Check EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epp0503.contact.epp_ContactCheckReq parms);

    /**
   * Accessor for the data representing EPP response from a server for the
   * contact check command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_ContactCheckRsp.
   * @returns The Contact Check response
   */
    org.openrtk.idl.epp0503.contact.epp_ContactCheckRsp getResponseData();
}
