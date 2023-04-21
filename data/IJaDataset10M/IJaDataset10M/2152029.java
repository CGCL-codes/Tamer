package br.com.linkcom.neo.view.ajax;

public class AutocompleteFilter {

    Class<?> type;

    String q;

    String beanName;

    String functionLoad;

    String propertyMatch;

    String propertyLabel;

    String getterLabel;

    Boolean matchOption;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getFunctionLoad() {
        return functionLoad;
    }

    public void setFunctionLoad(String functionLoad) {
        this.functionLoad = functionLoad;
    }

    public String getPropertyMatch() {
        return propertyMatch;
    }

    public String getPropertyLabel() {
        return propertyLabel;
    }

    public void setPropertyMatch(String propertyMatch) {
        this.propertyMatch = propertyMatch;
    }

    public void setPropertyLabel(String propertyLabel) {
        this.propertyLabel = propertyLabel;
    }

    public String getGetterLabel() {
        return getterLabel;
    }

    public void setGetterLabel(String getterLabel) {
        this.getterLabel = getterLabel;
    }

    public Boolean getMatchOption() {
        return matchOption;
    }

    public void setMatchOption(Boolean matchOption) {
        this.matchOption = matchOption;
    }
}
