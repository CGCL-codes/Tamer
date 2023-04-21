package com.mgensystems.mdss.base.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import com.mgensystems.mdss.base.EmbeddedDependencyBuilder;
import com.mgensystems.mdss.dependency.MavenDependency;
import com.mgensystems.mdss.util.Reporter;
import com.mgensystems.mdss.util.config.MDSSConfigurationException;
import com.mgensystems.mdss.util.config.XMLConfig;

public class Console {

    public static void main(String args[]) {
        CommandLineParser cli_parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        XMLConfig config;
        EmbeddedDependencyBuilder db = new EmbeddedDependencyBuilder();
        Options opts = buildOptions();
        CommandLine cli = null;
        try {
            cli = cli_parser.parse(opts, args);
        } catch (ParseException e) {
            formatter.printHelp("MDSS", opts, true);
            System.exit(1);
        }
        File configLoc = new File(cli.getOptionValue("f"));
        if (!configLoc.exists()) {
            Reporter.report("Config file does not exist: " + configLoc, true);
            System.exit(1);
        }
        try {
            config = new XMLConfig(configLoc);
            config.loadConfig();
            db.setConfig(config);
            Map<String, MavenDependency> deps = db.getDependencies();
        } catch (MDSSConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected static Options buildOptions() {
        Options options = new Options();
        Option xc = OptionBuilder.withArgName("file").hasArg().isRequired().withLongOpt("config-file").withDescription("location of the mdss config file").create("f");
        options.addOption(xc);
        return options;
    }
}
