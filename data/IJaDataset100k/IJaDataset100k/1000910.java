package org.apache.jetspeed.services.forward.configuration;

/**
 * Page interface
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: Page.java,v 1.2 2004/02/23 03:50:45 jford Exp $
 */
public interface Page {

    public String getName();

    public String getUser();

    public String getRole();

    public String getGroup();
}
