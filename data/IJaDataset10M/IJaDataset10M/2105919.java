package com.volantis.vdp.sps.connector;

import com.volantis.vdp.sps.response.ISCPWriter;

/**
 * Interface define api for connector class
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:37:07
 */
public interface IConnector extends Runnable {

    /**
     * @return byte[] - response from destination server
     */
    public byte[] getResponse();

    /**
     * @return int requestId
     */
    public int getRequestId();

    /**
     * method set writer
     */
    public void setWriter(ISCPWriter writer);

    /**
     * return true if connector is connected and false if not
     * connected
     */
    public boolean isConnected();
}
