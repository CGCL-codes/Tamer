package org.rails4j.mvc.ctrl.intercept;

import org.rails4j.mvc.beans.UrlMapping;
import org.rails4j.mvc.view.View;

/**
 * @author arnaud
 *
 */
public interface Interceptor {

    /**
     * 
     * @param urlmapping
     * @return
     */
    public boolean accept(UrlMapping urlmapping);

    /**
     * 
     * @return
     */
    public View execute();
}
