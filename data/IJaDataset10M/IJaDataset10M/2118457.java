package com.googlecode.yamaguchi.officeportal;

import org.apache.wicket.application.ReloadingClassLoader;
import org.apache.wicket.protocol.http.ReloadingWicketFilter;

/**
 * @author h_yamaguchi
 * 
 */
public class WicketHotDeployFilter extends ReloadingWicketFilter {

    static {
        ReloadingClassLoader.includePattern("com.googlecode.yamaguchi.officeportal.*");
    }
}
