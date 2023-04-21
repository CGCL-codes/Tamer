package org.plazmaforge.framework.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.UnmarshalException;
import java.sql.SQLException;
import java.text.MessageFormat;
import org.plazmaforge.framework.core.exception.AccessDeniedException;

/** 
 * @author Oleh Hapon
 * $Id: ErrorTranslator.java,v 1.5 2010/12/05 07:51:28 ohapon Exp $
 */
public class ErrorTranslator {

    private static ErrorTranslator instance;

    private IEntityManager entityManager;

    private String[][] violationForeignKeyArray;

    private ErrorTranslator() {
        violationForeignKeyArray = new String[3][2];
        violationForeignKeyArray[0][0] = "violation of FOREIGN KEY constraint";
        violationForeignKeyArray[0][1] = "on table";
        violationForeignKeyArray[1][0] = "Integrity constraint violation";
        violationForeignKeyArray[1][1] = "table:";
        violationForeignKeyArray[2][0] = "foreign key constraint fails";
        violationForeignKeyArray[2][1] = "(";
    }

    public static ErrorTranslator getInstance() {
        if (instance == null) {
            instance = new ErrorTranslator();
        }
        return instance;
    }

    public static Throwable getCause(Throwable t) {
        if (t == null) {
            return null;
        }
        Throwable clause = getInstance().getSQLException(t);
        if (clause != null) {
            return clause;
        }
        clause = getInstance().getAccessDeniedException(t);
        if (clause != null) {
            return clause;
        }
        clause = getInstance().getUndeclaredThrowableExceptionCause(t);
        if (clause != null) {
            return clause;
        }
        return t;
    }

    public static String getMessage(Throwable t) {
        return getInstance().translateError(t);
    }

    protected String translateError(Throwable t) {
        if (t == null) {
            return "";
        }
        SQLException sqle = getSQLException(t);
        if (sqle != null) {
            return translateSQLException(sqle);
        }
        String message = t.getMessage();
        if (message != null) {
            return message;
        }
        return t.toString();
    }

    protected String translateSQLException(SQLException e) {
        if (e == null) {
            return "";
        }
        String originalMessage = e.getMessage();
        return getImpossibleCommandMessage() + ": " + translateSQLMessage(originalMessage);
    }

    protected String translateSQLMessage(String originalMessage) {
        if (originalMessage == null) {
            return "";
        }
        for (int i = 0; i < violationForeignKeyArray.length; i++) {
            String constraintPrefix = violationForeignKeyArray[i][0];
            int index = originalMessage.indexOf(constraintPrefix);
            if (index < 0) {
                continue;
            }
            String mes = originalMessage.substring(index);
            String tablePrefix = violationForeignKeyArray[i][1];
            index = mes.indexOf(tablePrefix);
            if (index < 0) {
                return originalMessage;
            }
            String constraint = mes.substring(constraintPrefix.length(), index);
            String table = mes.substring(index + tablePrefix.length());
            if (table == null) {
                return originalMessage;
            }
            table = table.trim();
            index = table.indexOf(" ");
            if (index >= 0) {
                table = table.substring(0, index);
            }
            index = table.indexOf(",");
            if (index >= 0) {
                table = table.substring(0, index);
            }
            return translateViolationForeignKeyMessage(constraint, table);
        }
        return originalMessage;
    }

    public String translateViolationForeignKeyMessage(String constraint, String table) {
        MessageFormat format = new MessageFormat(getViolationForeignKeyUserMessage());
        Object[] args = new Object[2];
        args[0] = "\"" + getConstraintName(constraint) + "\"";
        args[1] = "\"" + getTableName(table) + "\"";
        return format.format(args);
    }

    protected String getConstraintName(String constraint) {
        return clearString(constraint);
    }

    public String getTableName(String table) {
        String clearString = clearString(table).toUpperCase();
        if (entityManager != null) {
            String str = entityManager.getEntityName(clearString);
            if (str == null || str.trim().length() == 0) {
                return clearString;
            }
            return str;
        }
        return clearString;
    }

    public boolean isBaseTable(String table) {
        if (entityManager == null) {
            return false;
        }
        return entityManager.isMasterEntity(table);
    }

    protected String clearString(String str) {
        if (str == null) {
            return "";
        }
        String s = str.trim();
        if (s.length() == 0) {
            return s;
        }
        int index = 0;
        if (s.charAt(index) == '\'' || s.charAt(index) == '\"' || s.charAt(index) == '`') {
            if (s.length() == 1) {
                return "";
            }
            s = s.substring(1);
        }
        index = s.length() - 1;
        if (s.charAt(index) == '\'' || s.charAt(index) == '\"' || s.charAt(index) == '`') {
            if (s.length() == 1) {
                return "";
            }
            s = s.substring(0, index);
        }
        index = s.indexOf('/');
        if (index > 0) {
            if (s.length() == 1) {
                return "";
            }
            index++;
            if (index < s.length()) {
                s = s.substring(index);
            }
        }
        return s;
    }

    protected SQLException getSQLException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof SQLException) {
            return (SQLException) t;
        }
        Throwable cause = t.getCause();
        if (cause == null) {
            return null;
        }
        if (cause instanceof SQLException) {
            return (SQLException) cause;
        }
        cause = cause.getCause();
        if (cause == null) {
            return null;
        }
        if (cause instanceof SQLException) {
            return (SQLException) cause;
        }
        return null;
    }

    protected AccessDeniedException getAccessDeniedException(Throwable t) {
        return AccessDeniedException.getException(t);
    }

    protected Throwable getUndeclaredThrowableExceptionCause(Throwable t) {
        if (t == null) {
            return null;
        }
        if (!(t instanceof UndeclaredThrowableException)) {
            return null;
        }
        Throwable cause = t.getCause();
        if (cause == null) {
            return t;
        }
        if (cause instanceof UnmarshalException) {
            cause = ((UnmarshalException) cause).getCause();
        }
        return cause;
    }

    protected String getDatabaseErrorMessage() {
        return Messages.getString("ErrorTranslator.database.error");
    }

    protected String getImpossibleCommandMessage() {
        return Messages.getString("ErrorTranslator.impossibleCommand");
    }

    protected String getViolationForeignKeySystemMessage() {
        return Messages.getString("ErrorTranslator.violation.foreign.key.system");
    }

    public String getViolationForeignKeyUserMessage() {
        return Messages.getString("ErrorTranslator.violation.foreign.key.user");
    }

    public static void setEntityManager(IEntityManager entityManager) {
        getInstance().entityManager = entityManager;
    }
}
