package de.fau.cs.dosis.database;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.fau.cs.dosis.CliFactory;
import de.fau.cs.dosis.Manager;

public class LiquibaseDumpCliFactory implements CliFactory {

    private Logger logger = LoggerFactory.getLogger(LiquibaseDumpCliFactory.class);

    @Override
    public String command() {
        return "liquibase-dump";
    }

    @Override
    public String description() {
        return "creates xml update script to create schema in given database";
    }

    private static Options options() {
        Options options = Database.options();
        Option newdatabase = new Option("o", "out", true, "");
        newdatabase.setRequired(true);
        options.addOption(newdatabase);
        return options;
    }

    /**
     * Instantiates/Validates new Baseline Object
     */
    public Runnable create(String[] args) {
        CommandLineParser clp = new PosixParser();
        try {
            CommandLine cmd = clp.parse(options(), args);
            LiquibaseDump ld = new LiquibaseDump();
            ld.setPassword(cmd.getOptionValue("password"));
            ld.setUser(cmd.getOptionValue("user"));
            ld.setUrl(cmd.getOptionValue("connection"));
            ld.setDriver(cmd.getOptionValue("driver"));
            ld.setOut(cmd.getOptionValue("out"));
            return ld;
        } catch (ParseException ex) {
            logger.error("unable to parse command line", ex);
            Manager.printHelp(this.command(), options());
            return null;
        }
    }
}
