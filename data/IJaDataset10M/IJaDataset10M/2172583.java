package com.memoire.foo;

import com.memoire.foo.*;
import java.util.*;

public class FooObject {

    private static FooCategory pkg_ = null;

    public static final FooCategory init() {
        if (pkg_ == null) {
            pkg_ = FooCategory.create(Object.class);
            pkg_.alias("&object");
            pkg_.setMessage("self", FooObject.class, "self");
            pkg_.setMessage("cons", FooObject.class, "cons");
            pkg_.setMessage("list", FooObject.class, "list");
            pkg_.setMessage("message", FooObject.class, "message");
            pkg_.setMessage("when", FooObject.class, "whenMsg", false);
            pkg_.setMessage("unless", FooObject.class, "unlessMsg", false);
            pkg_.setMessage("if", FooObject.class, "ifMsg", false);
            pkg_.setMessage("select", FooObject.class, "selectMsg", false);
            pkg_.setMessage("==", FooObject.class, "eqMsg");
            pkg_.setMessage("!=", FooObject.class, "neMsg");
            pkg_.setMessage("eval", FooLib.class, "eval");
            pkg_.setMessage("boolean", FooLib.class, "toBoolean");
            pkg_.setMessage("byte", FooLib.class, "toByte");
            pkg_.setMessage("double", FooLib.class, "toDouble");
            pkg_.setMessage("float", FooLib.class, "toFloat");
            pkg_.setMessage("int", FooLib.class, "toInteger");
            pkg_.setMessage("long", FooLib.class, "toLong");
            pkg_.setMessage("short", FooLib.class, "toShort");
            pkg_.setMessage("char", FooLib.class, "toCharacter");
            pkg_.setMessage("string", FooLib.class, "toString(java.lang.Object)");
        }
        return pkg_;
    }

    public static Object self(Object[] _o) {
        return _o[0];
    }

    public static FooList cons(Object _a, Object _b) {
        FooList r = new FooList();
        r.add(_a);
        if (_b instanceof FooList) {
            FooList l = (FooList) _b;
            for (int i = 0; i < l.length(); i++) r.add(l.at(i));
        } else r.add(_b);
        return r;
    }

    public static FooList list(Object[] _o) {
        FooList r = new FooList();
        for (int i = 0; i < _o.length; i++) r.add(_o[i]);
        return r;
    }

    public static FooList message(Object _o) {
        FooList r = new FooList();
        if (_o != null) {
            Class c = _o.getClass();
            while (c != null) {
                FooCategory f = FooCategory.create(c);
                if (f != null) {
                    FooList l = new FooList();
                    l.add(FooSymbol.create(f.getName()));
                    l.add(new FooList().append(f.getMessages()));
                    r.add(l);
                }
                c = FooLib.getSuperclass(c);
            }
        }
        return r;
    }

    public static Object whenMsg(Object[] _body) {
        if (_body[0] instanceof FooQuote) FooLib.getParser().warning("suspicious quote:" + _body[0]);
        Object r = null;
        if (FooLib.booleanValue(_body[0])) r = FooLib.evalBlock(_body, 1);
        return r;
    }

    public static Object unlessMsg(Object[] _body) {
        if (_body[0] instanceof FooQuote) FooLib.getParser().warning("suspicious quote:" + _body[0]);
        Object r = null;
        if (!FooLib.booleanValue(_body[0])) r = FooLib.evalBlock(_body, 1);
        return r;
    }

    public static Object ifMsg(Object _test, Object _then, Object _else) {
        Object r = null;
        if (FooLib.booleanValue(FooLib.eval(_test))) r = FooLib.eval(_then); else r = FooLib.eval(_else);
        return r;
    }

    public static Object selectMsg(Object[] _body) {
        Object r = null;
        for (int i = 1; i < _body.length; i++) FooLib.checkClassArgument(FooList.class, _body[i], i);
        for (int i = 1; i < _body.length; i++) {
            Object v = FooLib.eval(((FooList) _body[i]).car());
            if ((v == FooDefault.DEFAULT) || eqMsg(_body[0], v).booleanValue()) {
                Object o = ((FooList) _body[i]).cdr().car();
                r = FooLib.eval(o);
                if ((v == FooDefault.DEFAULT) && (i + 1 < _body.length)) FooLib.getParser().warning("unreachable expr " + i);
                break;
            }
        }
        return r;
    }

    public static Boolean eqMsg(Object _a, Object _b) {
        boolean r;
        if (_a == null) r = (_b == null); else r = _a.equals(_b);
        return FooBoolean.create(r);
    }

    public static Boolean neMsg(Object _a, Object _b) {
        boolean r;
        if (_a == null) r = (_b != null); else r = !_a.equals(_b);
        return FooBoolean.create(r);
    }
}
