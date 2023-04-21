package net.sourceforge.jdefprog.agent.processors.dbc;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import net.sourceforge.jdefprog.agent.dbcconstraints.ClassConstraintsCollection;
import net.sourceforge.jdefprog.agent.dbcconstraints.PreConstraint;
import net.sourceforge.jdefprog.agent.dbcconstraints.RelativeBehaviorId;
import net.sourceforge.jdefprog.agent.processors.BehaviorProcessor;
import net.sourceforge.jdefprog.dbc.Pre;

public class PreProcessor extends BehaviorProcessor {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(PreProcessor.class.getCanonicalName());

    public PreProcessor() {
        super(Pre.class);
    }

    @Override
    public void specificProcess(CtBehavior method, Annotation annotation, ClassConstraintsCollection constraints) throws NotFoundException, CannotCompileException {
        Pre pre = (Pre) annotation;
        constraints.addPre(RelativeBehaviorId.create(method), new PreConstraint(pre.value(), "Pre-condition on " + method.getLongName()));
    }
}
