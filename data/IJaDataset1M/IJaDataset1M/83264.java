package com.jdevflow.sunny.components;

import com.jdevflow.sunny.request.IRequestProcessor;
import com.jdevflow.sunny.request.parameters.IRequestParameters;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Common AJAX behavior interface. 
 * 
 * @author  SIY
 * @version 1.0
 */
public interface IBehavior {

    /**
     * Attach behavior to component.
     * 
     * @param component
     */
    public void setComponent(IComponent component);

    /**
     * Check if request parameters belong to this behavior.
     * 
     * @param param
     *        Parameters to check.
     * 
     * @return <code>true</code> if behavior can handle request with such a parameters.
     */
    public boolean match(IRequestParameters param);

    /**
     * Handle AJAX events.
     * 
     * @param processor
     * 
     * @return <code>false</code> if further processing is not required.
     * 
     * @throws IOException 
     * @throws ServletException 
     */
    public boolean handleEvent(IRequestProcessor processor) throws IOException, ServletException;
}
