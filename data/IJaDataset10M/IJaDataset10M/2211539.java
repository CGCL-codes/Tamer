package net.sf.doodleproject.mavenite.doxygen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang.BooleanUtils;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;

/**
 * @goal report
 * @version $Revision: 1.4 $ $Date: 2008/02/11 02:23:24 $
 */
public class DoxygenReport extends AbstractMavenReport {

    /**
	 * @parameter expression="${doxygen.alphabeticalIndex}" default-value="false"
	 * @required
	 */
    private boolean alphabeticalIndex;

    /**
	 * @parameter expression="${doxygen.columnsInAlphabeticalIndex}"
	 *            default-value="3"
	 * @required
	 */
    private int columnsInAlphabeticalIndex;

    /**
	 * @parameter expression="${doxygen.configurationFile}" default-value="${basedir}/src/doxygen/doxygen.config"
	 */
    private File configurationFile;

    /**
	 * @parameter expression="${doxygen.executable}" default-value="doxygen.exe"
	 * @required
	 */
    private File executable;

    /**
	 * @parameter expression="${doxygen.filePatterns}" default-value="*.c *.cc *.cxx *.cpp *.c++ *.java *.ii *.ixx *.ipp *.i++ *.inl *.h *.hh *.hxx *.hpp .h++ *.idl *.odl *.cs *.php *.php3 *.inc *.m *.mm"
	 * @required
	 */
    private String filePatterns;

    /**
	 * @parameter expression="${doxygen.generateHtml}" default-value="true"
	 * @required
	 */
    private boolean generateHtml;

    /**
	 * @parameter expression="${doxygen.generateLatex}" default-value="false"
	 * @required
	 */
    private boolean generateLatex;

    /**
	 * @parameter expression="${doxygen.htmlFooter}" default-value="${basedir}/src/doxygen/footer.html"
	 */
    private File htmlFooter;

    /**
	 * @parameter expression="${doxygen.htmlHeader}" default-value="${basedir}/src/doxygen/header.html"
	 */
    private File htmlHeader;

    /**
	 * @parameter expression="${doxygen.inputDirectory}" default-value="${basedir}"
	 * @required
	 */
    private File inputDirectory;

    /**
	 * @parameter expression="${doxygen.outputDirectory}" alias="destDir"
	 *            default-value="${project.build.directory}/apidocs"
	 * @required
	 */
    private File outputDirectory;

    /**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
    private MavenProject project;

    /**
	 * @parameter expression="${doxygen.projectName}"
	 *            default-value="${project.name}"
	 * @required
	 */
    private String projectName;

    /**
	 * @parameter expression="${doxygen.projectNumber}"
	 *            default-value="${project.version}"
	 * @required
	 */
    private String projectNumber;

    /**
	 * @parameter expression="${doxygen.recursive}" default-value="true"
	 * @required
	 */
    private boolean recursive;

    /**
	 * @component
	 */
    private Renderer siteRenderer;

    /**
	 * @parameter expression="${doxygen.workDirectory}"
	 *            default-value="${project.build.directory}/doxygen"
	 * @required
	 */
    private File workDirectory;

    private void addConfiguration(PrintWriter out) throws FileNotFoundException, IOException {
        if (doesFileExist(configurationFile)) {
            Reader in = null;
            try {
                in = new FileReader(configurationFile);
                IOUtil.copy(in, out);
            } finally {
                IOUtil.close(in);
            }
        }
    }

    private void addConfiguration(PrintWriter config, boolean condition, String key, boolean value) {
        addConfiguration(config, condition, key, BooleanUtils.toString(value, "YES", "NO"));
    }

    private void addConfiguration(PrintWriter config, boolean condition, String key, File value) {
        if (value != null) {
            addConfiguration(config, condition, key, value.getAbsolutePath());
        }
    }

    private void addConfiguration(PrintWriter config, boolean condition, String key, int value) {
        addConfiguration(config, condition, key, Integer.toString(value));
    }

    private void addConfiguration(PrintWriter config, boolean condition, String key, String value) {
        if (condition) {
            config.print(key);
            config.print('=');
            config.println(value);
        }
    }

    private void addConfiguration(PrintWriter config, String key, boolean value) {
        addConfiguration(config, true, key, value);
    }

    private void addConfiguration(PrintWriter config, String key, String value) {
        addConfiguration(config, true, key, value);
    }

    private File buildConfigurationFile() throws MavenReportException {
        File ret = new File(workDirectory, "doxygen.config");
        buildConfigurationFile(ret);
        return ret;
    }

    private void buildConfigurationFile(File config) throws MavenReportException {
        config.getParentFile().mkdirs();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(config)));
            addConfiguration(out, "ALPHABETICAL_INDEX", alphabeticalIndex);
            addConfiguration(out, 1 <= columnsInAlphabeticalIndex && columnsInAlphabeticalIndex <= 20, "COLS_IN_ALPHA_INDEX", columnsInAlphabeticalIndex);
            addConfiguration(out, "GENERATE_HTML", generateHtml);
            addConfiguration(out, "GENERATE_LATEX", generateLatex);
            addConfiguration(out, filePatterns != null, "FILE_PATTERNS", filePatterns);
            addConfiguration(out, doesFileExist(htmlFooter), "HTML_FOOTER", htmlFooter);
            addConfiguration(out, doesFileExist(htmlHeader), "HTML_HEADER", htmlHeader);
            addConfiguration(out, doesPathExist(inputDirectory), "INPUT", inputDirectory);
            addConfiguration(out, doesDirectoryExist(outputDirectory), "OUTPUT_DIRECTORY", outputDirectory);
            addConfiguration(out, "PROJECT_NAME", projectName);
            addConfiguration(out, "PROJECT_NUMBER", projectNumber);
            addConfiguration(out, "RECURSIVE", recursive);
            addConfiguration(out);
        } catch (IOException ex) {
            throw new MavenReportException("Error creating Doxygen configuration file '" + config.getAbsolutePath() + "'.", ex);
        } finally {
            IOUtil.close(out);
        }
    }

    private boolean doesDirectoryExist(File dir) {
        return doesPathExist(dir) && dir.isDirectory();
    }

    private boolean doesFileExist(File file) {
        return doesPathExist(file) && file.isFile();
    }

    private boolean doesPathExist(File file) {
        return file != null && file.exists();
    }

    /**
	 * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
	 */
    protected void executeReport(Locale locale) throws MavenReportException {
        File config = buildConfigurationFile();
        getLog().info("Generating Doxygen documentation...");
        Commandline cli = new Commandline();
        cli.getShell().setQuotedArgumentsEnabled(false);
        cli.setWorkingDirectory(workDirectory.getAbsolutePath());
        cli.setExecutable(getExecutablePath());
        cli.createArg().setValue(config.getAbsolutePath());
        try {
            Writer stringWriter = new StringWriter();
            StreamConsumer out = new WriterStreamConsumer(stringWriter);
            StreamConsumer err = new WriterStreamConsumer(stringWriter);
            int returnCode = CommandLineUtils.executeCommandLine(cli, out, err);
            getLog().debug("Doxygen output:");
            getLog().debug(stringWriter.toString());
            if (returnCode != 0) {
                throw new MavenReportException("Failed to generate Doxygen documentation.");
            }
        } catch (CommandLineException ex) {
            throw new MavenReportException("Error while executing Doxygen.", ex);
        }
        getLog().info("Generated Doxygen documentation.");
    }

    /**
	 * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
	 */
    public String getDescription(Locale locale) {
        return "Doxygen API documentation.";
    }

    private String getExecutablePath() {
        return executable.getPath();
    }

    public String getName(Locale locale) {
        return "Doxygen";
    }

    protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
    }

    public String getOutputName() {
        return "html/blank";
    }

    protected MavenProject getProject() {
        return project;
    }

    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    public boolean isExternalReport() {
        return true;
    }
}
