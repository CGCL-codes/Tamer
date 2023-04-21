package org.sss.module.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.sss.module.rule.checker.ParseException;
import org.sss.module.rule.checker.TokenMgrError;

/**
 * 语法检查错误
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 397 $ $Date: 2009-05-25 12:06:36 -0400 (Mon, 25 May 2009) $
 */
public class CheckerException extends Exception {

    private Throwable throwable;

    private int line = -1;

    private int row = -1;

    public CheckerException(ParseException e) {
        throwable = e;
        line = e.currentToken.beginLine;
        row = e.currentToken.beginColumn;
    }

    public CheckerException(TokenMgrError e) {
        throwable = e;
    }

    public int getLine() {
        return line;
    }

    public int getRow() {
        return row;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        if (throwable != null) return throwable.fillInStackTrace();
        return super.fillInStackTrace();
    }

    @Override
    public Throwable getCause() {
        return throwable.getCause();
    }

    @Override
    public String getLocalizedMessage() {
        return throwable.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return throwable.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return throwable.getStackTrace();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return throwable.initCause(cause);
    }

    @Override
    public void printStackTrace() {
        throwable.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        throwable.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        throwable.printStackTrace(s);
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        throwable.setStackTrace(stackTrace);
    }

    @Override
    public String toString() {
        return throwable.toString();
    }
}
