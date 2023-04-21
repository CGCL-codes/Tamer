package net.sf.spring.batch.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Parse the command line.
 * @author willy
 *
 */
public class ArgumentParser {

    protected Options options = new Options();

    public ArgumentParser() {
        options.addOption(buildOption('b', "batch-name", true, "Specify batch name", 1, "batch name"));
        options.addOption(buildOption('h', "batch-home", false, "Specify batch home", 1, "batch home"));
        options.addOption(buildOption('g', "global-properties", false, "Specify common properties file", 1, "properties file"));
        options.addOption(buildOption('c', "application-context", false, "Specify context file", 1, "context file"));
        options.addOption(buildOption('d', "validate-resource", false, "Run resources validation"));
        options.addOption(buildOption('v', "version", false, "Display version"));
        options.addOption(buildOption('?', "help", false, "Display help"));
        options.addOption(OptionBuilder.withArgName("property=value").hasArgs(2).withValueSeparator().withDescription("use value for given property").create("D"));
    }

    /**
	 * Parses the command line arguments
	 * @param args the command line arguments
	 * @return The configuration.
	 * @throws ParseException when options aren't correct.
	 */
    public LauncherConfiguration parse(String args[]) throws ParseException {
        LauncherConfiguration cfg = new LauncherConfiguration();
        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(options, args);
        cfg.setDoingHelpOnly(line.hasOption('?'));
        cfg.setBatchName(line.getOptionValue('b'));
        cfg.setDoingResourceValidationOnly(line.hasOption('d'));
        cfg.setDoingVersionOnly(line.hasOption('v'));
        if (line.hasOption("g")) cfg.setGlobalProperties(line.getOptionValue('g'));
        if (line.hasOption('c')) cfg.setSpringConfigPath(line.getOptionValue('c'));
        if (line.hasOption('D')) cfg.setBatchArgs(line.getOptionProperties("D"));
        return cfg;
    }

    public void displayHelp(String name) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(name, options);
    }

    protected Option buildOption(char shortOpt, String longOpt, boolean required, String description) {
        return OptionBuilder.withDescription(description).withLongOpt(longOpt).isRequired(required).hasArgs(0).create(shortOpt);
    }

    protected Option buildOption(char shortOpt, String longOpt, boolean required, String description, int argCount, String argName) {
        return OptionBuilder.withArgName(argName).withDescription(description).withLongOpt(longOpt).isRequired(required).hasArgs(argCount).create(shortOpt);
    }
}
