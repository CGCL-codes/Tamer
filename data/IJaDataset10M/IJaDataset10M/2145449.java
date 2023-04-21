package com.agimatec.dbmigrate.util;

import com.agimatec.sql.script.SQLScriptParser;
import com.agimatec.sql.script.ScriptVisitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * <b>Description:</b>   visitor able to invoke subscripts
 * with oracle @ syntax<br>
 * <b>Copyright:</b>     Copyright (c) 2007<br>
 * <b>Company:</b>       Agimatec GmbH<br>
 *
 * @author Roman Stumm
 */
public class SubscriptCapableVisitor extends ScriptVisitorDelegate {

    private static final Log log = LogFactory.getLog(SubscriptCapableVisitor.class);

    private final SQLScriptParser parser;

    public SubscriptCapableVisitor(ScriptVisitor nextVisitor, SQLScriptParser parser) {
        super(nextVisitor);
        this.parser = parser;
    }

    public int visitStatement(String statement) throws SQLException {
        if (statement.startsWith("@")) {
            try {
                if (statement.charAt(1) == '>') {
                    parser.execSQLScript(this, statement.substring(2));
                } else {
                    parser.iterateSQLScript(this, statement.substring(1));
                }
            } catch (Exception e) {
                log.error("error executing subscript: " + statement.substring(1), e);
                throw new SQLException(e.getMessage(), e);
            }
            return 0;
        } else if (statement.length() > 5 && statement.substring(0, 4).toLowerCase().equals("set ")) {
            doSetExpression(statement.substring(4));
            return 0;
        } else {
            return super.visitStatement(statement);
        }
    }

    private void doSetExpression(String expression) {
        StringTokenizer tokens = new StringTokenizer(expression, "=,; ", true);
        String varName = nextToken(tokens, expression);
        while (tokens.hasMoreTokens()) {
            String nt = nextToken(tokens, expression);
            if (!"=".equals(nt) && !" ".equals(nt)) {
                log.warn("Illegal operator, expected '=' in: " + expression);
                return;
            } else if ("=".equals(nt)) break;
        }
        String value = nextToken(tokens, expression);
        if (varName == null || value == null) return;
        if (varName.equals("FAIL_ON_ERROR")) {
            boolean bool = Boolean.parseBoolean(value);
            log.info("SET " + varName + "=" + bool + ";");
            parser.setFailOnError(bool);
        } else {
            log.warn("Illegal script set-option: " + expression);
        }
    }

    private String nextToken(StringTokenizer tokens, String expression) {
        if (!tokens.hasMoreTokens()) {
            log.warn("Illegal script set-option: " + expression);
            return null;
        }
        return tokens.nextToken();
    }
}
