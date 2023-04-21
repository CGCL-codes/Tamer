package com.liferay.portal.webdav.methods;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.webdav.WebDAVException;
import com.liferay.portal.webdav.WebDAVRequest;

/**
 * <a href="Method.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface Method {

    public static final String COPY = "COPY";

    public static final String DELETE = "DELETE";

    public static final String GET = "GET";

    public static final String HEAD = "HEAD";

    public static final String LOCK = "LOCK";

    public static final String MKCOL = "MKCOL";

    public static final String MOVE = "MOVE";

    public static final String OPTIONS = "OPTIONS";

    public static final String PROPFIND = "PROPFIND";

    public static final String PROPPATCH = "PROPPATCH";

    public static final String PUT = "PUT";

    public static final String UNLOCK = "UNLOCK";

    public static final String[] SUPPORTED_METHODS_ARRAY = { COPY, DELETE, GET, HEAD, LOCK, MKCOL, MOVE, OPTIONS, PROPFIND, PROPPATCH, PUT, UNLOCK };

    public static final String SUPPORTED_METHODS = StringUtil.merge(SUPPORTED_METHODS_ARRAY);

    public void process(WebDAVRequest webDavReq) throws WebDAVException;
}
