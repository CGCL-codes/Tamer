package com.inet.qlcbcc.mdb.pub.adapter;

/**
 * AdministrativeProcedureMessagePublisher.
 *
 * @author Dung Nguyen
 * @version $Id: AdministrativeProcedureMessagePublisher.java 2011-08-05 17:21:57z nguyen_dv $
 *
 * @since 1.0
 */
public interface AdministrativeProcedureMessagePublisher {

    /**
   * Sends the request to update the administrative procedure.
   *
   * @param forceUpdate the force update mode to force updating the administrative procedure.
   */
    void sendRequest(boolean forceUpdate);
}
