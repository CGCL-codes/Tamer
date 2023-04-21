package com.xinex.siteminder.struts;

import java.util.HashMap;
import org.apache.struts.action.ActionForm;
import com.netegrity.sdk.policyapi.SmPolicy;

public class PolicyIPRestrictionListForm extends ActionForm {

    private String domainName;

    private SmPolicy smPolicy;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public SmPolicy getSmPolicy() {
        return smPolicy;
    }

    public void setSmPolicy(SmPolicy smPolicy) {
        this.smPolicy = smPolicy;
    }
}
