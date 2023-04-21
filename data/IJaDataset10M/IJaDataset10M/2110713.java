package org.rails4j.mvc.ctrl;

import org.rails4j.mvc.beans.UrlMapping;

/**
 * @author A
 * 
 */
public interface UrlMapper {

    /**
     * 
     * @param request
     * @return
     */
    UrlMapping map(final String uri);
}
