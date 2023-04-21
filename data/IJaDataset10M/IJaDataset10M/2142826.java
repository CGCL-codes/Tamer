package org.spirit.bean.impl;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListUserVisitLog implements Serializable {

    private String requestPage;

    private String host;

    private Calendar createdOn;

    private String referer;

    private String userAgent;

    private String remoteHost;

    private Long id;

    private String requestUri;

    /**
		 * @return
		 */
    public Calendar getCreatedOn() {
        if (createdOn == null) {
            createdOn = Calendar.getInstance();
        }
        return createdOn;
    }

    /**
		 * @param createdOn the createdOn to set
		 */
    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
		 * @return the host
		 */
    public String getHost() {
        return host;
    }

    /**
		 * @param host the host to set
		 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
		 * @return the id
		 */
    public Long getId() {
        return id;
    }

    /**
		 * @param id the id to set
		 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
		 * @return the referer
		 */
    public String getReferer() {
        return referer;
    }

    /**
		 * @param referer the referer to set
		 */
    public void setReferer(String referer) {
        this.referer = referer;
    }

    /**
		 * @return the remoteHost
		 */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
		 * @param remoteHost the remoteHost to set
		 */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
		 * @return the requestPage
		 */
    public String getRequestPage() {
        return requestPage;
    }

    /**
		 * @param requestPage the requestPage to set
		 */
    public void setRequestPage(String requestPage) {
        this.requestPage = requestPage;
    }

    /**
		 * @return the requestUri
		 */
    public String getRequestUri() {
        return requestUri;
    }

    /**
		 * @param requestUri the requestUri to set
		 */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    /**
		 * @return the userAgent
		 */
    public String getUserAgent() {
        return userAgent;
    }

    /**
		 * @param userAgent the userAgent to set
		 */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
