package net.sf.jelly.apt.decorations.type;

import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.util.TypeVisitor;
import net.sf.jelly.apt.decorations.DeclarationDecorator;

/**
 * @author Ryan Heaton
 */
public class DecoratedAnnotationType extends DecoratedInterfaceType implements AnnotationType {

    public DecoratedAnnotationType(AnnotationType delegate) {
        super(delegate);
    }

    public AnnotationTypeDeclaration getDeclaration() {
        return DeclarationDecorator.decorate(((AnnotationType) delegate).getDeclaration());
    }

    public boolean isAnnotation() {
        return true;
    }

    public void accept(TypeVisitor v) {
        v.visitAnnotationType(this);
    }
}
