package org.jmlspecs.jml6.core.ast.valueobject;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jmlspecs.jml6.core.ast.JmlModelMethod;

public class JmlModelMethodValueObject extends JmlAstNodeValueObject {

    private String method;

    private String name;

    public JmlModelMethodValueObject(JmlModelMethod modelMethod) {
        MethodDeclaration md = modelMethod.getMethodDeclaration();
        method = toJIR(md);
        name = modelMethod.getName().toString();
    }

    public String getName() {
        return name;
    }

    public String getMethodDeclaration() {
        return method;
    }

    public String getMethodName() {
        return name;
    }
}
