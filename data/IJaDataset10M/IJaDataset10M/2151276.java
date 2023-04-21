package org.jomc.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.xml.bind.Marshaller;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jomc.model.Instance;
import org.jomc.model.ModelContext;
import org.jomc.model.Modules;
import org.jomc.model.ObjectFactory;

/**
 * Dumps a project's test instance.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id: TestInstanceDumpMojo.java 1866 2010-05-23 13:56:06Z schulte2005 $
 *
 * @goal dump-test-instance
 * @threadSafe
 * @requiresDependencyResolution test
 */
public final class TestInstanceDumpMojo extends AbstractJomcMojo {

    /**
     * File to dump the instance to. If not set, data will be logged to the console.
     *
     * @parameter expression="${jomc.dumpFile}"
     */
    private File dumpFile;

    /**
     * Identifier of the instance to dump.
     *
     * @parameter expression="${jomc.identifier}"
     * @required
     */
    private String identifier;

    protected void executeTool() throws Exception {
        if (this.identifier == null) {
            throw new MojoFailureException(getMessage("mandatoryParameterMissing", "identifier"));
        }
        final ClassLoader classLoader = this.getTestClassLoader();
        final ModelContext modelContext = this.createModelContext(classLoader);
        final Modules modules = this.getToolModules(modelContext);
        final Instance instance = modules.getInstance(this.identifier);
        if (instance != null) {
            final Marshaller m = modelContext.createMarshaller();
            m.setSchema(modelContext.createSchema());
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (this.dumpFile != null) {
                if (this.dumpFile.exists()) {
                    this.dumpFile.delete();
                }
                final OutputStream out = new FileOutputStream(this.dumpFile);
                m.marshal(new ObjectFactory().createInstance(instance), out);
                out.close();
                this.getLog().info(getMessage("writing", this.dumpFile.getAbsolutePath()));
            } else {
                this.getLog().info("");
                final StringWriter stringWriter = new StringWriter();
                stringWriter.append(System.getProperty("line.separator"));
                stringWriter.append(System.getProperty("line.separator"));
                m.marshal(new ObjectFactory().createInstance(instance), stringWriter);
                this.getLog().info(stringWriter.toString());
                this.getLog().info("");
            }
        } else {
            throw new MojoExecutionException(getMessage("instanceNotFound", this.identifier));
        }
    }

    private static String getMessage(final String key, final Object... args) {
        return MessageFormat.format(ResourceBundle.getBundle(TestInstanceDumpMojo.class.getName().replace('.', '/')).getString(key), args);
    }
}
