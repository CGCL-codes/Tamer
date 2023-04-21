package org.dmp.chillout.auxiliary.drmtool;

import javax.media.Codec;
import org.dmp.chillout.auxiliary.drmprocessor.DRMProcessor;

public interface ToolInterface extends Codec {

    String toolID = null;

    public int initialize(DRMProcessor dp, int[] cpidList);

    public int mutualAuthenticate(DRMProcessor sender, boolean requestNegotiation, boolean successNegotiation, int nCandidateAlgorithm, String cadidateAlgorithms);

    public String receiveMessage(String Message);

    public void setKey(String keyData);

    public String getToolID();

    public void setToolID(String toolID);
}
