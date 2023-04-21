package org.dcm4che.net;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
public interface PDU {

    public static final int A_ASSOCIATE_RQ = 1;

    public static final int A_ASSOCIATE_AC = 2;

    public static final int A_ASSOCIATE_RJ = 3;

    public static final int P_DATA_TF = 4;

    public static final int A_RELEASE_RQ = 5;

    public static final int A_RELEASE_RP = 6;

    public static final int A_ABORT = 7;

    public void writeTo(OutputStream out) throws IOException;

    public String toString(boolean verbose);
}
