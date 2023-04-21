package org.openrtk.idl.epp0705.host;

/**
 * Internal IDL interface which is never referenced externally.</p>
 * Sub-interface epp_HostUpdate is implemented by EPPHostUpdate.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/host/epp_HostUpdateOperations.java,v 1.1 2003/03/20 22:42:31 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:31 $<br>
 * @see org.openrtk.idl.epp0705.host.epp_HostUpdate
 */
public interface epp_HostUpdateOperations extends org.openrtk.idl.epp0705.epp_ActionOperations {

    /**
   * Sets the request data for an outgoing Host Update EPP request.
   * The implementor of this method is responsible for translating
   * the request parms into equivalent Host Update EPP XML.
   * @param parms The EPP request data
   */
    void setRequestData(org.openrtk.idl.epp0705.host.epp_HostUpdateReq parms);

    /**
   * Accessor for the data representing EPP response from a server for the
   * host update command.
   * The implementor of this method is responsible for translating
   * the response EPP XML into an instance of epp_HostUpdateRsp.
   * @returns The Host Update response
   */
    org.openrtk.idl.epp0705.host.epp_HostUpdateRsp getResponseData();
}
