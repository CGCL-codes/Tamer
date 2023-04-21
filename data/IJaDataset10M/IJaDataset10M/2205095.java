package org.nightlabs.annotation.facade.apt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.nightlabs.annotation.facade.Facade;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class FacadeAnnotationProcessorFactory implements AnnotationProcessorFactory {

    @Override
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> annotationTypeDeclarations, AnnotationProcessorEnvironment annotationProcessorEnvironment) {
        if (annotationTypeDeclarations.isEmpty()) return AnnotationProcessors.NO_OP;
        return new FacadeAnnotationProcessor(annotationTypeDeclarations, annotationProcessorEnvironment);
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        Collection<String> supportedAnnotationTypes = new ArrayList<String>(1);
        supportedAnnotationTypes.add(Facade.class.getName());
        return supportedAnnotationTypes;
    }

    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
}
