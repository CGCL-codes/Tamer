package net.sourceforge.jdefprog.agent.processors.cc;

import java.lang.annotation.Annotation;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.NotFoundException;
import net.sourceforge.jdefprog.agent.dbcconstraints.ClassConstraintsCollection;
import net.sourceforge.jdefprog.agent.processors.ParameterProcessor;
import net.sourceforge.jdefprog.cc.StrLen;
import net.sourceforge.jdefprog.mcl.interpret.MclConstants;
import net.sourceforge.jdefprog.mcl.interpret.context.builders.ParamNames;

public class StrLenInParamProcessor extends ParameterProcessor {

    public StrLenInParamProcessor() {
        super(StrLen.class);
    }

    @Override
    public void specificProcess(CtBehavior method, int paramIndex, Annotation annotation, ClassConstraintsCollection constraints) throws NotFoundException, CannotCompileException {
        StrLen strLen = (StrLen) annotation;
        String code = "";
        if (!strLen.nullAllowed()) {
            code = ParamNames.name(paramIndex) + "!=" + MclConstants.NULL_REF + " && " + ParamNames.name(paramIndex) + ".length()>=" + strLen.min();
            if (strLen.max() != StrLen.NO_MAX_LIMIT) {
                code += " && " + ParamNames.name(paramIndex) + ".length()<=" + strLen.max();
            }
        } else {
            code = ParamNames.name(paramIndex) + "=" + MclConstants.NULL_REF + " || (" + ParamNames.name(paramIndex) + ".length()>=" + strLen.min();
            if (strLen.max() != StrLen.NO_MAX_LIMIT) {
                code += " && " + ParamNames.name(paramIndex) + ".length()<=" + strLen.max();
            }
            code += ")";
        }
        this.addPre(method, paramIndex, annotation, constraints, code);
    }
}
