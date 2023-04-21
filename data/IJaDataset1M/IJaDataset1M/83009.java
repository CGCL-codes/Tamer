package org.jomc.mojo;

import java.io.File;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import org.apache.maven.plugin.MojoExecutionException;
import org.jomc.model.Module;
import org.jomc.modlet.ModelContext;
import org.jomc.modlet.ModelValidationReport;
import org.jomc.modlet.ObjectFactory;
import org.jomc.tools.ClassFileProcessor;

/**
 * Base class for validating class file model objects.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 */
public abstract class AbstractClassesValidateMojo extends AbstractJomcMojo {

    /** Constant for the name of the tool backing the mojo. */
    private static final String TOOLNAME = "ClassFileProcessor";

    /** Creates a new {@code AbstractClassesValidateMojo} instance. */
    public AbstractClassesValidateMojo() {
        super();
    }

    @Override
    protected final void executeTool() throws Exception {
        this.logSeparator();
        if (this.isClassProcessingEnabled()) {
            this.logProcessingModule(TOOLNAME, this.getClassesModuleName());
            final ModelContext context = this.createModelContext(this.getClassesClassLoader());
            final ClassFileProcessor tool = this.createClassFileProcessor(context);
            final JAXBContext jaxbContext = context.createContext(this.getModel());
            final Source source = new JAXBSource(jaxbContext, new ObjectFactory().createModel(tool.getModel()));
            ModelValidationReport validationReport = context.validateModel(this.getModel(), source);
            this.log(context, validationReport.isModelValid() ? Level.INFO : Level.SEVERE, validationReport);
            if (validationReport.isModelValid()) {
                final Module module = tool.getModules() != null ? tool.getModules().getModule(this.getClassesModuleName()) : null;
                if (module != null) {
                    validationReport = tool.validateModelObjects(module, context, this.getClassesDirectory());
                    if (validationReport != null) {
                        this.log(context, validationReport.isModelValid() ? Level.INFO : Level.SEVERE, validationReport);
                        if (!validationReport.isModelValid()) {
                            throw new MojoExecutionException(Messages.getMessage("classFileValidationFailure"));
                        }
                    }
                    this.logToolSuccess(TOOLNAME);
                } else {
                    this.logMissingModule(this.getClassesModuleName());
                }
            } else {
                throw new MojoExecutionException(Messages.getMessage("classFileValidationFailure"));
            }
        } else if (this.isLoggable(Level.INFO)) {
            this.log(Level.INFO, Messages.getMessage("classFileValidationDisabled"), null);
        }
    }

    /**
     * Gets the name of the module to validate class file model objects of.
     *
     * @return The name of the module to validate class file model objects of.
     *
     * @throws MojoExecutionException if getting the name fails.
     */
    protected abstract String getClassesModuleName() throws MojoExecutionException;

    /**
     * Gets the class loader to use for validating class file model objects.
     *
     * @return The class loader to use for validating class file model objects.
     *
     * @throws MojoExecutionException if getting the class loader fails.
     */
    protected abstract ClassLoader getClassesClassLoader() throws MojoExecutionException;

    /**
     * Gets the directory holding the class files to validate model objects of.
     *
     * @return The directory holding the class files to validate model objects of.
     *
     * @throws MojoExecutionException if getting the directory fails.
     */
    protected abstract File getClassesDirectory() throws MojoExecutionException;
}
