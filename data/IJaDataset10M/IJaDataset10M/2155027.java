package org.impalaframework.web.servlet.invoker;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface useful for wrapping servlets or filters with additional functionality, for example, to set up 
 * read-write locks (see {@link ReadWriteLockingInvoker}) and for setting up the thread context class loader 
 * see {@link ThreadContextClassLoaderHttpServiceInvoker}.
 * 
 * @see ReadWriteLockingInvoker
 * @see ThreadContextClassLoaderHttpServiceInvoker
 * @author Phil Zoio
 */
public interface HttpServiceInvoker {

    void invoke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
