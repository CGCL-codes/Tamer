package javax.faces.el;

import javax.el.ValueExpression;

/**
 * see Javadoc of <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/index.html">JSF Specification</a>
 */
public interface CompositeComponentExpressionHolder {

    public ValueExpression getExpression(String name);
}
