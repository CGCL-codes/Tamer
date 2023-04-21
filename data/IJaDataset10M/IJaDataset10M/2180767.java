package net.sourceforge.jdefprog.agent.processors.cc;

import java.util.logging.Logger;
import net.sourceforge.jdefprog.agent.dbcconstraints.ClassConstraintsCollection;
import net.sourceforge.jdefprog.agent.instrumentation.ClassModificationCache;
import net.sourceforge.jdefprog.agent.instrumentation.InstrumentationHelper;
import net.sourceforge.jdefprog.agent.processors.ParameterProcessor;
import net.sourceforge.jdefprog.cc.Nn;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;

public class NnProcessor extends ParameterProcessor {

    private static Logger logger = Logger.getLogger(NnProcessor.class.getCanonicalName());

    public NnProcessor() {
        super(Nn.class);
    }

    @Override
    public void specificProcess(CtBehavior behavior, int paramIndex, Object annotation, ClassModificationCache cache, ClassConstraintsCollection c) throws NotFoundException, CannotCompileException {
        logger.finest("Parameter annotation processing in " + NnProcessor.class.getCanonicalName());
        String paramName = InstrumentationHelper.parameterNameFor(behavior, paramIndex);
        cache.insertBefore(behavior, "if ($" + (paramIndex + 1) + "==null) throw new IllegalArgumentException(\"parameter " + paramName + " should be not null\");");
    }
}
