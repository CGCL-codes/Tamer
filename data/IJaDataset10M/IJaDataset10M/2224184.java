package org.middleheaven.report;

import java.util.Map;

public interface ReportContext {

    public String getReportName();

    public <P> P getParameter(String name, Class<P> type);

    public void setParameter(String name, Object value);

    public Map<String, Object> getParameters();
}
