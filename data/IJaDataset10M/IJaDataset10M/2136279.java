package com.once.server.data;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.once.server.OwnerOrgKeeperThread;

@SuppressWarnings("unused")
public class DataReplacerThread extends Thread {

    private static final Logger m_logger = Logger.getLogger(DataReplacerThread.class);

    private static final int ESCAPE_LEVEL_REPLACEMENT_VALUE = 4;

    private static final int ESCAPE_LEVEL_SELECTED_VALUE = 8;

    private static final String PATTERN_ALIAS_PRIMARY = "primary";

    private static final String PATTERN_BRACKET_CLOSE = ")";

    private static final String PATTERN_BRACKET_OPEN = "(";

    private static final String PATTERN_DATA_END = "END";

    private static final String PATTERN_EQUALS = "=";

    private static final String PATTERN_INVALID_PRIMARY = "-1";

    private static final String PATTERN_MODEL_ALIAS_SEPARATOR = ".";

    private static final String PATTERN_NEW_LINE = "\n";

    private static final String PATTERN_NOTHING = "";

    private static final String PATTERN_QUOTE_DOUBLE = "\"";

    private static final String PATTERN_QUOTE_SINGLE = "'";

    private static final String PATTERN_RESULT_RECORDS = "RECORDS";

    private static final String PATTERN_RESULT_NO_SQL_ERRORS = "(NO SQL ERRORS)";

    private static final String PATTERN_SPACE = " ";

    private static final String PATTERN_WHERE_PART_AND = " AND ";

    private static final String PATTERN_WHERE_PART_IS = "IS";

    private static final String PATTERN_WHERE_PART_IS_NOT = "IS NOT";

    private static final String PATTERN_WHERE_PART_NOT = " NOT ";

    private static final String PATTERN_WHERE_PART_OR = " OR ";

    private static final String PATTERN_WHERE_PART_EQUALS = " = ";

    private String session;

    private String model;

    private String wherePart;

    private HashMap<String, String> record;

    private StringBuffer errorText;

    private int errorCount;

    private int percent;

    public DataReplacerThread(String newSession, String newModel, String newWherePart, HashMap<String, String> newRecord) {
        session = newSession;
        model = newModel;
        wherePart = newWherePart;
        record = newRecord;
        percent = 0;
        errorCount = 0;
        errorText = new StringBuffer();
        return;
    }

    public int getErrorCount() {
        return (errorCount);
    }

    public int getPercentageComplete() {
        return (percent);
    }

    public static boolean isPrimary(String alias) {
        return (alias != null ? (alias.endsWith(PATTERN_MODEL_ALIAS_SEPARATOR + PATTERN_ALIAS_PRIMARY) == true || alias.endsWith(PATTERN_MODEL_ALIAS_SEPARATOR + PATTERN_QUOTE_DOUBLE + PATTERN_ALIAS_PRIMARY + PATTERN_QUOTE_DOUBLE) == true) : false);
    }

    public static boolean isPrimary(String alias, String model) {
        return (alias != null ? ((alias.endsWith(PATTERN_MODEL_ALIAS_SEPARATOR + PATTERN_ALIAS_PRIMARY) == true || alias.endsWith(PATTERN_MODEL_ALIAS_SEPARATOR + PATTERN_QUOTE_DOUBLE + PATTERN_ALIAS_PRIMARY + PATTERN_QUOTE_DOUBLE) == true) && (alias.startsWith(model + PATTERN_MODEL_ALIAS_SEPARATOR) == true || alias.startsWith(PATTERN_QUOTE_DOUBLE + model + PATTERN_QUOTE_DOUBLE + PATTERN_MODEL_ALIAS_SEPARATOR) == true)) : false);
    }

    public static String fastQuoteSingleModelAlias(String unquoted) {
        boolean started;
        int index;
        return (unquoted != null ? ((started = unquoted.startsWith(PATTERN_QUOTE_DOUBLE)) == false ? PATTERN_QUOTE_DOUBLE : PATTERN_NOTHING) + ((index = unquoted.indexOf(PATTERN_MODEL_ALIAS_SEPARATOR)) > -1 && started == false && (unquoted.endsWith(PATTERN_QUOTE_DOUBLE) == false) ? unquoted.substring(0, index) + PATTERN_QUOTE_DOUBLE + PATTERN_MODEL_ALIAS_SEPARATOR + PATTERN_QUOTE_DOUBLE + unquoted.substring(index + PATTERN_MODEL_ALIAS_SEPARATOR.length()) : unquoted) + (unquoted.endsWith(PATTERN_QUOTE_DOUBLE) == false ? PATTERN_QUOTE_DOUBLE : PATTERN_NOTHING) : PATTERN_NOTHING);
    }

    public static String escape(String trapped, int level) {
        StringBuffer escaped;
        char defaultCharacter;
        char[] replacement;
        int index;
        int trappedLength;
        escaped = new StringBuffer();
        if (trapped != null) {
            if (level < 1) level = 1;
            trappedLength = trapped.length();
            for (index = 0; index < trappedLength; index++) {
                switch(defaultCharacter = trapped.charAt(index)) {
                    case '\\':
                        Arrays.fill(replacement = new char[level * 2], '\\');
                        escaped.append(replacement);
                        break;
                    case '\'':
                        Arrays.fill(replacement = new char[level], '\\');
                        escaped.append(replacement);
                        escaped.append(defaultCharacter);
                        break;
                    case '\n':
                        escaped.append((char) 1);
                        break;
                    default:
                        escaped.append(defaultCharacter);
                        break;
                }
            }
        }
        return (escaped.toString());
    }
}
