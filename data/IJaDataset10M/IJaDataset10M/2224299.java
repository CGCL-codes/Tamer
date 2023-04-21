package org.javascriptxmldoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.javascriptxmldoc.generation.DocGenerator;
import org.javascriptxmldoc.meta.Project;
import org.javascriptxmldoc.reader.ProjectReader;
import org.javascriptxmldoc.reader.ReadException;
import org.javascriptxmldoc.reader.xml.XMLProjectReader;
import org.javascriptxmldoc.velocity.VelocityGenerator;

/**
 * The main class, used to run the xml reader and the velocity generator
 * @author Luis Fernando Planella Gonzalez
 */
public class JavaScriptXmlDoc {

    /**
     * The entry point
     * @param args Arguments
     */
    public static void main(String[] args) {
        try {
            CommandLine cmd = buildCommandLine(args);
            ProjectReader config = buildConfigurator(cmd);
            Project project = config.read();
            DocGenerator generator = buildGenerator(cmd);
            generator.generate(project);
            System.out.println("Documentation successfully generated");
        } catch (ParseException e) {
            printUsage();
        } catch (Exception e) {
            System.out.println("Error while generating the documentation");
            e.printStackTrace();
        }
    }

    /**
	 * Prints the command line usage
	 */
    public static void printUsage() {
        System.out.println("JavaScriptXmlDoc");
        HelpFormatter fmt = new HelpFormatter();
        fmt.printHelp("javascriptdoc", buildOptions());
        System.exit(0);
    }

    /**
	 * Returns the command-line options
     * @param args The argument array
     * @return The command-line options
     * @throws ParseException Error while parsing the command line
     */
    private static CommandLine buildCommandLine(String[] args) throws ParseException {
        Options options = buildOptions();
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        if (!cmd.hasOption("f") || !cmd.hasOption("d")) {
            printUsage();
        }
        return cmd;
    }

    /**
	 * Builds the ProjectReader to read the data
     * @param cmd The command line arguments
     * @return The configurator
     * @throws ReadException Error on configuration
     */
    private static ProjectReader buildConfigurator(CommandLine cmd) throws ReadException {
        try {
            String fileName = cmd.getOptionValue("f");
            String encoding = cmd.getOptionValue("e", XMLProjectReader.DEFAULT_ENCODING);
            InputStream in = getInputStream(fileName);
            ProjectReader config = new XMLProjectReader(in, encoding);
            return config;
        } catch (Exception e) {
            throw new ReadException(e);
        }
    }

    /**
     * Builds the documentation generator
     * @param cmd The command line
     * @return The documentation generator
     */
    private static DocGenerator buildGenerator(CommandLine cmd) {
        File destination = new File(cmd.getOptionValue("d"));
        String template = cmd.getOptionValue("t");
        if (template == null) {
            return new VelocityGenerator(destination);
        } else {
            return new VelocityGenerator(destination, new File(template));
        }
    }

    /**
     * Builds the command line options
     * @return The options
     */
    private static Options buildOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "The XML file. May be an absolute path or relative to the current path or to a directory in the class path. Mandatory.");
        options.addOption("d", "dest", true, "The destination folder. May be an absolute path or relative to the current path. Mandatory.");
        options.addOption("e", "encoding", true, "The character encoding. Defaults to ISO-8859-1. Optional.");
        options.addOption("t", "template", true, "The folder containing the Velocity templates. May be an absolute path or relative to the current path. Defaults to 'template' on the class path. Optional.");
        options.addOption("?", "help", true, "Displays this command help.");
        return options;
    }

    /**
	 * Returns the input stream to the xml file
     * @param fileName The file name
     * @return The input Stream
     * @throws FileNotFoundException The file does not exists
     */
    private static InputStream getInputStream(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        InputStream in = null;
        if (file.exists()) {
            in = new FileInputStream(file);
        } else {
            in = JavaScriptXmlDoc.class.getResourceAsStream('/' + fileName);
        }
        if (in == null) {
            throw new FileNotFoundException(fileName);
        }
        return in;
    }
}
