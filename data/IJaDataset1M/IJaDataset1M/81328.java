package com.modelmetrics.cloudconverter.describe.struts2;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import com.modelmetrics.cloudconverter.util.InterestingSobjectFilter;
import com.modelmetrics.cloudconverter.util.SelectObjectFilterProvider;
import com.sforce.soap.partner.DescribeGlobalResult;

public class SelectObjectAction extends AbstractDescribeContextAware {

    private static final Log log = LogFactory.getLog(SelectObjectAction.class);

    private String target;

    private String filter;

    private String existingLocationUrl;

    private String existingSessionId;

    public String execute() throws Exception {
        if (this.getSalesforceSessionContext().getSalesforceSession() == null && this.getExistingSessionId() != null && this.getExistingLocationUrl() != null) {
            this.getSalesforceSessionContext().setSalesforceExistingSession(this.getExistingSessionId(), this.getExistingLocationUrl());
        } else {
        }
        if (this.getTarget() == null) {
            DescribeGlobalResult describeGlobalResult = this.getSalesforceSessionContext().getSalesforceSession().getSalesforceService().describeGlobal();
            if (StringUtils.hasText(this.getFilter())) {
                Collection<String> types = new ArrayList<String>();
                for (String type : describeGlobalResult.getTypes()) {
                    if (this.getFilter().equalsIgnoreCase("all")) {
                        types.add(type);
                    } else if (type.startsWith(this.getFilter())) {
                        types.add(type);
                    }
                }
                this.getDescribeContext().setTypes(types);
            } else {
                this.getDescribeContext().setTypes(new InterestingSobjectFilter().getInterestSobjects(describeGlobalResult));
            }
            this.getDescribeContext().setLastResult(null);
            return INPUT;
        }
        this.getDescribeContext().setTarget(this.getTarget());
        return SUCCESS;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String targetSObject) {
        this.target = targetSObject;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getExistingLocationUrl() {
        return existingLocationUrl;
    }

    public String getExistingSessionId() {
        return existingSessionId;
    }

    public String getS() {
        return this.getExistingSessionId();
    }

    public String getU() {
        return this.getExistingLocationUrl();
    }

    public void setExistingLocationUrl(String username) {
        this.existingLocationUrl = username;
    }

    public void setExistingSessionId(String password) {
        this.existingSessionId = password;
    }

    public void setS(String s) {
        this.setExistingSessionId(s);
    }

    public void setU(String u) {
        this.setExistingLocationUrl(u);
    }

    public Collection<String> getFilterPrompts() {
        return SelectObjectFilterProvider.getFilters();
    }
}
