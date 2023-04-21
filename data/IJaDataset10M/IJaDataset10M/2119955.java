package org.yuanheng.cookcc.exception;

import java.text.MessageFormat;

/**
 * @author Heng Yuan
 * @version $Id: CookCCException.java 743 2012-03-17 06:53:00Z superduperhengyuan@gmail.com $
 */
public class CookCCException extends RuntimeException {

    public static MessageFormat ERROR_MSG = new MessageFormat("Error on line {0}: {1}.");

    private final int m_lineNumber;

    private final String m_msg;

    public CookCCException(int lineNumber, String msg) {
        m_lineNumber = lineNumber;
        m_msg = msg;
    }

    public int getLineNumber() {
        return m_lineNumber;
    }

    @Override
    public String toString() {
        Object[] objs = new Object[] { new Integer(m_lineNumber), m_msg };
        return ERROR_MSG.format(objs);
    }
}
