package net.sf.etl.tests.term_parser.beans;

import net.sf.etl.parsers.TermToken;
import net.sf.etl.parsers.beans.TokenCollector;

/**
 * Let statement
 * 
 * @author const
 * 
 */
public class LetStatement extends Statement implements TokenCollector {

    /**
	 * serial version id
	 */
    private static final long serialVersionUID = -8827443002800538040L;

    /** name of value */
    String name;

    /** value */
    Expression value;

    /** the text of the statement */
    private StringBuilder text = new StringBuilder();

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the value.
	 */
    public Expression getValue() {
        return value;
    }

    /**
	 * @param value
	 *            The value to set.
	 */
    public void setValue(Expression value) {
        this.value = value;
    }

    /**
	 * @return the text of the statement
	 */
    public String statementText() {
        return text.toString();
    }

    /** {@inheritDoc} */
    public void collect(TermToken token) {
        if (token.hasLexicalToken()) {
            text.append(token.token().token().text());
        }
    }
}
