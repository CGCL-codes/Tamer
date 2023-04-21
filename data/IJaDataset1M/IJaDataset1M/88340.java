package com.brekeke.report.realtime.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.brekeke.report.engine.entity.CTIServerGroup;
import com.brekeke.report.engine.entity.Clm;
import com.brekeke.report.engine.entity.MAgentStatus;

public interface AgentReportService {

    public List<CTIServerGroup> searchGroup(Integer tenantId) throws Exception;

    public Map<String, Object> searchAgentReport(Integer tenantId, String groupId, Locale locale) throws Exception;

    public List<Map<String, Object>> searchAgentReportTitle() throws Exception;

    public void initAgentList(Integer tenantId) throws Exception;

    public abstract void sumCallLogMaster(Clm clm) throws Exception;

    public abstract void sumAgentStatus(int tenantId, String agentGroup, String agentId, MAgentStatus mAgentStatus) throws Exception;

    public abstract void cleanBeforeValueMap(Clm clm);
}
