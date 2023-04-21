package net.innig.sweetxml.maven;

import net.innig.sweetxml.ConversionMode;
import net.innig.sweetxml.FileConverterEngine;
import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Converts SweetXML files in the output directory to (or from) XML files.
 *
 * @author Paul Cantrell.
 *      Portions based on ResourceMojo.java from Maven, written
 *      by Michal Maczka, Jason van Zyl and Andreas Hoheneder.
 * @goal  convert-resources
 * @phase process-resources
 */
public class ConvertResourcesMojo extends AbstractMojo {

    /**
     * The directory in which there are resources to be converted.
     * By default, this is the project's outputDirectory.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String inputDirectory;

    /**
     * The directory in which to place the converted resources.
     * By default, this is the same as the inputDirectory.
     *
     * @parameter
     */
    private String outputDirectory;

    /**
     * The conversion mode. May be "s2x" (the default) or "x2s".
     *
     * @parameter
     */
    private String mode;

    /**
     * If true, the plugin will not print information about which files are being converted.
     * True by default.
     *
     * @parameter
     */
    private boolean quiet = true;

    /**
     * If true, the plugin will overwrite existing output files. True by default.
     *
     * @parameter
     */
    private boolean overwrite = true;

    /**
     * If true, the plugin will delete files from the output directory after converting them.
     * False by default.
     *
     * @parameter
     */
    private boolean deleteSources;

    public void execute() throws MojoExecutionException {
        if (mode == null) mode = "s2x";
        ConversionMode modeParsed;
        try {
            modeParsed = ConversionMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new MojoExecutionException("No such mode \"" + mode + "\"; expected \"x2s\" or \"s2x\"");
        }
        if (outputDirectory == null) outputDirectory = inputDirectory;
        File inputDir = new File(inputDirectory);
        File outputDir = new File(outputDirectory);
        if (!inputDir.exists()) throw new MojoExecutionException("SweetXML input directory does not exist: " + inputDir); else if (!inputDir.isDirectory()) throw new MojoExecutionException("SweetXML input directory is not a directory: " + inputDir);
        if (!outputDir.exists()) outputDir.mkdirs(); else if (!outputDir.isDirectory()) throw new MojoExecutionException("SweetXML output directory does not exist: " + outputDir);
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(inputDir);
        scanner.setIncludes(new String[] { "**/**" + modeParsed.getSourceExtension() });
        scanner.addDefaultExcludes();
        scanner.scan();
        FileConverterEngine converter = new FileConverterEngine(overwrite, quiet);
        for (String inFileName : scanner.getIncludedFiles()) try {
            File inFile = new File(inputDir, inFileName);
            File outFile = converter.outputFileFor(new File(outputDir, inFileName), modeParsed);
            if (!outFile.getParentFile().exists()) outFile.getParentFile().mkdirs();
            converter.convertFile(inFile, outFile, modeParsed);
            if (deleteSources) inFile.delete();
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to convert " + inFileName + " from " + modeParsed.getSourceExtension() + " to " + modeParsed.getTargetExtension(), ioe);
        }
    }
}
