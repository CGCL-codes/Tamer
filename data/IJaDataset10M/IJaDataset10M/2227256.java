package org.taak.operator;

import org.taak.function.MemberFunction;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.taak.*;
import org.taak.error.*;
import org.taak.util.*;

public class New extends Code {

    static final Logger log = Logger.getLogger(New.class);

    public String type;

    public Code[] args;

    public New(String type, Code[] args) {
        this.type = type;
        this.args = args;
    }

    public Object eval(Context context) {
        Object result = null;
        Object t = context.get(type);
        if (t instanceof Type) {
            Type type = (Type) t;
            result = constructObj(context, type);
        } else if (t instanceof Class) {
            result = constructJavaObject(context, (Class) t);
        }
        if (result == null) {
            throw new UnknownType(type);
        }
        return result;
    }

    private Object constructObj(Context context, Type type) {
        return type.__new__(context, args);
    }

    private Object constructJavaObject(Context context, Class type) {
        Object[] params = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = context.eval(args[i]);
        }
        Class[] paramTypes = TypeUtil.getTypes(params);
        Constructor ctor = TypeUtil.getConstructor(type, paramTypes);
        if (ctor == null) {
            throw new TaakError("No constructor for " + type);
        }
        Class[] types = ctor.getParameterTypes();
        TypeUtil.convert(types, params);
        Object obj = null;
        try {
            obj = ctor.newInstance(params);
        } catch (IllegalAccessException e) {
            throw new TaakError(e);
        } catch (InstantiationException e) {
            throw new TaakError(e);
        } catch (InvocationTargetException e) {
            throw new TaakError(e);
        }
        return obj;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("new ");
        sb.append(type);
        sb.append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i]);
        }
        sb.append(")");
        return sb.toString();
    }
}
