package pl.wcislo.sbql4j.tools.apt.mirror.declaration;

import pl.wcislo.sbql4j.mirror.declaration.AnnotationTypeDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationTypeElementDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationValue;
import pl.wcislo.sbql4j.mirror.util.DeclarationVisitor;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;

/**
 * Implementation of AnnotationTypeElementDeclaration
 */
public class AnnotationTypeElementDeclarationImpl extends MethodDeclarationImpl implements AnnotationTypeElementDeclaration {

    AnnotationTypeElementDeclarationImpl(AptEnv env, MethodSymbol sym) {
        super(env, sym);
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationTypeDeclaration getDeclaringType() {
        return (AnnotationTypeDeclaration) super.getDeclaringType();
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationValue getDefaultValue() {
        return (sym.defaultValue == null) ? null : new AnnotationValueImpl(env, sym.defaultValue, null);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(DeclarationVisitor v) {
        v.visitAnnotationTypeElementDeclaration(this);
    }
}
