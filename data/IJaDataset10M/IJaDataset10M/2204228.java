package net.sourceforge.jdefprog.agent.processors.dbc.mcl;

import java.util.logging.Logger;
import javassist.*;
import net.sourceforge.jdefprog.mcl.interpret.types.PrimitiveMclType;
import net.sourceforge.jdefprog.mcl.interpret.types.MclType;
import net.sourceforge.jdefprog.mcl.interpret.types.TypeFactory;

public class AgentTypeFactory implements TypeFactory<CtClass, CtBehavior> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AgentTypeFactory.class.getCanonicalName());

    @Override
    public MclType get(CtClass cc) {
        if (cc.isPrimitive()) {
            if (cc.getName().equals(int.class.getCanonicalName())) {
                return PrimitiveMclType.INT;
            }
            if (cc.getName().equals(long.class.getCanonicalName())) {
                return PrimitiveMclType.LONG;
            }
            if (cc.getName().equals(char.class.getCanonicalName())) {
                return PrimitiveMclType.CHAR;
            }
            if (cc.getName().equals(boolean.class.getCanonicalName())) {
                return PrimitiveMclType.BOOLEAN;
            }
        } else if (!cc.isArray()) {
            return MclType.createDeclared(cc.getName());
        }
        throw new RuntimeException("AgentTypeFactory.get not implemented for " + cc);
    }

    @Override
    public MclType[] getParamTypes(CtBehavior ee) {
        CtClass[] classes;
        try {
            classes = ee.getParameterTypes();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        MclType[] types = new MclType[classes.length];
        for (int i = 0; i < classes.length; i++) {
            types[i] = this.get(classes[i]);
        }
        return types;
    }

    @Override
    public CtClass get(MclType t) {
        try {
            CtClass cc = ClassPool.getDefault().get(t.getQualifiedName());
            return cc;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
