package net.sf.groovymda.maven;

import java.io.File;
import net.sf.groovymda.ant.GroovyMDATask;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which transforms a UML model into code using a script.  
 *
 * @goal gmda
 * 
 * @phase generate-sources
 */
public class GroovyMDAMojo extends AbstractMojo {

    /**
	 * Location of a jar file containing a UML model.
	 * @parameter
	 */
    private File modelJar;

    /**
	 * Either:
	 * - path to a UML model.
	 * - the name of a UML model file within the <code>modelJar</code> parameter.
	 * @parameter
	 * @required  
	 */
    private File modelFile;

    /**
	 * Location of a script that will transform the UML model 
	 * (GroovyMDA will use its default if this parameter is 
	 * not set.)
	 * @parameter
	 */
    private File scriptFile;

    /**
	 * Location of the transformation results.
	 * @parameter expression="${project.build.directory}"     
	 */
    private File outputDir;

    public void execute() throws MojoExecutionException {
        getLog().debug("Running GroovyMDATask using " + "modelJar: " + modelJar + ", " + "modelFile: " + modelFile + ", " + "scriptFile: " + scriptFile + ", " + "outputDir: " + outputDir);
        GroovyMDATask gen = new GroovyMDATask();
        gen.setModelJar(modelJar);
        gen.setModelFile(modelFile);
        gen.setOutputDir(outputDir);
        gen.setScript(scriptFile);
        gen.execute();
    }
}
