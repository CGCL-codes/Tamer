package com.liferay.portal.sharepoint.methods;

import com.liferay.portal.sharepoint.SharepointException;
import com.liferay.portal.sharepoint.SharepointRequest;

/**
 * <a href="Method.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public interface Method {

    public static final String GET = "GET";

    public static final String POST = "POST";

    public String getMethodName();

    public String getRootPath(SharepointRequest sharepointRequest);

    public void process(SharepointRequest sharepointRequest) throws SharepointException;
}
