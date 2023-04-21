package net.sf.mavenizer.generators;

import java.io.File;
import net.sf.mavenizer.model.Project;

/**
 * @author <a href="mailto:cedric-vidal@users.sourceforge.net">C&eacute;dric
 *         Vidal</a>
 * 
 */
public interface Generator {

    /**
	 * Generates a Mavenizer project
	 * 
	 * @param mavenizer
	 * @throws GenerationException
	 */
    public void generate(Project mavenizer) throws GenerationException;

    /**
	 * Configures the directory the geneation is going to occur in.
	 * 
	 * @param outputDir
	 */
    public void setOutputDir(File outputDir);
}
