package org.rich.charlesmurphy.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.ddevil.data.BeanData;
import org.rich.charlesmurphy.core.CharliesUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Represents an HTTP request.
 * @author Rich O'Connell
 */
public class Request extends BeanData {

    private static final long serialVersionUID = -3524552441633196117L;

    private String url;

    private String path;

    private String query;

    private String method;

    private String status;

    private String remoteHost;

    private Date startTime;

    private Long requestHeaderSize;

    private Long requestBodySize;

    private Long responseHeaderSize;

    private Long responseBodySize;

    private Long elapsedTime;

    private ResourceType resourceType;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        return remoteHost + " - " + startTime + "[" + method + ":" + url + "]";
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void configureWrapper(BeanWrapper wrapper) {
        wrapper.registerCustomEditor(Date.class, "startTime", new CustomDateEditor(new SimpleDateFormat("dd MMM yyyy HH:mm:ss"), true));
    }

    /**
	 * @param url the url to set
	 */
    public void setUrl(String url) {
        this.url = url;
        String path = url.replaceAll("https?://\\w+(:[\\d]{2,4})?/", "");
        int idx = findQueryIndex(path);
        if (idx == -1) {
            this.path = path;
            this.query = "";
        } else {
            this.path = path.substring(0, idx);
            this.query = idx < path.length() ? path.substring(idx + 1) : "";
        }
        this.resourceType = CharliesUtils.determineResourceType(path);
    }

    private static int findQueryIndex(String in) {
        if (in == null) return -1;
        int idx = in.indexOf("?");
        if (idx > -1) {
            return idx;
        }
        idx = in.indexOf(";");
        return idx;
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @return the path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * @return the method
	 */
    public String getMethod() {
        return method;
    }

    /**
	 * @param method the method to set
	 */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
	 * @return the status
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * @return the elapsedTime
	 */
    public Long getElapsedTime() {
        return elapsedTime;
    }

    /**
	 * @param elapsedTime the elapsedTime to set
	 */
    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
	 * @return the query
	 */
    public String getQuery() {
        return query;
    }

    /**
	 * @return the startTime
	 */
    public Date getStartTime() {
        return startTime;
    }

    /**
	 * @param startTime the startTime to set
	 */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
	 * @return the requestHeaderSize
	 */
    public Long getRequestHeaderSize() {
        return requestHeaderSize;
    }

    /**
	 * @param requestHeaderSize the requestHeaderSize to set
	 */
    public void setRequestHeaderSize(Long requestHeaderSize) {
        this.requestHeaderSize = requestHeaderSize;
    }

    /**
	 * @return the requestBodySize
	 */
    public Long getRequestBodySize() {
        return requestBodySize;
    }

    /**
	 * @param requestBodySize the requestBodySize to set
	 */
    public void setRequestBodySize(Long requestBodySize) {
        this.requestBodySize = requestBodySize;
    }

    /**
	 * @return the responseHeaderSize
	 */
    public Long getResponseHeaderSize() {
        return responseHeaderSize;
    }

    /**
	 * @param responseHeaderSize the responseHeaderSize to set
	 */
    public void setResponseHeaderSize(Long responseHeaderSize) {
        this.responseHeaderSize = responseHeaderSize;
    }

    /**
	 * @return the responseBodySize
	 */
    public Long getResponseBodySize() {
        return responseBodySize;
    }

    /**
	 * @param responseBodySize the responseBodySize to set
	 */
    public void setResponseBodySize(Long responseBodySize) {
        this.responseBodySize = responseBodySize;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
