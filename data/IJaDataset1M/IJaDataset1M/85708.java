package net.sf.jdpa.core;

import net.sf.jdpa.lang.Element;
import net.sf.jdpa.lang.TypeElement;
import net.sf.jdpa.Instrumenter;
import java.lang.annotation.Annotation;
import java.io.Serializable;

/**
 * @author Andreas Nilsson
 */
public class AddInterfaceInstrumenter implements Instrumenter {

    public void instrument(Element element, Annotation annotation) {
        TypeElement classElement = (TypeElement) element;
        classElement.addInterface(Serializable.class);
    }
}
