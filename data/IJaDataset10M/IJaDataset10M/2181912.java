package org.openrtk.idl.epp0402;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_Login is implemented by EPPLogin.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/epp_LoginOperations.java,v 1.1 2003/03/21 16:35:39 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:35:39 $<br>
 * @see org.openrtk.idl.epp0402.epp_Login
 */
public interface epp_LoginOperations extends org.openrtk.idl.epp0402.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Login EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Login EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epp0402.epp_LoginReq data);

    /**
   * Accessor for the data representing EPP response from a server for the
   * login command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_LoginRsp.
   * @return The Login response
   */
    org.openrtk.idl.epp0402.epp_LoginRsp getResponseData();
}
