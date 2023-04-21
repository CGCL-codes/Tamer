package org.archive.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;

/**
 * Goal which touches a timestamp file.
 *
 * @goal generate
 * 
 * @phase process-classes
 */
public class SubclassesMojo extends AbstractMojo {

    /**
     * Output directory for generate .option files.
     * 
     * @parameter expression="${project.build.directory}/classes"
     * @required
     */
    private File outputDirectory;

    /**
     * Input directory containing .class files.
     * 
     * @parameter expression="${project.build.directory}/classes"
     * @required
     */
    private File inputDirectory;

    public void execute() throws MojoExecutionException {
        Config config = new Config();
        config.setInputDir(inputDirectory);
        config.setOutputDir(outputDirectory);
        Generator gen = new Generator(config);
        try {
            gen.generate();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
