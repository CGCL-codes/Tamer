package org.dbmaintain.script.parser.parsingstate.impl;

import org.dbmaintain.script.parser.parsingstate.PlSqlBlockMatcher;
import java.util.regex.Pattern;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class OraclePlSqlBlockMatcher implements PlSqlBlockMatcher {

    private static final Pattern PL_SQL_PATTERN = Pattern.compile("^(CREATE (OR REPLACE )?(PACKAGE|LIBRARY|FUNCTION|PROCEDURE|TRIGGER|TYPE)|DECLARE|BEGIN)");

    public boolean isStartOfPlSqlBlock(StringBuilder statementWithoutCommentsOrWhitespace) {
        return PL_SQL_PATTERN.matcher(statementWithoutCommentsOrWhitespace).matches();
    }
}
