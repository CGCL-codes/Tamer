package net.sourceforge.jdefprog.dbc.mcl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.sourceforge.jdefprog.mcl.interpret.MethodId;
import net.sourceforge.jdefprog.mcl.interpret.MethodInfo;
import net.sourceforge.jdefprog.mcl.interpret.context.BufferTypeAccessContext;
import net.sourceforge.jdefprog.mcl.interpret.context.TypeAccessContext;
import net.sourceforge.jdefprog.mcl.interpret.context.TypeAccessContextFactory;
import net.sourceforge.jdefprog.mcl.interpret.types.MclType;
import net.sourceforge.jdefprog.mcl.interpret.types.TypeFactory;

public class ClientTypeAccessContextFactory implements TypeAccessContextFactory {

    private TypeFactory<Class<?>, Behavior> typeFactory;

    private static ClientTypeAccessContextFactory instance = null;

    public static ClientTypeAccessContextFactory getInstance() {
        if (instance == null) {
            instance = new ClientTypeAccessContextFactory(ClientTypeFactory.getInstance());
        }
        return instance;
    }

    private ClientTypeAccessContextFactory(TypeFactory<Class<?>, Behavior> typeFactory) {
        this.typeFactory = typeFactory;
    }

    private void add(BufferTypeAccessContext ctx, Field f) {
        MclType ft = this.typeFactory.get(f.getType());
        ctx.setIdent(f.getName(), ft);
    }

    private void add(BufferTypeAccessContext ctx, Behavior method) {
        MethodId id = new MethodId(method.getName(), typeFactory.getParamTypes(method));
        MclType returnType = typeFactory.get(method.getReturnType());
        MethodInfo info = new MethodInfo(returnType);
        ctx.setMethod(id, info);
    }

    @Override
    public TypeAccessContext getObjContext(MclType t) {
        BufferTypeAccessContext ctx = new BufferTypeAccessContext();
        Class<?> c = typeFactory.get(t);
        for (Method m : c.getMethods()) {
            add(ctx, new MethodBehavior(m));
        }
        for (Field f : c.getFields()) {
            add(ctx, f);
        }
        return ctx;
    }
}
