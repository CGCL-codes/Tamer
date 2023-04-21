package org.tanso.fountain.admin.node;

import org.tanso.fountain.interfaces.func.comm.IMessage;
import org.tanso.fountain.util.net.SigBase;

/**
 * This is the signal to control the interaction between the node admin
 * entities.
 * 
 * @author Haiping Huang
 * 
 */
public class SigNodeInfo extends SigBase implements IMessage {

    private static final long serialVersionUID = 8867206302990282681L;

    /** content: [tid] */
    public static final int ACT_PING = 0;

    /** content: [tid][SEP]ACK */
    public static final int ACT_PING_ACK = 1;

    /** content: [tid] */
    public static final int ACT_GET_CPU = 2;

    /** content: [tid][SEP]cpuusage */
    public static final int ACT_GET_CPU_ACK = 3;

    /** content: [tid] */
    public static final int ACT_GET_FREEMEM = 4;

    /** content: [tid][SEP]freemem */
    public static final int ACT_GET_FREEMEN_ACK = 5;

    /** content: [tid] */
    public static final int ACT_GET_TOTALMEM = 6;

    /** content: [tid][SEP]totalmem */
    public static final int ACT_GET_TOTALMEM_ACK = 7;

    public static final String SIG_SEPARATOR = "#";

    /**
	 * 
	 */
    public SigNodeInfo() {
        super();
    }

    public SigNodeInfo(int sig, String sessionId, String contactTopic, String content) {
        super(sig, sessionId, contactTopic, content);
    }
}
