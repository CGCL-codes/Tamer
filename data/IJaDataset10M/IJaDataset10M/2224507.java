package schemacrawler.main.dbconnector;

import schemacrawler.main.BaseOptionsParser;
import sf.util.CommandLineParser.Option;
import sf.util.CommandLineParser.StringOption;

/**
 * Options for the command line.
 * 
 * @author sfatehi
 */
abstract class BaseConnectorOptionsParser<O extends BaseConnectorOptions> extends BaseOptionsParser<O> {

    protected final StringOption optionSchemaPattern = new StringOption(Option.NO_SHORT_FORM, "schemapattern", null);

    protected final StringOption optionUser = new StringOption(Option.NO_SHORT_FORM, "user", null);

    protected final StringOption optionPassword = new StringOption(Option.NO_SHORT_FORM, "password", "");

    /**
   * Parses the command line into options.
   * 
   * @param args
   */
    protected BaseConnectorOptionsParser(final String[] args) {
        super(args);
    }
}
