package engine.cmd.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import engine.RequireCommand;
import engine.Symbol;
import engine.MethodInvocation;
import engine.error.CompileVExcexption;
import engine.error.VException;
import engine.expr.AbstractBlock;

public class _importstatic extends RequireCommand {

    @Override
    public void runCompile(final AbstractBlock parent, final String name) throws CompileVExcexption {
        try {
            final Class<?> c = Class.forName(name);
            for (final Method m : c.getMethods()) {
                if (!Modifier.isStatic(m.getModifiers())) continue;
                final MethodInvocation meth = new MethodInvocation(c, m);
                parent.putVar(m.getName(), meth);
            }
            for (final Field f : c.getFields()) {
                if (!(Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))) continue;
                try {
                    parent.putVar(f.getName(), new Symbol(f.get(null)));
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (final ClassNotFoundException cnf) {
            throw new VException("Class not found");
        }
    }
}
