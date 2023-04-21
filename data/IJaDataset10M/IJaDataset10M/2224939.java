package org.jumpmind.pos.flow;

import java.util.Map;

public interface IFlowManager {

    public void execute(String flowId, String action, Map<String, Object> params) throws FlowNotFoundException;
}
