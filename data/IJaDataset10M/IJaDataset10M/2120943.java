package com.google.gxp.compiler.base;

/**
 * Visitor for {@link Root}s.
 *
 * @param <T> return type of visitor
 */
public interface RootVisitor<T> {

    T visitInterface(Interface iface);

    T visitNullRoot(NullRoot nullRoot);

    T visitTemplate(Template template);
}
