package net.sourceforge.ondex.parser.biopaxmodel;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for dataSource ontology class<br>
 * Use the net.sourceforge.ondex.parser.biopaxmodel.biopax_DASH_level2_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#dataSource)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : A description of the source of this data e.g. a database or person name.  Currently, this class only contains a free text description, but may be made more structured in future levels.^^http://www.w3.org/2001/XMLSchema#string <br>
 * <br>
 * <br>
 */
public interface dataSource extends net.sourceforge.ondex.parser.biopaxmodel.externalReferenceUtilityClass, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.biopax.org/release/biopax-level2.owl#dataSource");

    /**
	 * The Jena Property for NAME 
	 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#NAME)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : The preferred full name for this entity.@en <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property NAMEProperty = ResourceFactory.createProperty("http://www.biopax.org/release/biopax-level2.owl#NAME");

    /**
	 * The Jena Property for XREF 
	 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#XREF)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : Values of this slot define external cross-references from this entity to entities in external databases.@en <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property XREFProperty = ResourceFactory.createProperty("http://www.biopax.org/release/biopax-level2.owl#XREF");

    /**
	 * Iterates through the 'NAME' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link java.lang.String}
	 * @see			#NAMEProperty
	 */
    public java.util.Iterator getNAME() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Add a 'NAME' property value
	 * @param		{@link java.lang.String}, the value to add
	 * @see			#NAMEProperty
	 */
    public void addNAME(java.lang.String NAME) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Remove a 'NAME' property value. This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		{@link java.lang.String}, the value to remove
	 * @see			#NAMEProperty
	 */
    public void removeNAME(java.lang.String NAME) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Get an Iterator the 'XREF' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link net.sourceforge.ondex.parser.biopaxmodel.xref}
	 * @see			#XREFProperty
	 */
    public java.util.Iterator getXREF() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'XREF' property
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.xref} to add
	 * @see			#XREFProperty
	 */
    public void addXREF(net.sourceforge.ondex.parser.biopaxmodel.xref XREF) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'XREF' property
	 * @return		The anoymous {@link net.sourceforge.ondex.parser.biopaxmodel.xref} created
	 * @see			#XREFProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.xref addXREF() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'XREF' property.  This
	 * method is equivalent constructing a new instance of {@link net.sourceforge.ondex.parser.biopaxmodel.xref} with the factory
	 * and calling addXREF(net.sourceforge.ondex.parser.biopaxmodel.xref XREF)
	 * The resource argument have rdf:type http://www.biopax.org/release/biopax-level2.owl#xref.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#XREFProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.xref addXREF(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'XREF' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.xref} to remove
	 * @see			#XREFProperty
	 */
    public void removeXREF(net.sourceforge.ondex.parser.biopaxmodel.xref XREF) throws com.ibm.adtech.jastor.JastorException;
}
