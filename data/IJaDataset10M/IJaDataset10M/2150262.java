package org.jfeature.fpi.apt;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.jfeature.fpi.FPProducible;

public interface APProducible extends FPProducible<TypeMirror, ExecutableElement, TypeElement, AnnotationMirror, APTMethodHandler> {
}
