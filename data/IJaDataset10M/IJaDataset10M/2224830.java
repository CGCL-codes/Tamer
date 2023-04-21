package org.blojsom.plugin.admin.event;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Process blog entry event contains information about a blog entry with hooks for retrieving the servlet request,
 * response, and the current plugin execution context.
 *
 * @author David Czarnecki
 * @version $Id: ProcessEntryEvent.java,v 1.1 2006/03/20 21:30:49 czarneckid Exp $
 * @since blojsom 3.0
 */
public class ProcessEntryEvent extends EntryEvent {

    protected HttpServletRequest _httpServletRequest;

    protected HttpServletResponse _httpServletResponse;

    protected Map _context;

    /**
     * Create a new event indicating something happened with an entry in the system.
     *
     * @param source    Source of the event
     * @param timestamp Event timestamp
     * @param entry     {@link Entry}
     * @param blog      {@link Blog}
     */
    public ProcessEntryEvent(Object source, Date timestamp, Entry entry, Blog blog, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map context) {
        super(source, timestamp, entry, blog);
        _httpServletRequest = httpServletRequest;
        _httpServletResponse = httpServletResponse;
        _context = context;
    }

    /**
     * Retrieve the servlet request
     *
     * @return {@link HttpServletRequest}
     */
    public HttpServletRequest getHttpServletRequest() {
        return _httpServletRequest;
    }

    /**
     * Retrieve the servlet response
     *
     * @return {@link HttpServletResponse}
     */
    public HttpServletResponse getHttpServletResponse() {
        return _httpServletResponse;
    }

    /**
     * Retrieve the plugin execution context
     *
     * @return Context map
     */
    public Map getContext() {
        return _context;
    }
}
