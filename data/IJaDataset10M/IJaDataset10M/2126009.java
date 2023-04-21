package com.brekeke.report.history.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public interface AgentHistoryService {

    public abstract InputStream agentReportByAgent(long from, long to, String timeFrom, String timeTo, HashMap<String, String> parameter, String reportType, Locale locale) throws Exception;

    public abstract InputStream agentReportByGroup(long from, long to, String timeFrom, String timeTo, HashMap<String, String> parameter, String reportType, Locale locale) throws Exception;

    public abstract Map<Integer, Map<String, InputStream>> agentReportAll(long from, long to, String timeFrom, String timeTo, HashMap<Integer, HashMap<String, String>> parameterMap, Locale locale) throws Exception;
}
