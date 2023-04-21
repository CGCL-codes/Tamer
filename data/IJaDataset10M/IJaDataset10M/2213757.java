package net.stickycode.plugin.wait;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * The pause goal lets you pause the build process for a specified time period
 *
 * @goal pause
 */
public class PauseMojo extends AbstractMojo {

    /**
   * The length of time to pause in milliseconds
   * @parameter default-value="0"
   */
    private long timeout;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Sleeping for " + timeout + "ms");
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new MojoExecutionException("Sleep was interupted", e);
        }
    }
}
