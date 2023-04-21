package org.ajcontract.annotationprocessor.ast.visitor.codegenerator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * <i>Element</i> that represents the return value
 * in order to allow transparent code generation. It
 * is useful to avoid meeding to compare between return
 * and non return types. 
 * 
 * @author Arcadio Rubio Garc�a
 */
public class ReturnValue implements Element {

    private static Elements elementUtils;

    private TypeMirror typeMirror;

    public static void setElementUtils(Elements newElementUtils) {
        elementUtils = newElementUtils;
    }

    public ReturnValue(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        throw new UnsupportedOperationException();
    }

    public TypeMirror asType() {
        return typeMirror;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        throw new UnsupportedOperationException();
    }

    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        throw new UnsupportedOperationException();
    }

    public List<? extends Element> getEnclosedElements() {
        throw new UnsupportedOperationException();
    }

    public Element getEnclosingElement() {
        throw new UnsupportedOperationException();
    }

    public ElementKind getKind() {
        return ElementKind.OTHER;
    }

    public Set<Modifier> getModifiers() {
        throw new UnsupportedOperationException();
    }

    public Name getSimpleName() {
        return elementUtils.getName("returnval");
    }
}
