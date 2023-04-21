package org.dllearner.core.owl;

import java.util.Map;

/**
 * @author Jens Lehmann
 *
 */
public class DatatypePropertyDomainAxiom extends PropertyDomainAxiom {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4545077497129132116L;

    public DatatypePropertyDomainAxiom(DatatypeProperty property, Description domain) {
        super(property, domain);
    }

    @Override
    public DatatypeProperty getProperty() {
        return (DatatypeProperty) property;
    }

    public int getLength() {
        return domain.getLength() + 2;
    }

    public String toString(String baseURI, Map<String, String> prefixes) {
        return "Domain(" + getProperty() + ", " + getDomain() + ")";
    }

    public String toKBSyntaxString(String baseURI, Map<String, String> prefixes) {
        return "DPDOMAIN(" + property.toKBSyntaxString(baseURI, prefixes) + ") = " + domain.toKBSyntaxString(baseURI, prefixes);
    }

    @Override
    public void accept(AxiomVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(KBElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toManchesterSyntaxString(String baseURI, Map<String, String> prefixes) {
        return "Domain(" + getProperty().toManchesterSyntaxString(baseURI, prefixes) + ", " + getDomain().toManchesterSyntaxString(baseURI, prefixes) + ")";
    }
}
