package net.sourceforge.javautil.sourcecode.java.ast.expressions;

import net.sourceforge.javautil.grammar.impl.parser.pojo.Token;

/**
 * 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JavaString extends JavaExpressionAbstract {

    protected String literal;

    @Token(types = "STRINGLITERAL")
    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    @Override
    public StringBuilder append(StringBuilder builder) {
        return builder.append(literal);
    }
}
