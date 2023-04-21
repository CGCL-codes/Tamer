package net.sourceforge.jdefprog.agent.processors.cc;

import java.util.logging.Logger;
import net.sourceforge.jdefprog.cc.Pos;
import net.sourceforge.jdefprog.agent.dbcconstraints.ClassConstraintsCollection;
import net.sourceforge.jdefprog.agent.instrumentation.ClassModificationCache;
import net.sourceforge.jdefprog.agent.instrumentation.InstrumentationHelper;
import net.sourceforge.jdefprog.agent.processors.ParameterProcessor;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;

public class PosProcessor extends ParameterProcessor {

    private static Logger logger = Logger.getLogger(PosProcessor.class.getCanonicalName());

    public PosProcessor() {
        super(Pos.class);
    }

    @Override
    public void specificProcess(CtBehavior behavior, int paramIndex, Object annotation, ClassModificationCache cache, ClassConstraintsCollection c) throws NotFoundException, CannotCompileException {
        logger.finest("Parameter annotation processing in " + PosProcessor.class.getCanonicalName());
        String paramName = InstrumentationHelper.parameterNameFor(behavior, paramIndex);
        Pos pos = (Pos) annotation;
        String comparationSign = pos.zero() ? ">=" : ">";
        cache.insertBefore(behavior, "if (!($" + (paramIndex + 1) + comparationSign + "0)) throw new IllegalArgumentException(\"parameter " + paramName + " should be positive (" + comparationSign + "0)\");");
    }
}
