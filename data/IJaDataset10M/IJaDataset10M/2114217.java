package org.jumpmind.symmetric;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jumpmind.symmetric.DbImport.Format;

/**
 * Import data from file to database tables.
 */
public class DbImportCommand extends AbstractCommandLauncher {

    private static final String OPTION_FORMAT = "format";

    private static final String OPTION_CATALOG = "catalog";

    private static final String OPTION_SCHEMA = "schema";

    private static final String OPTION_TABLE = "table";

    private static final String OPTION_USE_VARIABLE_DATES = "use-variable-dates";

    public DbImportCommand() {
        super("dbimport", "[file...]", "DbImport.Option.");
    }

    public static void main(String[] args) {
        new DbImportCommand().execute(args);
    }

    protected void printHelp(Options options) {
        System.out.println(app + " version " + Version.version());
        System.out.println("Import data from file to database tables.\n");
        super.printHelp(options);
    }

    @Override
    protected void buildOptions(Options options) {
        super.buildOptions(options);
        addOption(options, null, OPTION_FORMAT, true);
        addOption(options, null, OPTION_CATALOG, true);
        addOption(options, null, OPTION_SCHEMA, true);
        addOption(options, null, OPTION_TABLE, true);
        addOption(options, null, OPTION_USE_VARIABLE_DATES, false);
    }

    @Override
    protected boolean executeOptions(CommandLine line) throws Exception {
        DbImport dbImport = new DbImport(getDatabasePlatform());
        if (line.hasOption(OPTION_FORMAT)) {
            dbImport.setFormat(Format.valueOf(line.getOptionValue(OPTION_FORMAT).toUpperCase()));
        }
        if (line.hasOption(OPTION_CATALOG)) {
            dbImport.setCatalog(line.getOptionValue(OPTION_CATALOG));
        }
        if (line.hasOption(OPTION_SCHEMA)) {
            dbImport.setSchema(line.getOptionValue(OPTION_SCHEMA));
        }
        if (line.hasOption(OPTION_USE_VARIABLE_DATES)) {
            dbImport.setUseVariableForDates(true);
        }
        String[] args = line.getArgs();
        if (args.length == 0) {
            dbImport.importTables(System.in, line.getOptionValue(OPTION_TABLE));
        } else {
            for (String fileName : args) {
                if (!new File(fileName).exists()) {
                    throw new RuntimeException("Cannot find file " + fileName);
                }
            }
            for (String fileName : args) {
                FileInputStream in = new FileInputStream(fileName);
                dbImport.importTables(in, line.getOptionValue(OPTION_TABLE));
                in.close();
            }
        }
        return true;
    }
}
