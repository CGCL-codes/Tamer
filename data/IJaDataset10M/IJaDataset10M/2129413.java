package net.sourceforge.jdefprog.agent;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import net.sourceforge.jdefprog.agent.instrumentation.ClassModificationCache;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;

public abstract class BehaviorProcessor {

    private static Logger logger = Logger.getLogger(BehaviorProcessor.class.getCanonicalName());

    private Class<? extends Annotation> processedAnnotation;

    public BehaviorProcessor(Class<? extends Annotation> processedAnnotation) {
        this.processedAnnotation = processedAnnotation;
    }

    public final void process(CtBehavior method, ClassModificationCache cache) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        logger.finest("Processing " + method);
        Object[] annotations = method.getAnnotations();
        for (Object anno : annotations) {
            if (this.processedAnnotation.isInstance(anno)) {
                specificProcess(method, anno, cache);
            }
        }
    }

    public abstract void specificProcess(CtBehavior method, Object annotation, ClassModificationCache cache) throws NotFoundException, CannotCompileException;
}
