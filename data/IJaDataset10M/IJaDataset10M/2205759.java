package org.frameworkset.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import org.frameworkset.web.servlet.HandlerAdapter;

/**
 * <p>Title: LastModified.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface LastModified {

    /**
	 * Same contract as for HttpServlet's <code>getLastModified</code> method.
	 * Invoked <b>before</b> request processing.
	 * <p>The return value will be sent to the HTTP client as Last-Modified header,
	 * and compared with If-Modified-Since headers that the client sends back.
	 * The content will only get regenerated if there has been a modification.
	 * @param request current HTTP request
	 * @return the time the underlying resource was last modified, or -1
	 * meaning that the content must always be regenerated
	 * @see HandlerAdapter#getLastModified
	 * @see javax.servlet.http.HttpServlet#getLastModified
	 */
    long getLastModified(HttpServletRequest request);
}
