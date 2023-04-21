package org.genxdm.processor.w3c.xs.impl.xmlrep;

import javax.xml.namespace.QName;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.processor.w3c.xs.impl.SrcFrozenLocation;

abstract class XMLComponent extends XMLTag {

    private final XMLScope scope;

    public XMLComponent(final XMLScope scope) {
        this.scope = PreCondition.assertArgumentNotNull(scope, "scope");
    }

    public XMLComponent(final XMLScope scope, final SrcFrozenLocation location) {
        super(location);
        this.scope = PreCondition.assertArgumentNotNull(scope, "scope");
    }

    public abstract QName getName();

    public final XMLScope getScope() {
        return scope;
    }
}
