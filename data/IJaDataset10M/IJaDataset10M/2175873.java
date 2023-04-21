package prop4j.parser;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.Type;

/**
 *
 * @author mike
 */
public abstract class AccessorNode extends Node {

    protected String propertyName;

    protected MethodDeclaration method;

    protected AccessorNode(MethodDeclaration method) {
        this.method = method;
        char[] c = new char[method.name.length() - 3];
        c[0] = Character.toLowerCase(method.name.charAt(3));
        for (int i = 1; i < c.length; i++) {
            c[i] = method.name.charAt(i + 3);
        }
        propertyName = new String(c);
    }

    protected AccessorNode(String propertyName, String fieldName, Type propertyType) {
        this.propertyName = propertyName;
        this.method = buildMethodDeclaration(propertyName, fieldName, propertyType);
    }

    protected abstract MethodDeclaration buildMethodDeclaration(String propertyName, String fieldName, Type propertyType);

    public String getPropertyName() {
        return propertyName;
    }

    protected MethodDeclaration getMethodDeclaration() {
        return method;
    }
}
