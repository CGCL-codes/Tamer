package bee;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Environment {

    private Environment parent;

    private Map locals;

    private Collection imports;

    private Callable get, set, dir;

    private boolean inline;

    public Environment() {
        this(null, false);
    }

    public Environment(Environment parent) {
        this(parent, false);
    }

    public Environment(Environment parent, boolean inline) {
        this.locals = new HashMap(8);
        this.parent = parent;
        this.inline = inline;
        this.imports = null;
    }

    public synchronized Collection getVariableNames(EvalContext ctx) throws EvalException, InternalEvalException {
        if (dir == null) return locals.keySet();
        ArrayList keys = new ArrayList(locals.keySet());
        keys.addAll((Collection) dir.call(ctx, null));
        return keys;
    }

    public synchronized Object getVariable(EvalContext ctx, Object name) throws EvalException, InternalEvalException {
        if ("var".equals(name)) return this;
        if ("this".equals(name)) return inline ? parent.getVariable(ctx, name) : this;
        if ("super".equals(name)) return inline ? parent.getVariable(ctx, name) : parent != null ? parent : this;
        if ("$context".equals(name)) return ctx;
        Object value = get(ctx, name);
        if (value != Primitive.VOID) return value;
        if (parent != null) value = parent.getVariable(ctx, name);
        return value;
    }

    public void setVariable(EvalContext ctx, Object name, Object value) throws EvalException, InternalEvalException {
        if (setSpecial(name, value)) return;
        if (!updateVariable(ctx, name, value)) {
            Environment env = this;
            while (env.inline && env.parent != null) env = env.parent;
            env.set(ctx, name, value);
        }
    }

    public void setLocalVariable(EvalContext ctx, Object name, Object value) throws EvalException, InternalEvalException {
        if (setSpecial(name, value)) return;
        set(ctx, name, value);
    }

    private boolean updateVariable(EvalContext ctx, Object name, Object value) throws EvalException, InternalEvalException {
        if (parent != null && parent.updateVariable(ctx, name, value)) return true;
        if (get(ctx, name) != Primitive.VOID) {
            set(ctx, name, value);
            return true;
        }
        return false;
    }

    Object get(EvalContext ctx, Object name) throws EvalException, InternalEvalException {
        Object value = Primitive.VOID;
        if (get != null) value = get.call(ctx, new Object[] { name });
        if (value == Primitive.VOID && locals.containsKey(name)) value = locals.get(name);
        return value;
    }

    void set(EvalContext ctx, Object name, Object value) throws EvalException, InternalEvalException {
        if (set != null) {
            set.call(ctx, new Object[] { name, value });
        } else {
            if (value != Primitive.VOID) locals.put(name, value); else locals.remove(name);
        }
    }

    private boolean setSpecial(Object name, Object value) throws EvalException, InternalEvalException {
        if ("var".equals(name)) throw new InternalEvalException("cannot assign a value to var");
        if ("this".equals(name)) throw new InternalEvalException("cannot assign a value to this");
        if ("super".equals(name)) {
            if (!(value instanceof Environment)) throw new InternalEvalException("super must be an Environment");
            setParent((Environment) value);
            return true;
        }
        if ("$set".equals(name)) {
            if (value == Primitive.VOID) value = null; else if (!(value instanceof Callable)) throw new InternalEvalException("$set must be a Callable");
            set = (Callable) value;
            return true;
        }
        if ("$get".equals(name)) {
            if (value == Primitive.VOID) value = null; else if (!(value instanceof Callable)) throw new InternalEvalException("$get must be a Callable");
            get = (Callable) value;
            return true;
        }
        if ("$dir".equals(name)) {
            if (value == Primitive.VOID) value = null; else if (!(value instanceof Callable)) throw new InternalEvalException("$dir must be a Callable");
            dir = (Callable) value;
            return true;
        }
        return false;
    }

    public boolean isInline() {
        return inline;
    }

    public synchronized Collection getImports() {
        if (imports == null) imports = Collections.synchronizedList(new ArrayList());
        return imports;
    }

    public synchronized void setImports(Collection imports) {
        this.imports = imports;
    }

    public synchronized Environment getParent() {
        return parent;
    }

    public synchronized void setParent(Environment parent) {
        this.parent = parent;
    }

    protected Class resolveClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
        }
        for (Environment en = this; en != null; en = en.getParent()) {
            for (Iterator i = en.getImports().iterator(); i.hasNext(); ) {
                try {
                    return Class.forName(i.next() + "." + name);
                } catch (ClassNotFoundException e) {
                }
            }
        }
        return null;
    }

    public String toString() {
        return "bee.Environment@" + Integer.toHexString(System.identityHashCode(this));
    }

    public Object getProxy(JavaBridge bridge, Class[] interfaces) {
        Class[] tmp = new Class[interfaces.length + 1];
        tmp[0] = EnvironmentProxy.class;
        System.arraycopy(interfaces, 0, tmp, 1, interfaces.length);
        return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, new ScriptInvoker(bridge));
    }

    class ScriptInvoker implements InvocationHandler {

        private JavaBridge bridge;

        public ScriptInvoker(JavaBridge bridge) {
            this.bridge = bridge;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("_this_".equals(method.getName())) return Environment.this;
            EvalContext ctx = new EvalContext(bridge, Environment.this);
            Object val = Environment.this.getVariable(ctx, method.getName());
            if (val == Primitive.VOID) {
                if ("toString".equals(method.getName())) return toString();
                throw new InternalEvalException("method " + method.getName() + " is not implemented");
            }
            if (!(val instanceof Callable)) throw new InternalEvalException(method.getName() + " is not a bee.Callable");
            Callable fun = (Callable) val;
            try {
                return Primitive.unwrap(fun.call(ctx, Primitive.wrap(args, method.getParameterTypes())));
            } catch (TargetException e) {
                throw e.getCause();
            } catch (InternalTargetException e) {
                throw e.getCause();
            }
        }
    }
}
