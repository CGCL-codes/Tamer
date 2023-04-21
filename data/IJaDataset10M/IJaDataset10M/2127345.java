package com.vladium.jcd.cls;

/**
 * @author (C) 2001, Vlad Roubtsov
 */
public abstract class AbstractClassDefVisitor implements IClassDefVisitor {

    public Object visit(final ClassDef cls, final Object ctx) {
        visit(cls.getConstants(), ctx);
        visit(cls.getInterfaces(), ctx);
        visit(cls.getFields(), ctx);
        visit(cls.getMethods(), ctx);
        visit(cls.getAttributes(), ctx);
        return ctx;
    }

    public Object visit(final IAttributeCollection attributes, final Object ctx) {
        return ctx;
    }

    public Object visit(final IConstantCollection constants, final Object ctx) {
        return ctx;
    }

    public Object visit(final IFieldCollection fields, final Object ctx) {
        return ctx;
    }

    public Object visit(final IInterfaceCollection interfaces, final Object ctx) {
        return ctx;
    }

    public Object visit(final IMethodCollection methods, final Object ctx) {
        return ctx;
    }
}
