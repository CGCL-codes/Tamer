package com.memoire.foo;

import com.memoire.foo.*;
import java.lang.reflect.*;
import java.util.*;

public class FooList implements FooEval {

    private static FooCategory pkg_ = null;

    public static final FooCategory init() {
        if (pkg_ == null) {
            pkg_ = FooCategory.create(FooList.class);
            pkg_.alias("&list");
            pkg_.setMessage("#", FooList.class, "length");
            pkg_.setMessage("+", FooList.class, "append");
            pkg_.setMessage("[]", FooList.class, "extractII");
            pkg_.setMessage("[[", FooList.class, "extractIX");
            pkg_.setMessage("]]", FooList.class, "extractXI");
            pkg_.setMessage("][", FooList.class, "extractXX");
            pkg_.setMessage("=", FooList.class, "setValue");
            pkg_.setMessage(":", FooList.class, "local", false);
            pkg_.setMessage("each", FooList.class, "each", false);
        }
        return pkg_;
    }

    private Vector vector_;

    String origin_;

    int lineno_;

    public FooList() {
        vector_ = new Vector(0);
    }

    void add(Object _o) {
        if (_o != FooVoid.VOID) {
            if (_o == null) _o = FooNull.NULL;
            vector_.addElement(_o);
        }
    }

    public Object at(int _i) {
        Object r = null;
        if ((_i >= 0) && (_i < vector_.size())) r = vector_.elementAt(_i);
        if (r == FooNull.NULL) r = null;
        return r;
    }

    public int length() {
        return vector_.size();
    }

    public boolean empty() {
        return (vector_.size() == 0);
    }

    public boolean contains(Object _o) {
        return vector_.contains(_o);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object _o) {
        if (_o == this) return true;
        if (_o == null) return false;
        boolean r = (_o instanceof FooList);
        if (r) {
            FooList o = (FooList) _o;
            int l = length();
            r = (l == o.length());
            if (r) {
                for (int i = 0; i < l; i++) {
                    Object a = at(i);
                    Object b = o.at(i);
                    r = FooObject.eqMsg(a, b).booleanValue();
                    if (!r) break;
                }
            }
        }
        return r;
    }

    public Object[] array() {
        int l = length();
        Object[] r = new Object[l];
        for (int i = 0; i < l; i++) r[i] = at(i);
        return r;
    }

    public Object convert(Class _c) {
        int l = length();
        Object r = Array.newInstance(_c, l);
        for (int i = 0; i < l; i++) Array.set(r, i, at(i));
        return r;
    }

    public Object eval() {
        if (empty()) return this;
        Object r = null;
        Object old_call3 = FooSymbol.get_local("[call-3]");
        Object old_call2 = FooSymbol.get_local("[call-2]");
        Object old_call1 = FooSymbol.get_local("[call-1]");
        Object old_caller = FooSymbol.get_local("[caller]");
        Object old_agent = FooSymbol.get_local("[agent]");
        FooSymbol.create_local("[call-3]", old_call2);
        FooSymbol.create_local("[call-2]", old_call1);
        FooSymbol.create_local("[call-1]", this);
        Object c = FooNull.NULL;
        Object a = null;
        if ((old_call1 instanceof FooList) && !((FooList) old_call1).empty()) c = ((FooList) old_call1).at(0);
        if ((c != null) && (FooCategory.create(c.getClass()).getMessage("[send]") != null)) a = c;
        FooSymbol.create_local("[caller]", c);
        if (a != null) FooSymbol.create_local("[agent]", a);
        r = eval1();
        FooSymbol.forget_local("[call-3]", old_call3);
        FooSymbol.forget_local("[call-2]", old_call2);
        FooSymbol.forget_local("[call-1]", old_call1);
        FooSymbol.forget_local("[caller]", old_caller);
        if (a != null) FooSymbol.forget_local("[agent]", old_agent);
        return r;
    }

    private Object eval1() {
        if (empty()) throw new RuntimeException("empty list:" + FooLib.toString(this));
        Object o = FooLib.eval(at(0));
        if (o == null) o = FooNull.NULL;
        if (length() == 1) throw new RuntimeException("no message to eval:" + FooLib.toString(this));
        Object z = at(1);
        if (z instanceof FooComma) z = ((FooComma) z).eval();
        String s = z.toString();
        Object r = null;
        Object e = this;
        Object a = FooSymbol.get_local("[agent]");
        Object c = FooSymbol.get_local("[caller]");
        Object old_meta_send = FooSymbol.get_local("[meta-send]");
        Object old_meta_recv = FooSymbol.get_local("[meta-recv]");
        if (!Boolean.TRUE.equals(old_meta_send) && !Boolean.TRUE.equals(old_meta_recv) && (a != null) && (a != o)) {
            FooList l = new FooList();
            l.add(a);
            l.add(FooSymbol.create("[send]"));
            l.add(this);
            FooSymbol.create_local("[meta-send]", Boolean.TRUE);
            e = l.eval();
            FooSymbol.forget_local("[meta-send]", old_meta_send);
        }
        if (!Boolean.TRUE.equals(old_meta_send) && (c != o) && (FooCategory.create(o.getClass()).getMessage("[receive]") != null) && !"[receive]".equals(s)) {
            FooList l = new FooList();
            l.add(o);
            l.add(FooSymbol.create("[receive]"));
            l.add(this);
            FooSymbol.create_local("[meta-recv]", Boolean.TRUE);
            e = l.eval();
            FooSymbol.forget_local("[meta-recv]", old_meta_recv);
        }
        if (equals(e)) r = eval0(o, s); else r = FooLib.eval(e);
        return r;
    }

    private Object eval0(Object _o, String _s) {
        Object r = null;
        Object o = _o;
        String s = _s;
        int l = length();
        Object[] p = new Object[l - 2];
        for (int i = 2; i < l; i++) p[i - 2] = at(i);
        Method m = null;
        Class c = o.getClass();
        boolean isClass = false;
        boolean evalArgs = true;
        Class d = c;
        while (d != null) {
            FooCategory fd = FooCategory.create(d);
            if (fd != null) {
                FooMessage msg = fd.getMessage(s);
                if (msg != null) {
                    Object obj = msg.getObject();
                    if (obj instanceof Method) {
                        m = (Method) obj;
                        evalArgs = msg.shouldEvalArgs();
                        break;
                    }
                    if (obj instanceof Field) {
                        if (msg.shouldEvalArgs()) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
                        return FooLib.invokeField(o, (Field) obj, p);
                    }
                    if (obj instanceof Object[]) {
                        if (msg.shouldEvalArgs()) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
                        return FooLib.invokeBlock(o, (Object[]) obj, p, msg.getArgNames());
                    }
                    if (obj instanceof String) {
                        d = c;
                        s = (String) obj;
                        if (msg.shouldEvalArgs()) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
                        continue;
                    }
                }
            }
            d = FooLib.getSuperclass(d);
        }
        if ((m == null) && (o instanceof Class)) {
            Constructor n = FooLib.getConstructor((Class) o, s, p.length);
            if (n != null) {
                if (evalArgs) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
                return FooLib.invokeConstructor(n, p);
            }
        }
        if (m == null) {
            m = FooLib.getMethod(c, s, p.length);
            if ((m == null) && (o instanceof Class)) {
                m = FooLib.getMethod((Class) o, s, p.length);
                isClass = (m != null);
            }
        }
        if (m == null) {
            Field f = FooLib.getField(c, s);
            if ((f == null) && (o instanceof Class)) f = FooLib.getField((Class) o, s);
            if (f != null) {
                if (evalArgs) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
                return FooLib.invokeField(o, f, p);
            }
        }
        if (m == null) throw new RuntimeException("message " + s + " can not be found for " + FooLib.getClassName(o.getClass()));
        if (evalArgs) for (int i = 0; i < p.length; i++) p[i] = FooLib.eval(p[i]);
        if (!isClass) {
            boolean isStatic = ((m.getModifiers() & Modifier.STATIC) != 0);
            if (isStatic) {
                Object[] q = new Object[p.length + 1];
                for (int i = 0; i < p.length; i++) q[i + 1] = p[i];
                q[0] = o;
                o = null;
                p = q;
            }
        }
        r = FooLib.invokeMethod(o, m, p);
        return r;
    }

    public String toString() {
        StringBuffer r = new StringBuffer();
        r.append('(');
        for (int i = 0; i < length(); i++) {
            if (i > 0) r.append(' ');
            Object o = at(i);
            if (o == null) r.append(o); else if (o.getClass().isArray()) r.append(FooLib.stringValue(o)); else r.append(o);
        }
        r.append(')');
        return r.toString();
    }

    public String toSource() {
        StringBuffer r = new StringBuffer();
        r.append('(');
        for (int i = 0; i < length(); i++) {
            if (i > 0) r.append(' ');
            r.append(FooLib.toSource(at(i)));
        }
        r.append(')');
        return r.toString();
    }

    public Object car() {
        return at(0);
    }

    public FooList cdr() {
        FooList r = new FooList();
        for (int i = 1; i < length(); i++) r.add(at(i));
        return r;
    }

    public FooList append(Object[] _n) {
        FooList r = new FooList();
        for (int i = 0; i < length(); i++) r.add(at(i));
        for (int i = 0; i < _n.length; i++) r.add(_n[i]);
        return r;
    }

    public FooList map(Object[] _o) {
        FooList r = new FooList();
        for (int i = 0; i < length(); i++) {
            Object o = at(i);
            FooList call = new FooList();
            call = call.append(new Object[] { new FooQuote(o) });
            call = call.append(_o);
            r.add(call.eval());
        }
        return r;
    }

    public FooList filter(Object[] _o) {
        FooList r = new FooList();
        for (int i = 0; i < length(); i++) {
            Object o = at(i);
            FooList call = new FooList();
            call = call.append(new Object[] { o });
            call = call.append(_o);
            Object b = call.eval();
            if (FooLib.booleanValue(b)) r.add(o);
        }
        return r;
    }

    public FooList each(Object[] _o) {
        FooLib.checkClassArgument(FooSymbol.class, _o[0], 1);
        FooList r = new FooList();
        String name = ((FooSymbol) _o[0]).getName();
        for (int i = 0; i < length(); i++) {
            Object old = FooSymbol.get_local(name);
            FooSymbol.create_local(name, at(i));
            r.add(FooLib.evalBlock(_o, 1));
            FooSymbol.forget_local(name, old);
        }
        return r;
    }

    public FooList slice(int _begin, int _end) {
        FooList r = new FooList();
        for (int i = Math.max(0, _begin); i < Math.min(length(), _end); i++) r.add(at(i));
        return r;
    }

    public Object extractII(Object[] _o) {
        Object r = null;
        switch(_o.length) {
            case 1:
                FooLib.checkClassArgument(Number.class, _o[0], 1);
                r = at(((Number) _o[0]).intValue());
                break;
            case 2:
                FooLib.checkClassArgument(Number.class, _o[0], 1);
                FooLib.checkClassArgument(Number.class, _o[1], 2);
                r = slice(((Number) _o[0]).intValue() + 0, ((Number) _o[1]).intValue() + 1);
                break;
            default:
                FooLib.checkNumberArgument(1, _o.length);
                break;
        }
        return r;
    }

    public Object extractIX(Object[] _o) {
        Object r = null;
        switch(_o.length) {
            case 2:
                FooLib.checkClassArgument(Number.class, _o[0], 1);
                FooLib.checkClassArgument(Number.class, _o[1], 2);
                r = slice(((Number) _o[0]).intValue() + 0, ((Number) _o[1]).intValue() + 0);
                break;
            default:
                FooLib.checkNumberArgument(2, _o.length);
                break;
        }
        return r;
    }

    public Object extractXI(Object[] _o) {
        Object r = null;
        switch(_o.length) {
            case 2:
                FooLib.checkClassArgument(Number.class, _o[0], 1);
                FooLib.checkClassArgument(Number.class, _o[1], 2);
                r = slice(((Number) _o[0]).intValue() + 1, ((Number) _o[1]).intValue() + 1);
                break;
            default:
                FooLib.checkNumberArgument(2, _o.length);
                break;
        }
        return r;
    }

    public Object extractXX(Object[] _o) {
        Object r = null;
        switch(_o.length) {
            case 2:
                FooLib.checkClassArgument(Number.class, _o[0], 1);
                FooLib.checkClassArgument(Number.class, _o[1], 2);
                r = slice(((Number) _o[0]).intValue() + 1, ((Number) _o[1]).intValue() + 0);
                break;
            default:
                FooLib.checkNumberArgument(2, _o.length);
                break;
        }
        return r;
    }

    public void setValue(FooList _list) {
        int l = _list.length();
        Object[] r = new Object[l];
        for (int i = 0; i < l; i++) r[i] = FooLib.eval(_list.at(i));
        int n = length();
        for (int i = 0; i < n; i++) {
            Object s = at(i);
            if (s == null) continue;
            if (!(s instanceof FooSymbol)) throw new RuntimeException("not a symbol:" + FooLib.toString(s));
            ((FooSymbol) s).setValue((i < r.length) ? r[i] : null);
        }
    }

    public Object local(Object[] _body) {
        Object r = null;
        Object[] names = array();
        int l = names.length;
        Object[] old_vars = new Object[l];
        for (int i = 0; i < l; i++) {
            Object s = names[i];
            if (!(s instanceof FooSymbol)) throw new RuntimeException("not a symbol:" + FooLib.toString(s));
            String n = s.toString();
            old_vars[i] = FooSymbol.get_local(n);
            FooSymbol.create_local(n, null);
        }
        r = FooLib.evalBlock(_body, 0);
        for (int i = 0; i < l; i++) {
            Object s = names[i];
            String n = s.toString();
            FooSymbol.forget_local(n, old_vars[i]);
        }
        return r;
    }

    public FooList order() {
        Hashtable t = new Hashtable();
        int l = length();
        int i;
        for (i = 0; i < l; i++) {
            Object o = at(i);
            Integer j = (Integer) t.get(o);
            if (j == null) j = new Integer(1); else j = new Integer(j.intValue() + 1);
            t.put(o, j);
        }
        l = t.size();
        int[] n = new int[l];
        Object[] v = new Object[l];
        Enumeration e = t.keys();
        i = 0;
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            v[i] = o;
            n[i] = ((Integer) t.get(o)).intValue();
            i++;
        }
        for (i = 1; i < l; i++) {
            if (n[i - 1] < n[i]) {
                int m = n[i - 1];
                n[i - 1] = n[i];
                n[i] = m;
                Object w = v[i - 1];
                v[i - 1] = v[i];
                v[i] = w;
                i--;
                if (i >= 0) i--;
            }
        }
        return new FooList().append(v);
    }
}
