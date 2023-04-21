package org.jecars.client.nt;

import org.jecars.client.JC_DefaultNode;
import org.jecars.client.JC_Exception;

/**
 *
 * @author weert
 */
public class JC_WorkflowTaskPortNode extends JC_DefaultNode {

    public long getSequenceNumber() throws JC_Exception {
        return getProperty("jecars:sequencenumber").getValueAsLong();
    }
}
