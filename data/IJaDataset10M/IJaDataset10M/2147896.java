package org.eclipse.datatools.sqltools.launching;

import java.sql.Connection;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * This interface should be implemented by clients that need to customize the routine object launching behavior.
 * @author Shifeng Yu
 */
public interface IExtendedLaunchSupport {

    /**
     * Does all pretreatment in this method before launching
     * @param config
     * @param conn the connection object
     * @param mode 
     */
    public void preLaunch(ILaunchConfiguration config, Connection conn, String mode) throws CoreException;

    /**
     * Does all post treatment in this method after launching 
     * @param config
     * @param conn the connection object
     * @param mode 
     */
    public void postLaunch(ILaunchConfiguration config, Connection conn, String mode) throws CoreException;
}
