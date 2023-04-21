package net.sourceforge.jdefprog.agent.cc;

import java.util.logging.Logger;
import net.sourceforge.jdefprog.agent.ParameterProcessor;
import net.sourceforge.jdefprog.agent.instrumentation.ClassModificationCache;
import net.sourceforge.jdefprog.agent.instrumentation.InstrumentationHelper;
import net.sourceforge.jdefprog.cc.IRange;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;

public class IRangeProcessor extends ParameterProcessor {

    private static Logger logger = Logger.getLogger(IRangeProcessor.class.getCanonicalName());

    public IRangeProcessor() {
        super(IRange.class);
    }

    @Override
    public void specificProcess(CtBehavior behavior, int paramIndex, Object annotation, ClassModificationCache cache) throws NotFoundException, CannotCompileException {
        logger.finest("Parameter annotation processing in " + IRangeProcessor.class.getCanonicalName());
        IRange iRange = (IRange) annotation;
        String paramName = InstrumentationHelper.parameterNameFor(behavior, paramIndex);
        if ("int".equals(behavior.getParameterTypes()[paramIndex].getSimpleName())) {
            long intMax = Integer.MAX_VALUE;
            if (iRange.max() < intMax) {
                cache.insertBefore(behavior, "if ($" + (paramIndex + 1) + ">" + iRange.max() + ") throw new IllegalArgumentException(\"parameter " + paramName + " should be equal or lower than " + iRange.max() + "\");");
            }
            long intMin = Integer.MIN_VALUE;
            if (iRange.min() > intMin) {
                cache.insertBefore(behavior, "if ($" + (paramIndex + 1) + "<" + iRange.min() + ") throw new IllegalArgumentException(\"parameter " + paramName + " should be equal or greater than " + iRange.min() + "\");");
            }
        } else {
            cache.insertBefore(behavior, "if ($" + (paramIndex + 1) + "<" + iRange.min() + ") throw new IllegalArgumentException(\"parameter " + paramName + " should be equal or greater than " + iRange.min() + "\");");
            cache.insertBefore(behavior, "if ($" + (paramIndex + 1) + ">" + iRange.max() + ") throw new IllegalArgumentException(\"parameter " + paramName + " should be equal or lower than " + iRange.max() + "\");");
        }
    }
}
