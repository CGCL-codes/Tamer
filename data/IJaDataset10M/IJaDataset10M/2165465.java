package org.matsim.utils.vis.otfvis.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public interface OTFAgentHandler<SrcAgent> extends Serializable {

    public void writeAgent(SrcAgent agent, DataOutputStream out) throws IOException;

    public void readAgent(DataInputStream in) throws IOException;
}
