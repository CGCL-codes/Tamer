package org.dllearner.core.owl;

import java.util.Map;

/**
 * Represents an role assertion in a knowledge base / ontology, 
 * e.g. "heiko is brother of stefan".
 * 
 * @author Jens Lehmann
 *
 */
public class ObjectPropertyAssertion extends PropertyAssertion {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7546114914807945292L;

    private ObjectProperty role;

    private Individual individual1;

    private Individual individual2;

    public ObjectPropertyAssertion(ObjectProperty role, Individual individual1, Individual individual2) {
        this.role = role;
        this.individual1 = individual1;
        this.individual2 = individual2;
    }

    public Individual getIndividual1() {
        return individual1;
    }

    public Individual getIndividual2() {
        return individual2;
    }

    public ObjectProperty getRole() {
        return role;
    }

    @Override
    public void accept(AxiomVisitor visitor) {
        visitor.visit(this);
    }

    public int getLength() {
        return 2 + role.getLength();
    }

    public String toString(String baseURI, Map<String, String> prefixes) {
        return role.toString(baseURI, prefixes) + "(" + individual1.toString(baseURI, prefixes) + "," + individual2.toString(baseURI, prefixes) + ")";
    }

    public String toKBSyntaxString(String baseURI, Map<String, String> prefixes) {
        return role.toKBSyntaxString(baseURI, prefixes) + "(" + individual1.toKBSyntaxString(baseURI, prefixes) + "," + individual2.toKBSyntaxString(baseURI, prefixes) + ")";
    }

    public void accept(KBElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toManchesterSyntaxString(String baseURI, Map<String, String> prefixes) {
        return "OBJECTPROPERTYASSERTION NOT IMPLEMENTED";
    }
}
