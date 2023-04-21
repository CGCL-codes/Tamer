package com.memoire.foo;

import com.memoire.foo.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class FooCategory {

    private static FooCategory pkg_ = null;

    public static final FooCategory init() {
        if (pkg_ == null) {
            pkg_ = FooCategory.create(FooCategory.class);
            pkg_.alias("&category");
            pkg_.setMessage("message", FooCategory.class, "messageEval", false);
            pkg_.setMessage("message:", FooCategory.class, "messageRaw", false);
        }
        return pkg_;
    }

    private static final Hashtable CATEGORIES = new Hashtable(21);

    public static FooCategory create(Class _class) {
        return create0(_class.getName());
    }

    static FooCategory create(String _name) {
        FooCategory r = create0(_name);
        FooSymbol.setValue(_name, r);
        return r;
    }

    private static FooCategory create0(String _name) {
        FooCategory r = get_raw(_name);
        if (r == null) {
            r = new FooCategory(_name);
            put_raw(_name, r);
        }
        return r;
    }

    static FooCategory get_raw(String _name) {
        return (FooCategory) CATEGORIES.get(_name);
    }

    static void put_raw(String _name, FooCategory _category) {
        if (_category == null) CATEGORIES.remove(_name); else CATEGORIES.put(_name, _category);
    }

    private String name_;

    private Hashtable messages_;

    private FooList parents_;

    private FooCategory(String _name) {
        name_ = _name;
        messages_ = new Hashtable();
        parents_ = new FooList();
    }

    private FooMessage get_msg(String _name) {
        return (FooMessage) messages_.get(_name);
    }

    private void put_msg(FooMessage _msg) {
        String name = _msg.getName();
        Object previous = get_msg(name);
        if ((previous != null) && (!previous.equals(_msg))) FooLib.getParser().warning("message redefined:" + name);
        messages_.put(name, _msg);
    }

    private void rem_msg(String _name) {
        Object previous = get_msg(_name);
        if (previous == null) FooLib.getParser().warning("message was not defined:" + _name); else messages_.remove(_name);
    }

    public String getName() {
        return name_;
    }

    public FooCategory alias(String _name) {
        if (!_name.startsWith("&")) throw new RuntimeException("illegal alias:" + _name);
        put_raw(_name, this);
        return this;
    }

    public void extend(FooCategory _category) {
        if (!parents_.contains(_category)) parents_.add(_category);
    }

    public FooMessage getMessage(String _name) {
        FooMessage r = get_msg(_name);
        if (r == null) {
            for (int i = 0; i < parents_.length(); i++) {
                r = ((FooCategory) parents_.at(i)).getMessage(_name);
                if (r != null) break;
            }
        }
        return r;
    }

    public void setMessage(FooMessage _message) {
        put_msg(_message);
    }

    public void setMessage(String _name, Field _field) {
        put_msg(new FooMessage(_name, _field, true));
    }

    public void setMessage(String _name, Method _method) {
        put_msg(new FooMessage(_name, _method, true));
    }

    public void setMessage(String _name, Method _method, boolean _eval) {
        put_msg(new FooMessage(_name, _method, _eval));
    }

    public void setMessage(String _name, String _alias) {
        put_msg(new FooMessage(_name, _alias, false));
    }

    public void setMessage(String _name, Object[] _expr, boolean _eval) {
        put_msg(new FooMessage(_name, _expr, _eval));
    }

    public void setMessage(String _name, Object[] _expr, String[] _argnames, boolean _eval) {
        put_msg(new FooMessage(_name, _expr, _argnames, _eval));
    }

    public void setMessage(String _name, Class _clazz, String _signature) {
        setMessage(_name, _clazz, _signature, true);
    }

    public void setMessage(String _name, Class _clazz, String _signature, boolean _evalargs) {
        Method m = FooLib.getMethod(_clazz, _signature);
        if (m == null) throw new RuntimeException("method " + _signature + " can not be found in " + FooLib.getClassName(_clazz));
        setMessage(_name, m, _evalargs);
    }

    public String[] getMessages() {
        String[] r = new String[messages_.size()];
        int i = 0;
        for (Enumeration e = messages_.keys(); e.hasMoreElements(); ) {
            r[i] = (String) e.nextElement();
            i++;
        }
        return r;
    }

    public String toString() {
        return "category " + name_;
    }

    public static Object newMsg(Object[] _p) {
        FooLib.checkClassArgument(Class.class, _p[0], 0);
        Class c = (Class) _p[0];
        Constructor m = FooLib.getConstructor(c, c.getName(), _p.length - 1);
        Object[] q = new Object[_p.length - 1];
        for (int i = 0; i < q.length; i++) q[i] = _p[i + 1];
        return FooLib.invokeConstructor(m, q);
    }

    public static Object methodMsg(Class _clazz, String _signature) {
        return FooLib.getMethod(_clazz, _signature);
    }

    public static Object messageEval(Object[] _p) {
        return message0(_p, true);
    }

    public static Object messageRaw(Object[] _p) {
        return message0(_p, false);
    }

    private static Object message0(Object[] _p, boolean _eval) {
        Object r = null;
        FooLib.checkClassArgument(FooCategory.class, _p[0], 0);
        FooCategory c = (FooCategory) _p[0];
        switch(_p.length) {
            case 1:
                FooList l = new FooList();
                for (Enumeration e = c.messages_.keys(); e.hasMoreElements(); ) l.add(e.nextElement());
                r = l;
                break;
            case 2:
                r = c.getMessage(_p[1].toString());
                break;
            case 3:
                String name = _p[1].toString();
                Object obj = FooLib.eval(_p[2]);
                if (obj instanceof FooMessage) c.setMessage((FooMessage) obj); else if (obj instanceof Field) c.setMessage(name, (Field) obj); else if (obj instanceof Method) c.setMessage(name, (Method) obj); else if (obj instanceof String) c.setMessage(name, (String) obj); else throw new RuntimeException("invalid argument:" + FooLib.toString(_p[2]));
                r = c;
                break;
            default:
                String[] argnames = null;
                if (_p[2] instanceof FooList) {
                    FooList a = (FooList) _p[2];
                    try {
                        int la = a.length();
                        argnames = new String[la];
                        for (int i = 0; i < la; i++) argnames[i] = ((FooSymbol) a.at(i)).getName();
                    } catch (Exception ex) {
                        throw new RuntimeException("invalid list of arguments:" + FooLib.toString(a));
                    }
                } else FooLib.checkClassArgument(Integer.class, _p[2], 2);
                Object[] q = new Object[_p.length - 3];
                for (int i = 3; i < _p.length; i++) q[i - 3] = _p[i];
                c.setMessage(_p[1].toString(), q, argnames, _eval);
                r = c;
                break;
        }
        return r;
    }
}
