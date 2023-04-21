package edu.neu.ccs.demeterf.perform;

import edu.neu.ccs.demeterf.Control;
import java.lang.reflect.*;
import java.util.ArrayList;
import edu.neu.ccs.demeterf.util.Option;
import edu.neu.ccs.demeterf.util.Util;
import edu.neu.ccs.demeterf.lib.List;

public abstract class AbstTraversal {

    protected Control control;

    /** Create a Traversal that goes Everywhere */
    public AbstTraversal() {
        this(Control.everywhere());
    }

    /** Create a Traversal with Selective edge/field Bypassing */
    public AbstTraversal(Control c) {
        control = c;
    }

    /** Do the Traversal... No traversal arguments */
    public <Ret> Ret traverse(Object o) {
        return this.<Ret>traverse(o, Option.none());
    }

    /** Do the Traversal... With a traversal argument */
    public <Ret> Ret traverse(Object o, Object a) {
        return this.<Ret>traverse(o, Option.some(a));
    }

    /** Do the Traversal... With an ML like Option argument (Some/None)*/
    protected <Ret> Ret traverse(Object o, Option arg) {
        Object result = null;
        Class<?> c = o.getClass();
        boolean hasArg = arg.some();
        List<Field> fl = Util.getFuncFields(c);
        if (control.isBuiltIn(c)) result = applyBuilder(Util.addArg(new Object[] { o }, arg), true); else {
            if (c.isArray()) result = traverseArray((Object[]) o, arg); else {
                ArrayList<Object> ret = new ArrayList<Object>();
                ret.add(o);
                for (Field f : fl) {
                    try {
                        f.setAccessible(true);
                        Object tret = f.get(o);
                        Option farg = (hasArg) ? applyAugment(new Object[] { o, arg.get() }, f) : arg;
                        if (!control.skip(c, f.getName())) tret = this.<Object>traverse(tret, farg);
                        ret.add(tret);
                    } catch (Exception e) {
                        throw (RuntimeException) e;
                    }
                }
                if (hasArg) ret.add(arg.get());
                result = applyBuilder(ret.toArray(), false);
            }
        }
        return (Ret) result;
    }

    /** Traverses each element of an Array, and combines the results into
     *    an array with an element type of the 'closest' common supertype
     *    of all returned values */
    protected Object traverseArray(Object[] o, Option arg) {
        ArrayList<Object> ret = new ArrayList<Object>();
        Class<?> c = Object.class;
        for (int i = 0; i < o.length; i++) {
            Object t = traverse(o[i], arg);
            Class<?> tc = t.getClass();
            ret.add(t);
            if (i == 1) c = tc; else while (!c.isAssignableFrom(tc)) c = c.getSuperclass();
        }
        return ret.toArray((Object[]) Array.newInstance(c, 0));
    }

    /** Apply the Augmentor to the Argument at this Object before
     *    traversing the given field. */
    protected Option applyAugment(Object o[], Field f) {
        return Option.some(applyAugment(o, f.getDeclaringClass(), f.getName()));
    }

    /** Apply the Builder to this list of 'fields' */
    protected abstract Object applyBuilder(Object o[], boolean prim);

    /** Apply the Augmentor the the traversal argument at this object
     *   before traversing the given 'field' */
    protected abstract Object applyAugment(Object o[], Class<?> pc, String fn);
}
