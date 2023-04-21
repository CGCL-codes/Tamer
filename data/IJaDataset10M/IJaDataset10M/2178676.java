package net.openmaximo.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.openmaximo.log.Log4jLogger;
import net.openmaximo.maven.plugins.templates.Template;
import net.openmaximo.util.PackageUtil;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * <p>
 * MOJO that generates IBM Maximo Crontask classes.
 * </p>
 * <h2>Override methods available</h2>
 * <p>
 * None at the moment.
 * </p>
 * <hr />
 * <h2>Latest Modification</h2>
 * <p>
 * $Date: 2009-12-30 06:39:52 -0500 (Wed, 30 Dec 2009) $
 * </p>
 * <p>
 * $Rev: 60 $
 * </p>
 * 
 * @author openmaximo.net
 * @goal crontask
 * @since 0.2
 * @version 1.0
 */
public class Crontask extends Maximo {

    /**
   * 
   */
    private static final Logger lLogger = Log4jLogger.getLogger(Crontask.class);

    /**
   * <p>
   * Crontask we'll be extending.
   * </p>
   * 
   * @parameter expression="${crontask.base}"
   *            default-value="psdi.server.SimpleCronTask"
   */
    private String baseCrontask;

    /**
   * <p>
   * Package and classname of the new Crontask.
   * </p>
   * 
   * @parameter expression="${crontask.new}"
   * @required
   */
    private String newCrontask;

    /**
   * @return the baseCrontask
   */
    public String getBaseCrontask() {
        return baseCrontask;
    }

    /**
   * @param baseCrontask
   *          the baseCrontask to set
   */
    public void setBaseCrontask(String Crontask) {
        this.baseCrontask = Crontask;
    }

    /**
   * @return the newCrontask
   */
    public String getNewCrontask() {
        return newCrontask;
    }

    /**
   * @param newCrontask
   *          the newCrontask to set
   */
    public void setNewCrontask(String newCrontask) {
        this.newCrontask = newCrontask;
    }

    /**
   * <p>
   * Executes the plugin.
   * </p>
   */
    public void execute() throws MojoExecutionException {
        lLogger.info(".execute() started.");
        if (lLogger.isDebugEnabled()) {
            lLogger.debug("newCrontask:" + newCrontask);
            lLogger.debug("baseCrontask:" + baseCrontask);
            lLogger.debug("outputDirectory:" + outputDirectory);
            lLogger.debug("PluginContext:" + this.getPluginContext().keySet().toString());
        }
        if (newCrontask == null) {
            throw new MojoExecutionException("The new MBO must not be empty. Please use the -DnewCrontask=... parameter.");
        }
        File f = outputDirectory;
        if (!f.exists()) {
            f.mkdirs();
        } else if (f.isFile()) {
            throw new MojoExecutionException("Target folder cannot be a file.");
        }
        try {
            String crontaskName = PackageUtil.getClassName(newCrontask);
            String packageName = newCrontask.replaceFirst(".".concat(crontaskName), "");
            String packageFolder = PackageUtil.packageToFolder(packageName);
            if (lLogger.isDebugEnabled()) {
                lLogger.debug("crontaskName:" + crontaskName);
                lLogger.debug("packageName:" + packageName);
                lLogger.debug("packageFolder:" + packageFolder);
            }
            HashMap patternsHM = Template.setPatterns(crontaskName, this.getBaseCrontask(), packageName, this.getClass().getSimpleName(), this.getOverrideMethods(), this.getClass());
            String outputFolderPath = outputDirectory.getPath().concat("/").concat(packageFolder);
            if (lLogger.isDebugEnabled()) {
                lLogger.debug("outputFolderPath:" + outputFolderPath);
            }
            String outputClassName = null;
            Template templateApp = null;
            outputClassName = crontaskName;
            templateApp = new Template(this.getClass(), "Crontask.java", outputFolderPath, outputClassName, this.getCharEncoding(), patternsHM);
            templateApp.applyTemplate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lLogger.info(".execute() finished.");
    }
}
