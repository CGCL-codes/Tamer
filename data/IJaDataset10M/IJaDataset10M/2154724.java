package org.jomc.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.jomc.model.Instance;
import org.jomc.model.Modules;
import org.jomc.model.ObjectFactory;
import org.jomc.model.modlet.ModelHelper;
import org.jomc.modlet.Model;
import org.jomc.modlet.ModelContext;

/**
 * Displays a project's test instance.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 *
 * @goal show-test-instance
 * @threadSafe
 * @requiresDependencyResolution test
 * @since 1.1
 */
public final class TestInstanceShowMojo extends AbstractModelShowMojo {

    /**
     * Identifier of the instance to show.
     *
     * @parameter expression="${jomc.identifier}"
     * @required
     */
    private String identifier;

    /**
     * Execution strategy of the goal ({@code always} or {@code once-per-session}).
     *
     * @parameter default-value="once-per-session" expression="${jomc.showTestInstanceExecutionStrategy}"
     */
    private String showTestInstanceExecutionStrategy;

    /** Creates a new {@code TestInstanceShowMojo} instance. */
    public TestInstanceShowMojo() {
        super();
    }

    @Override
    protected Model getDisplayModel(final ModelContext modelContext) throws MojoExecutionException {
        final Model model = this.getModel(modelContext);
        final Modules modules = ModelHelper.getModules(model);
        final Instance instance = modules != null ? modules.getInstance(this.identifier) : null;
        Model displayModel = null;
        if (instance != null) {
            displayModel = new Model();
            displayModel.setIdentifier(model.getIdentifier());
            displayModel.getAny().add(new ObjectFactory().createInstance(instance));
        }
        return displayModel;
    }

    @Override
    protected ClassLoader getDisplayClassLoader() throws MojoExecutionException {
        return this.getTestClassLoader();
    }

    @Override
    protected String getGoal() throws MojoExecutionException {
        return "show-test-instance";
    }

    @Override
    protected String getExecutionStrategy() throws MojoExecutionException {
        return this.showTestInstanceExecutionStrategy;
    }
}
