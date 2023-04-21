package net.sourceforge.jdefprog.agent.processors;

import java.lang.annotation.Annotation;
import javassist.CannotCompileException;
import javassist.CtField;
import javassist.NotFoundException;
import net.sourceforge.jdefprog.agent.dbcconstraints.ClassConstraintsCollection;

public abstract class FieldProcessor {

    private Class<? extends Annotation> processedAnnotation;

    public FieldProcessor(Class<? extends Annotation> processedAnnotation) {
        this.processedAnnotation = processedAnnotation;
    }

    public final void process(CtField field, Annotation anno, ClassConstraintsCollection constraints) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        if (this.processedAnnotation.isInstance(anno)) {
            specificProcess(field, anno, constraints);
        }
    }

    public abstract void specificProcess(CtField field, Object annotation, ClassConstraintsCollection constraints) throws NotFoundException, CannotCompileException;
}
