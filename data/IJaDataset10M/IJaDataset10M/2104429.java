package net.sourceforge.ondex.parser.biopaxmodel;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for rna ontology class<br>
 * Use the net.sourceforge.ondex.parser.biopaxmodel.biopax_DASH_level2_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#rna)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : A physical entity consisting of a sequence of ribonucleotide monophosphates; a ribonucleic acid (e.g. messengerRNA, microRNA, ribosomalRNA). A specific example is the let-7 microRNA.^^http://www.w3.org/2001/XMLSchema#string <br>
 * <br>
 * <br>
 */
public interface rna extends net.sourceforge.ondex.parser.biopaxmodel.physicalEntity, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.biopax.org/release/biopax-level2.owl#rna");

    /**
	 * The Jena Property for SEQUENCE 
	 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#SEQUENCE)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : Polymer sequence in uppercase letters.@en <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property SEQUENCEProperty = ResourceFactory.createProperty("http://www.biopax.org/release/biopax-level2.owl#SEQUENCE");

    /**
	 * The Jena Property for ORGANISM 
	 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#ORGANISM)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : An organism, e.g. 'Homo sapiens'.@en <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property ORGANISMProperty = ResourceFactory.createProperty("http://www.biopax.org/release/biopax-level2.owl#ORGANISM");

    /**
	 * Gets the 'SEQUENCE' property value
	 * @return		{@link java.lang.String}
	 * @see			#SEQUENCEProperty
	 */
    public java.lang.String getSEQUENCE() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'SEQUENCE' property value
	 * @param		{@link java.lang.String}
	 * @see			#SEQUENCEProperty
	 */
    public void setSEQUENCE(java.lang.String SEQUENCE) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Gets the 'ORGANISM' property value
	 * @return		{@link net.sourceforge.ondex.parser.biopaxmodel.bioSource}
	 * @see			#ORGANISMProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.bioSource getORGANISM() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'ORGANISM' property value
	 * @param		{@link net.sourceforge.ondex.parser.biopaxmodel.bioSource}
	 * @see			#ORGANISMProperty
	 */
    public void setORGANISM(net.sourceforge.ondex.parser.biopaxmodel.bioSource ORGANISM) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'ORGANISM' property value to an anonymous node
	 * @return		{@link net.sourceforge.ondex.parser.biopaxmodel.bioSource}, the created value
	 * @see			#ORGANISMProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.bioSource setORGANISM() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'ORGANISM' property value to the given resource, and add's rdf:type properties.  This
	 * method is equivalent constructing a new instance of {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource} with the factory.
	 * and calling setORGANISM(net.sourceforge.ondex.parser.biopaxmodel.bioSource ORGANISM)
	 * The resource argument have rdf:type http://www.biopax.org/release/biopax-level2.owl#bioSource.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		{@link com.hp.hpl.jena.rdf.model.Resource} must not be be null.
	 * @return		{@link net.sourceforge.ondex.parser.biopaxmodel.bioSource}, the newly created value
	 * @see			#ORGANISMProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.bioSource setORGANISM(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;
}
