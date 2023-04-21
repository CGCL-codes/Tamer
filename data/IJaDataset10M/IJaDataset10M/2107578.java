package net.sourceforge.javautil.sourcecode.java.ast;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.javautil.grammar.impl.parser.pojo.Token;
import net.sourceforge.javautil.grammar.impl.parser.pojo.TokenComposite;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@TokenComposite(prefixes = "<", suffixes = ">")
public class JavaParameterizedList {

    protected final List<JavaGeneric> genericTypes = new ArrayList<JavaGeneric>();

    @Token(separator = ",")
    public List<JavaGeneric> getGenericTypes() {
        return genericTypes;
    }

    public StringBuilder append(StringBuilder builder) {
        builder.append("<");
        for (int i = 0; i < genericTypes.size(); i++) {
            if (i > 0) builder.append(", ");
            genericTypes.get(i).append(builder);
        }
        return builder.append(">");
    }

    public String toString() {
        return append(new StringBuilder()).toString();
    }
}
