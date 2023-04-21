package org.dbmaintain.script.parser.impl;

import org.dbmaintain.script.parser.parsingstate.PlSqlBlockMatcher;
import org.dbmaintain.script.parser.parsingstate.impl.PostgreSqlPlSqlBlockMatcher;
import java.util.Properties;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class PostgreSqlScriptParserFactory extends DefaultScriptParserFactory {

    public PostgreSqlScriptParserFactory(boolean backSlashEscapingEnabled, Properties scriptParameters) {
        super(backSlashEscapingEnabled, scriptParameters);
    }

    @Override
    protected PlSqlBlockMatcher createStoredProcedureMatcher() {
        return new PostgreSqlPlSqlBlockMatcher();
    }
}
