package org.genxdm.processor.w3c.xs.impl.xmlrep;

import javax.xml.namespace.QName;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.processor.w3c.xs.impl.SrcFrozenLocation;

abstract class XMLDeclaration extends XMLComponent {

    private final QName m_name;

    public XMLTypeRef typeRef;

    public XMLValueConstraint m_valueConstraint = null;

    public XMLDeclaration(final QName name, final XMLScope scope, final XMLTypeRef typeRef, final SrcFrozenLocation location) {
        super(scope, location);
        this.m_name = PreCondition.assertArgumentNotNull(name, "name");
        this.typeRef = PreCondition.assertArgumentNotNull(typeRef, "typeRef");
    }

    public XMLDeclaration(final QName name, final XMLScope scope, final XMLTypeRef typeRef) {
        super(scope);
        this.m_name = PreCondition.assertArgumentNotNull(name, "name");
        this.typeRef = PreCondition.assertArgumentNotNull(typeRef, "typeRef");
    }

    public QName getName() {
        return m_name;
    }
}
