package net.sourceforge.jdefprog.agent;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import net.sourceforge.jdefprog.agent.instrumentation.ClassModificationCache;
import net.sourceforge.jdefprog.agent.instrumentation.InstrumentationHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;

public abstract class ParameterProcessor {

    private static Logger logger = Logger.getLogger(BehaviorProcessor.class.getCanonicalName());

    private Class<? extends Annotation> processedAnnotation;

    public ParameterProcessor(Class<? extends Annotation> processedAnnotation) {
        this.processedAnnotation = processedAnnotation;
    }

    public Class<? extends Annotation> getProcessedAnnotation() {
        return processedAnnotation;
    }

    public void process(CtBehavior method, int paramIndex, Object annotation, ClassModificationCache cache) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        logger.finest("Processing " + method);
        specificProcess(method, paramIndex, annotation, cache);
    }

    public abstract void specificProcess(CtBehavior method, int paramIndex, Object annotation, ClassModificationCache cache) throws NotFoundException, CannotCompileException;
}
