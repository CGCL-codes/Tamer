package org.yuanheng.cookcc.exception;

import java.text.MessageFormat;

/**
 * @author Heng Yuan
 * @version $Id: EscapeSequenceException.java 743 2012-03-17 06:53:00Z superduperhengyuan@gmail.com $
 */
public class EscapeSequenceException extends RuntimeException {

    public static MessageFormat ERROR_MSG = new MessageFormat("invalid escape sequence {0}");

    private String m_esc;

    public EscapeSequenceException(String esc) {
        m_esc = esc;
    }

    @Override
    public String toString() {
        return ERROR_MSG.format(new String[] { m_esc });
    }
}
