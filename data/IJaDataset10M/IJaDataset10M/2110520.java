package it.essepuntato.earmark.core;

import java.net.URI;
import java.util.Set;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * This class represents an abstract docuverse in the EARMARK model.
 * 
 * @author Silvio Peroni
 *
 */
public abstract class Docuverse implements EARMARKItem {

    /**
	 * This enumeration describes all the possible types for docuverses.
	 * 
	 * @author Silvio Peroni
	 *
	 */
    public enum Type {

        /**
		 * This variable is used to refer to a string docuverse.
		 */
        StringDocuverse, /**
		 * This variable is used to refer to an URI docuverse.
		 */
        URIDocuverse
    }

    private EARMARKDocument document;

    private Object content;

    private URI id;

    /**
	 * Create a docuverse.
	 * 
	 * @param d the document to which this item is associated.
	 * @param c the object content of the new docuverse.
	 * @param id the identifier for the new docuverse.
	 */
    protected Docuverse(EARMARKDocument d, Object c, URI id) {
        document = d;
        content = c;
        this.id = id;
    }

    /**
	 * This method returns the content describing it.
	 * 
	 * @return an object representing the content of the docuverse.
	 */
    public Object hasContent() {
        return content;
    }

    /**
	 * This method sets the content of the docuverse.
	 * 
	 * @param c the content to set.
	 */
    protected void setContent(Object c) {
        content = c;
    }

    /**
	 * This method says the type of this docuverse.
	 * 
	 * @return the type of the docuverse.
	 */
    public abstract Docuverse.Type getType();

    @Override
    public URI hasId() {
        return id;
    }

    @Override
    public String hasLocalId() {
        String result = null;
        String[] curIdString = id.toString().split("(/|#)");
        for (int i = curIdString.length - 1; i >= 0 && result == null; i--) {
            String current = curIdString[i];
            if (!current.equals("")) {
                result = current;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Docuverse other = (Docuverse) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (!hasId().equals(other.hasId())) return false;
        return true;
    }

    /**
	 * This method changes the local id of the docuverse with a new one. If the new id
	 * specified is equal to EARMARKDocument.reservedDocumentId, no change is performed.
	 * 
	 * @param newId the new local id for the docuverse.
	 */
    protected void setId(URI newId) {
        URI tmp = id;
        id = newId;
        if (!document.setId(this, newId)) {
            id = tmp;
        }
    }

    /**
	 * This method sets the EARMARK document associated to the docuverse.
	 * 
	 * @param d the EARMARK document associated to the docuverse.
	 */
    protected void setEARMARKDocument(EARMARKDocument d) {
        document = d;
    }

    /**
	 * This method returns the EARMARK document associated to the docuverse.
	 * 
	 * @return the EARMARK document associated to the docuverse.
	 */
    protected EARMARKDocument getEARMARKDocument() {
        return document;
    }

    @Override
    public Statement assertsAsObject(Resource subject, Property predicate) {
        return getEARMARKDocument().addAssertion(subject, predicate, this);
    }

    @Override
    public Statement assertsAsObject(EARMARKItem subject, Property predicate) {
        return getEARMARKDocument().addAssertion(subject, predicate, this);
    }

    @Override
    public Statement assertsAsSubject(Property predicate, RDFNode object) {
        return getEARMARKDocument().addAssertion(this, predicate, object);
    }

    @Override
    public Statement assertsAsSubject(Property predicate, EARMARKItem object) {
        return getEARMARKDocument().addAssertion(this, predicate, object);
    }

    @Override
    public Set<Statement> getAssertionsAsObject() {
        return getEARMARKDocument().getAssertionsAsObject(this);
    }

    @Override
    public Set<Statement> getAssertionsAsSubject() {
        return getEARMARKDocument().getAssertionsAsSubject(this);
    }

    @Override
    public Set<Statement> removeAllAssertions() {
        return getEARMARKDocument().removeAllAssertions(this);
    }

    /**
	 * This method returns a new docuverse initialised with an identifier different from the calling node one and
	 * containing the same content.
	 * 
	 * @return a new docuverse.
	 */
    public abstract Docuverse clone();

    @Override
    public String toString() {
        return hasId().toString();
    }
}
