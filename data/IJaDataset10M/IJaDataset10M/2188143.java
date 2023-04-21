package jaxlib.lang.javac;

import java.util.Set;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import jaxlib.lang.First;
import jaxlib.lang.model.AbstractAnnotationProcessor;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: FirstProcessor.java 3002 2011-10-18 00:11:08Z joerg_wassmer $
 */
@SupportedAnnotationTypes("jaxlib.lang.First")
public final class FirstProcessor extends AbstractAnnotationProcessor {

    public FirstProcessor() {
        super();
    }

    private void visitMethod(final ExecutableElement subMethod) {
        if (subMethod.getModifiers().contains(Modifier.PRIVATE)) {
            warning("Private method annotated by " + First.class, subMethod);
        } else {
            final TypeElement subClass = (TypeElement) subMethod.getEnclosingElement();
            if (subClass.getKind() != ElementKind.CLASS) {
                error("Method annotated by " + First.class + " inside interface or annotation type", subMethod);
                return;
            }
            TypeElement classElement = subClass;
            for (TypeMirror superClass = classElement.getSuperclass(); superClass.getKind() != TypeKind.NONE; superClass = classElement.getSuperclass()) {
                classElement = (TypeElement) this.typeUtil.asElement(superClass);
                for (final Element superElement : classElement.getEnclosedElements()) {
                    if ((superElement.getKind() == ElementKind.METHOD) && this.elementUtil.overrides(subMethod, (ExecutableElement) superElement, subClass)) {
                        error("Method annotated by " + First.class.getName() + " overrides method " + superClass + "." + superElement, subMethod);
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected final boolean processImpl(final Set<? extends TypeElement> annotations) {
        for (final Element e : this.roundEnv.getElementsAnnotatedWith(getTypeElementOrError(First.class.getName()))) {
            visitMethod((ExecutableElement) e);
        }
        return true;
    }
}
