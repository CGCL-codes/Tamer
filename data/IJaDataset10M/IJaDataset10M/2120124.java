package org.kablink.teaming.gwt.client.rpc.shared;

import java.util.List;
import org.kablink.teaming.gwt.client.util.AssignmentInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class holds the response data for RPC commands that return a
 * List<AssignmentInfo>.
 * 
 * @author drfoster@novell.com
 */
public class AssignmentInfoListRpcResponseData implements IsSerializable, VibeRpcResponseData {

    private List<AssignmentInfo> m_assignmentInfoList;

    /**
	 * Class constructor.
	 * 
	 * For GWT serialization, must have a zero parameter
	 * constructor.
	 */
    public AssignmentInfoListRpcResponseData() {
        super();
    }

    /**
	 * Class constructor.
	 * 
	 * @param assignmentInfoList
	 */
    public AssignmentInfoListRpcResponseData(List<AssignmentInfo> assignmentInfoList) {
        m_assignmentInfoList = assignmentInfoList;
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public List<AssignmentInfo> getAssignmentInfoList() {
        return m_assignmentInfoList;
    }
}
