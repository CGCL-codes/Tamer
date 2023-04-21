package org.antlr.stringtemplate.language;

import antlr.CommonToken;
import java.util.List;

public class StringTemplateToken extends CommonToken {

    /** Track any args for anonymous templates like
	 *  <tokens,rules:{t,r | <t> then <r>}>
	 *  The lexer in action.g returns a single token ANONYMOUS_TEMPLATE
	 *  and so I need to have it parse args in the lexer and make them
	 *  available for when I build the anonymous template.
	 */
    public List args;

    public StringTemplateToken() {
        super();
    }

    public StringTemplateToken(int type, String text) {
        super(type, text);
    }

    public StringTemplateToken(String text) {
        super(text);
    }

    public StringTemplateToken(int type, String text, List args) {
        super(type, text);
        this.args = args;
    }

    public String toString() {
        return super.toString() + "; args=" + args;
    }
}
