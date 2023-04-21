package org.software.bird.som.exception;

/**
 * Class description goes here.
 *
 * @author <a href="mailto:cyyan@isoftstone.com">cyyan</a>
 * @version $Id: DateParseException.java,v0.1 2007-12-7 ����04:47:38 cyyan Exp$
 */
public class InvalidDateException extends BreakRuleException {

    private static final long serialVersionUID = -6555656755094186252L;

    public InvalidDateException(String string) {
        super(string);
    }
}
