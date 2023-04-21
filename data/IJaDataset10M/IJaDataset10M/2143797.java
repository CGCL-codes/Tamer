package org.jomc.mojo;

import java.util.ResourceBundle;
import java.util.logging.Level;
import org.apache.maven.plugin.MojoExecutionException;
import org.jomc.model.ModelContext;
import org.jomc.model.ModelValidationReport;

/**
 * Validates a projects' main modules.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id: MainModulesValidateMojo.java 1866 2010-05-23 13:56:06Z schulte2005 $
 *
 * @phase process-classes
 * @goal validate-main-modules
 * @threadSafe
 * @requiresDependencyResolution test
 */
public final class MainModulesValidateMojo extends AbstractJomcMojo {

    /** Constant for the name of the tool backing the mojo. */
    private static final String TOOLNAME = "ModelValidator";

    /** Creates a new {@code ValidateMainModulesMojo} instance. */
    public MainModulesValidateMojo() {
        super();
    }

    @Override
    protected void executeTool() throws Exception {
        final ModelContext context = this.createModelContext(this.getMainClassLoader());
        final ModelValidationReport validationReport = context.validateModel(this.getToolModules(context));
        this.log(context, validationReport.isModelValid() ? Level.INFO : Level.SEVERE, validationReport);
        if (!validationReport.isModelValid()) {
            throw new MojoExecutionException(getMessage("failed"));
        }
        this.logSeparator(Level.INFO);
        this.logToolSuccess(TOOLNAME);
        this.logSeparator(Level.INFO);
    }

    private static String getMessage(final String key) {
        return ResourceBundle.getBundle(MainModulesValidateMojo.class.getName().replace('.', '/')).getString(key);
    }
}
