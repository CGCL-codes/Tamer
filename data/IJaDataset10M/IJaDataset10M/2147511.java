package org.parosproxy.paros.extension.option;

import org.parosproxy.paros.common.AbstractParam;

public class OptionsHttpHeader extends AbstractParam {

    private static final String CUSTOM_HTTP_HEADER_USER_AGENT = "filter.httpHeader.customUserAgent";

    private String customUserAgent = null;

    public OptionsHttpHeader() {
    }

    protected void parse() {
        customUserAgent = getConfig().getString(CUSTOM_HTTP_HEADER_USER_AGENT, "");
    }

    /**
	 * @return Returns the User-Agent
	 */
    public String getCustomUserAgent() {
        return customUserAgent;
    }

    /**
	 * @param customUserAgent
	 */
    public void setCustomUserAgent(String customUserAgent) {
        this.customUserAgent = customUserAgent;
        getConfig().setProperty(CUSTOM_HTTP_HEADER_USER_AGENT, customUserAgent);
    }

    public boolean isCustomUserAgent() {
        return !(customUserAgent != "");
    }
}
