package org.jomc.mojo;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Validates a projects' main class file model objects.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 *
 * @phase process-classes
 * @goal validate-main-classes
 * @threadSafe
 * @requiresDependencyResolution test
 */
public final class MainClassesValidateMojo extends AbstractClassesValidateMojo {

    /**
     * Execution strategy of the goal ({@code always} or {@code once-per-session}).
     *
     * @parameter default-value="once-per-session" expression="${jomc.validateMainClassesExecutionStrategy}"
     * @since 1.1
     */
    private String validateMainClassesExecutionStrategy;

    /** Creates a new {@code MainClassesValidateMojo} instance. */
    public MainClassesValidateMojo() {
        super();
    }

    @Override
    protected String getClassesModuleName() throws MojoExecutionException {
        return this.getModuleName();
    }

    @Override
    protected ClassLoader getClassesClassLoader() throws MojoExecutionException {
        return this.getMainClassLoader();
    }

    @Override
    protected File getClassesDirectory() throws MojoExecutionException {
        return this.getOutputDirectory();
    }

    @Override
    protected String getGoal() {
        return "validate-main-classes";
    }

    @Override
    protected String getExecutionStrategy() {
        return this.validateMainClassesExecutionStrategy;
    }
}
