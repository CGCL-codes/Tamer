package com.controltier.ctl.dispatcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * IDispatchedScript describes the parameters for a ctl-exec invocation sent to the Dispatcher
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision$
 */
public interface IDispatchedScript extends IDispatchedExecution {

    /**
     * Get the framework project name
     * @return project name
     */
    public String getFrameworkProject();

    /**
     * Get the full script
     * @return the script string
     */
    public String getScript();

    /**
     * Get an InputStream that can provide the full script
     * @return the inputstream
     * @throws IOException if an error occurs reading or getting the input stream
     */
    public InputStream getScriptAsStream();

    /**
     * Get the server-local script path
     * @return server-side script path
     */
    public String getServerScriptFilePath();
}
