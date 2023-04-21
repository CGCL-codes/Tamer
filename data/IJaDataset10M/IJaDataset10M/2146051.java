package com.google.gxp.compiler.base;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.gxp.compiler.parser.Namespace;
import com.google.gxp.compiler.parser.NullNamespace;

/**
 * The (name-space-qualified) name of an XML attribute.
 */
public class AttributeName {

    private final Namespace ns;

    private final String localName;

    public AttributeName(String localName) {
        this(NullNamespace.INSTANCE, localName);
    }

    public AttributeName(Namespace ns, String localName) {
        this.ns = Preconditions.checkNotNull(ns);
        this.localName = Preconditions.checkNotNull(localName);
    }

    public Namespace getNamespace() {
        return ns;
    }

    public String getLocalName() {
        return localName;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || (that instanceof AttributeName && equals((AttributeName) that));
    }

    public boolean equals(AttributeName that) {
        return Objects.equal(getNamespace(), that.getNamespace()) && Objects.equal(getLocalName(), that.getLocalName());
    }

    public int hashCode() {
        return Objects.hashCode(getNamespace(), getLocalName());
    }
}
