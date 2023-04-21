package org.shortbrain.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which run app in yourkit
 * 
 * @goal run
 * @requiresDependencyResolution runtime
 * @execute phase="compile"
 * @description Run yourkit directly from maven
 */
public class YourKitRun extends AbstractYourKitMojo {

    /**
     * 
     * @since 0.1
     * @version $Id: YourKitRun.java 8 2007-12-10 19:14:43Z vdemeester $
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
    }

    @Override
    public void overConfiguration() {
    }
}
