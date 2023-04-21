package au.gov.nla.aons.mvc.actions.logging;

import java.io.Serializable;

public class LoggingSearchParameters implements Serializable {

    private static final long serialVersionUID = 4127193132987047562L;

    private String componentName = "";

    private String searchString = "";

    private String logLevelText = "";

    private String afterDate = "";

    private String beforeDate = "";

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getLogLevelText() {
        return logLevelText;
    }

    public void setLogLevelText(String logLevelText) {
        this.logLevelText = logLevelText;
    }

    public String getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(String afterDate) {
        this.afterDate = afterDate;
    }

    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }
}
