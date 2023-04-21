package org.fb4j.client.parser.fql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Gino Miceli
 * @author Mino Togna
 */
public class FqlQuery {

    private static final String WHITESPACE = "\\s+";

    private static final String WORD = "\\w+";

    private static final Pattern MAIN_PATTERN = Pattern.compile("SELECT" + WHITESPACE + "(.*)" + WHITESPACE + "FROM" + WHITESPACE + "(" + WORD + ")" + WHITESPACE + "WHERE .*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final String QUOTATION_MARK = "\"";

    private static final String OPEN_PARENTHESIS = "(";

    private static final String FIELD_DELIMITER = "\\s*,\\s*";

    private List<String> fields;

    private String table;

    private FqlQuery(String fql) throws FqlQueryParseException {
        parseInternal(fql);
    }

    public List<String> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public String getTable() {
        return table;
    }

    private void parseInternal(String fql) throws FqlQueryParseException {
        Matcher matcher = MAIN_PATTERN.matcher(fql);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new FqlQueryParseException("Unknown FQL format", fql);
        }
        String[] fld = matcher.group(1).split(FIELD_DELIMITER);
        fields = new ArrayList<String>(fld.length);
        for (String fieldName : fld) {
            if (fieldName.contains(QUOTATION_MARK) || fieldName.contains(OPEN_PARENTHESIS)) {
                throw new FqlQueryParseException("Derived attributes not supported(" + fieldName + ").  " + "Use Java methods and constants instead!", fql);
            }
            fields.add(fieldName);
        }
        table = matcher.group(2);
    }

    public static FqlQuery parse(String fql) throws FqlQueryParseException {
        return new FqlQuery(fql);
    }
}
