package net.sourceforge.javautil.sourcecode.java.ast;

import net.sourceforge.javautil.grammar.impl.parser.pojo.Token;
import net.sourceforge.javautil.grammar.impl.parser.pojo.TokenComposite;
import net.sourceforge.javautil.grammar.lexer.GrammarTokenType;
import net.sourceforge.javautil.sourcecode.java.ast.types.IJavaGenericType;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@TokenComposite(concrete = { JavaParameterizedType.class, JavaParameterizedWildcard.class })
public abstract class JavaParameterizedVariable extends JavaParameterization {

    protected JavaParameterExtension extension;

    @Token(order = 1, required = false)
    public JavaParameterExtension getExtension() {
        return extension;
    }

    public void setExtension(JavaParameterExtension extension) {
        this.extension = extension;
    }

    /**
	 * 
	 * 
	 * @author elponderador
	 * @author $Author$
	 * @version $Id$
	 */
    public static class JavaParameterExtension {

        protected String extensionType;

        protected JavaParameterizedName extensionName;

        @Token(order = 0, pattern = "(super|extends)")
        public String getExtensionType() {
            return extensionType;
        }

        public void setExtensionType(String extensionType) {
            this.extensionType = extensionType;
        }

        @Token(order = 1)
        public JavaParameterizedName getExtensionName() {
            return extensionName;
        }

        public void setExtensionName(JavaParameterizedName extensionName) {
            this.extensionName = extensionName;
        }

        public String toString() {
            return this.append(new StringBuilder()).toString();
        }

        public StringBuilder append(StringBuilder builder) {
            builder.append(extensionType).append(" ");
            extensionName.append(builder);
            return builder;
        }
    }
}
