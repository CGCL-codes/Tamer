package org.dllearner.core.owl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EquivalentObjectPropertiesAxiom extends PropertyAxiom {

    private static final long serialVersionUID = -1085651734702155330L;

    private Set<ObjectProperty> equivalentProperties;

    public EquivalentObjectPropertiesAxiom(Set<ObjectProperty> equivalentProperties) {
        this.equivalentProperties = equivalentProperties;
    }

    public Set<ObjectProperty> getEquivalentProperties() {
        return equivalentProperties;
    }

    public int getLength() {
        int length = 1;
        for (ObjectProperty p : equivalentProperties) length += p.getLength();
        return length;
    }

    public String toString(String baseURI, Map<String, String> prefixes) {
        StringBuffer sb = new StringBuffer();
        sb.append("EquivalentObjectProperties(");
        Iterator<ObjectProperty> it = equivalentProperties.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString(baseURI, prefixes));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String toKBSyntaxString(String baseURI, Map<String, String> prefixes) {
        StringBuffer sb = new StringBuffer();
        sb.append("EquivalentObjectProperties(");
        Iterator<ObjectProperty> it = equivalentProperties.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toKBSyntaxString(baseURI, prefixes));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
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
        StringBuffer sb = new StringBuffer();
        sb.append("EquivalentObjectProperties(");
        Iterator<ObjectProperty> it = equivalentProperties.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toManchesterSyntaxString(baseURI, prefixes));
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
