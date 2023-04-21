package com.meterware.httpunit;

import org.w3c.dom.Node;
import com.kenstevens.stratdom.site.SiteForm;
import com.kenstevens.stratdom.site.SiteResponse;
import com.kenstevens.stratdom.site.StratSite;

public class MockSiteForm implements SiteForm {

    private String value;

    public MockSiteForm(Node node, String value) {
        this.value = value;
    }

    public MockSiteForm() {
    }

    public String getParameterValue(String key) {
        return value;
    }

    @Override
    public String[] getOptionValues(String name) {
        return null;
    }

    @Override
    public String[] getOptions(String name) {
        return null;
    }

    @Override
    public void setCheckbox(String name, boolean value) {
    }

    @Override
    public void setParameter(String name, String value) {
    }

    @Override
    public SiteResponse submit(StratSite stratSite) throws Exception {
        return null;
    }
}
