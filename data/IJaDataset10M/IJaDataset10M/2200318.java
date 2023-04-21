package org.dcm4che.net;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
public interface ExtNegotiation {

    public String getSOPClassUID();

    public byte[] info();
}
