package net.sourceforge.jdefprog.agent.processors.dbc.mcl;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.sourceforge.jdefprog.mcl.interpret.context.AccessContext;
import net.sourceforge.jdefprog.mcl.interpret.context.AccessContextFactory;
import net.sourceforge.jdefprog.mcl.interpret.context.BufferAccessContext;
import net.sourceforge.jdefprog.mcl.interpret.types.Type;
import net.sourceforge.jdefprog.mcl.interpret.values.Value;

public class AgentContextFactory implements AccessContextFactory {

    @Override
    public AccessContext getClassContext(Type t) {
        throw new RuntimeException("AgentContextFactory.getClassContext not implemented");
    }

    @Override
    public AccessContext getObjContext(Value v) {
        Type t = v.getType();
        try {
            CtClass cc = ClassPool.getDefault().get(t.getQualifiedName());
            BufferAccessContext bObj = new BufferAccessContext();
            CtMethod[] methods = cc.getMethods();
            for (CtMethod m : methods) {
                add(bObj, m);
            }
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void add(BufferAccessContext bObj, CtMethod method) {
    }
}
