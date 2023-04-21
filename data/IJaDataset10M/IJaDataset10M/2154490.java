package org.stars.dao.sqlmapper.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stars.config.Config;
import org.stars.dao.exception.parser.BracketNotBalancedException;
import org.stars.dao.exception.parser.MacroNotDefinedException;
import org.stars.dao.sqlmapper.SqlBase;
import org.stars.dao.sqlmapper.SqlFragment;
import org.stars.dao.sqlmapper.SqlMacro;
import org.stars.dao.sqlmapper.macro.Macro;
import org.stars.daostars.exceptions.parser.MalformedSqlException;
import org.stars.datastructure.stack.Stack;
import static org.stars.config.Message.MALFORMED_SQL_ERROR;
import static org.stars.config.Message.MACRO_NOT_DEFINED_ERROR;

public class SubParser04_MacroReplacer {

    protected static final String MACRO_START = "@[";

    protected static final String MACRO_END = "]";

    protected static final String SUBQUERY_START = "{";

    protected static final String SUBQUERY_END = "}";

    protected static final char PARAMETER_START = '(';

    protected static final char PARAMETER_END = ')';

    protected static final String COMMA = ",";

    protected static final String QUOTE = "'";

    protected static final String DOUBLE_QUOTE = "\"";

    /**
	 * Create the sql parts and check the macro.
	 * 
	 * @param sqlBase
	 * @param macroMap
	 */
    public static void execute(String currentId, SqlBase sqlBase) {
        List<SqlFragment> result = new ArrayList<SqlFragment>();
        result = subdivide(currentId, sqlBase);
        sqlBase.setParts(result);
    }

    /**
	 * Subdivide the sql code in fragment.
	 * 
	 * @param sqlBase
	 *            the sql base
	 * @param macroMap
	 * @param result
	 *            the result
	 * @param currentStart
	 *            the current start
	 * @return the list
	 */
    protected static List<SqlFragment> subdivide(String originalSqlName, SqlBase sqlBase) {
        Map<String, Macro> macroMap = Config.getDaoConfig().getMacroMap();
        Log log = LogFactory.getLog(SubParser04_MacroReplacer.class);
        List<SqlFragment> result = new ArrayList<SqlFragment>();
        String sql = sqlBase.getTemplateText();
        boolean exit = false;
        String buffer;
        SqlFragment fragment;
        SqlMacro macroDef;
        SubParser04_Result tempResult;
        int currentStart = 0;
        if (sql == null) return result;
        int index1 = sql.indexOf(MACRO_START, 0);
        int index2;
        int indexNext;
        if (index1 == -1) {
            return result;
        }
        while (!exit) {
            if (index1 == -1 || index1 < currentStart) {
                index1 = sql.length();
                exit = true;
            }
            buffer = sql.substring(currentStart, index1);
            if (buffer.length() > 0) {
                fragment = new SqlFragment();
                fragment.setSqlMapper(sqlBase.getSqlMapper());
                fragment.setTemplateText(buffer);
                result.add(fragment);
            }
            if (!exit) {
                tempResult = find(sql, '[', index1 + 1);
                buffer = tempResult.buffer;
                currentStart = tempResult.lastIndex;
                macroDef = new SqlMacro("", "");
                macroDef.setSqlMapper(sqlBase.getSqlMapper());
                macroDef.setTemplateArgs(buffer);
                result.add(macroDef);
                indexNext = sql.indexOf(MACRO_START, currentStart);
                index2 = sql.indexOf(SUBQUERY_START, currentStart);
                if ((index2 > 0 && indexNext == -1) || (index2 < indexNext)) {
                    buffer = sql.substring(currentStart, index2);
                    if (buffer.trim().length() != 0) {
                        log.error(MALFORMED_SQL_ERROR.with(sqlBase.getId()));
                        throw (new MalformedSqlException(MALFORMED_SQL_ERROR.with(sqlBase.getId())));
                    }
                    tempResult = find(sql, '{', index2);
                    buffer = tempResult.buffer;
                    currentStart = tempResult.lastIndex;
                    macroDef.setTemplateText(buffer);
                }
                index1 = indexNext;
            }
        }
        Macro foundMacro;
        for (SqlFragment item : result) {
            if (item instanceof SqlMacro) {
                macroDef = (SqlMacro) item;
                createMacroIdAndArgs(macroDef);
                System.out.println("MACRO ID=" + macroDef.getId());
                foundMacro = macroMap.get(macroDef.getId());
                if (foundMacro == null) {
                    Macro macroError = macroMap.get("error");
                    macroDef.setType(macroError);
                    throw (new MacroNotDefinedException(MACRO_NOT_DEFINED_ERROR.with(macroDef.getId(), sqlBase.getSqlMapper().getId(), originalSqlName)));
                }
                macroDef.setType(foundMacro);
                List<SqlFragment> sub = subdivide(originalSqlName, macroDef);
                if (sub.size() > 0) {
                    macroDef.setParts(sub);
                }
            }
        }
        return result;
    }

    private static SubParser04_Result find(String s, char base, int startIndex) {
        SubParser04_Result result = new SubParser04_Result();
        boolean apiceAperto = false;
        Stack<Character> stack = new Stack<Character>();
        char lastInStack;
        char current;
        int idx;
        stack.push(base);
        for (idx = startIndex + 1; idx < s.length() && !stack.empty(); idx++) {
            current = s.charAt(idx);
            if (!apiceAperto) {
                if (current == '\'' || current == '"' || current == '[' || current == '(' || current == '{') {
                    stack.push(current);
                    if (current == '\'' || current == '"') {
                        apiceAperto = true;
                    }
                } else if (current == ']' || current == ')' || current == '}') {
                    lastInStack = stack.peek();
                    if ((current == ']' && lastInStack == '[') || (current == '}' && lastInStack == '{') || (current == ')' && lastInStack == '(')) {
                        stack.pop();
                    } else {
                        throw (new BracketNotBalancedException("SqlMapper " + ": error during parsing of parenthesis (,[,{. The number of the opened parenthesis is different from the number of the closed parenthesis."));
                    }
                }
            } else {
                lastInStack = stack.peek();
                if (current == '\'' || current == '"') {
                    if (lastInStack == current) {
                        stack.pop();
                        apiceAperto = false;
                    }
                }
            }
        }
        result.buffer = s.substring(startIndex + 1, idx - 1);
        result.lastIndex = idx;
        return result;
    }

    /**
	 * Split the input string creating list of parameters. Parameters are
	 * separated by comma. Comma in " or in ' are not considered.
	 * 
	 * @param s
	 * @param start
	 * @return
	 */
    private static List<String> splitParameters(String s, char start) {
        List<String> ret = new ArrayList<String>();
        boolean apiceAperto = false;
        Stack<Character> stack = new Stack<Character>();
        char lastInStack;
        char current;
        int idx;
        int startArg = 0;
        for (idx = 0; idx < s.length(); idx++) {
            current = s.charAt(idx);
            if (!apiceAperto) {
                if (current == '\'' || current == '"' || current == '[' || current == '(' || current == '{') {
                    stack.push(current);
                    if (current == '\'' || current == '"') {
                        apiceAperto = true;
                    }
                } else if (current == ']' || current == ')' || current == '}') {
                    lastInStack = stack.peek();
                    if ((current == ']' && lastInStack == '[') || (current == '}' && lastInStack == '{') || (current == ')' && lastInStack == '(')) {
                        stack.pop();
                    } else {
                        throw (new BracketNotBalancedException("SqlMapper " + ": error during parsing of parenthesis (,[,{. The number of the opened parenthesis is different from the number of the closed parenthesis."));
                    }
                } else if (current == ',') {
                    ret.add(s.substring(startArg, idx));
                    startArg = idx + 1;
                }
            } else {
                lastInStack = stack.peek();
                if (current == '\'' || current == '"') {
                    if (lastInStack == current) {
                        stack.pop();
                        apiceAperto = false;
                    }
                }
            }
        }
        ret.add(s.substring(startArg));
        return ret;
    }

    /**
	 * Parse macro args template and define id and args
	 * 
	 * @param macro
	 */
    protected static void createMacroIdAndArgs(SqlMacro macro) {
        String buffer = macro.getTemplateArgs();
        String temp;
        int n = buffer.indexOf(PARAMETER_START);
        int nLast = 0;
        String name;
        if (n == -1) {
            name = buffer.trim();
            macro.setId(name);
        } else {
            name = buffer.substring(0, n).trim();
            macro.setId(name);
            nLast = buffer.lastIndexOf(PARAMETER_END);
            temp = buffer.substring(n + 1, nLast);
            List<String> listParams = splitParameters(temp, PARAMETER_START);
            macro.setArgs(listParams);
        }
    }
}
