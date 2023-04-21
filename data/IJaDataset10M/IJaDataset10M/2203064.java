package org.modelibra.swing.app.gen;

import org.modelibra.config.DomainConfig;
import org.modelibra.gen.Generator;

/**
 * Generates the Swing application based on the domain configuration.
 * 
 * @author Dzenan Ridjanovic
 * @version 2009-11-17
 */
public class ModelibraSwingGenerator extends Generator {

    private String sourceDirectoryPath;

    private StartGenerator startGenerator;

    private TextResGenerator textResGenerator;

    /**
	 * Constructs the generator.
	 * 
	 * @param domainConfig
	 *            domain configuration
	 * @param sourceDirectoryPath
	 *            source directory path
	 */
    public ModelibraSwingGenerator(DomainConfig domainConfig, String sourceDirectoryPath) {
        this.sourceDirectoryPath = sourceDirectoryPath;
        startGenerator = new StartGenerator(domainConfig, sourceDirectoryPath);
        textResGenerator = new TextResGenerator(domainConfig, sourceDirectoryPath);
    }

    /**
	 * Constructs the generator.
	 * 
	 * @param domainConfig
	 *            domain configuration
	 * @param sourceDirectoryPath
	 *            source directory path
	 * @param minCodeGen
	 *            <code>true</code> if it is a minCodeGen
	 */
    public ModelibraSwingGenerator(DomainConfig domainConfig, String sourceDirectoryPath, boolean minCodeGen) {
        this.sourceDirectoryPath = sourceDirectoryPath;
        startGenerator = new StartGenerator(domainConfig, sourceDirectoryPath);
        textResGenerator = new TextResGenerator(domainConfig, sourceDirectoryPath, minCodeGen);
    }

    /**
	 * Get code directory path.
	 * 
	 * @return code directory path
	 */
    public String getCodeDirectoryPath() {
        return sourceDirectoryPath;
    }

    /**
	 * Get Start generator.
	 * 
	 * @return Start generator
	 */
    public StartGenerator getStartGenerator() {
        return startGenerator;
    }

    /**
	 * Get TextRes generator.
	 * 
	 * @return TextRes generator
	 */
    public TextResGenerator getTextResGenerator() {
        return textResGenerator;
    }

    /**
	 * Generates all.
	 */
    public void generate() {
        startGenerator.generate();
        textResGenerator.generate();
    }
}
