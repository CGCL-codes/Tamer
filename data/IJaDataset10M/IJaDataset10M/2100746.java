package net.sourceforge.javautil.sourcecode.java.ast.types.members;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.javautil.grammar.impl.parser.pojo.Token;
import net.sourceforge.javautil.grammar.impl.parser.pojo.TokenComposite;
import net.sourceforge.javautil.sourcecode.java.ast.expressions.IJavaResult;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@TokenComposite(prefixes = "(", suffixes = ")")
public class JavaArguments {

    protected final List<IJavaResult> arguments = new ArrayList<IJavaResult>();

    /**
	 * @return The parameters in this parameter list
	 */
    @Token(required = false, separator = ",")
    public List<IJavaResult> getParameters() {
        return arguments;
    }

    public String toString() {
        return this.append(new StringBuilder()).toString();
    }

    public StringBuilder append(StringBuilder builder) {
        builder.append("(");
        for (int p = 0; p < arguments.size(); p++) {
            if (p > 0) builder.append(", ");
            arguments.get(p).append(builder);
        }
        builder.append(")");
        return builder;
    }
}
