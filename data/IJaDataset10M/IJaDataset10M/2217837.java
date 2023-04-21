package com.google.code.play;

import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Stop Play! Server ("play stop" equivalent).
 * 
 * @author <a href="mailto:gslowikowski@gmail.com">Grzegorz Slowikowski</a>
 * @goal stop
 */
public class PlayStopMojo extends AbstractPlayStopServerMojo {

    @Override
    protected void internalExecute() throws MojoExecutionException, MojoFailureException, IOException {
        stopServer();
        getLog().info("Play! Server stopped");
    }
}
