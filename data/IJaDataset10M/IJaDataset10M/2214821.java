package org.jxul;

/**
 * Should be contained within a bindings element. A binding is used to bind a variable to a node. Like the triple element in syntax, it can be used to bind a particular property of a matched node to a particular variable name. That name can then be used within the action of a rule.
 * @author <a href="wetson@comcast.net">Will Etson</a>
 *
 */
public interface XulBinding {

    String ELEMENT_NAME = "binding";

    /**
	 * The object of a binding. It can be a variable reference, an RDF node URI, or an RDF literal value.
	 */
    Object getObject();

    void setObject(Object object);

    /**
	 * @return The predicate or property to match. This must be a URI of the property.
	 */
    String getPredicate();

    void setPredicate(String predicate);

    /**
	 * 
	 * @return The subject of a binding. It can be a variable reference, an RDF node URI, or an RDF literal value.
	 */
    Object getSubject();

    void setSubject(Object object);
}
