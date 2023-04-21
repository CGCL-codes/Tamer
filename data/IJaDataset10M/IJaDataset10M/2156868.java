package net.sourceforge.ondex.parser.biopaxmodel;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for complex ontology class<br>
 * Use the net.sourceforge.ondex.parser.biopaxmodel.biopax_DASH_level2_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#complex)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : A physical entity whose structure is comprised of other physical entities bound to each other non-covalently, at least one of which is a macromolecule (protein or RNA). Complexes must be stable enough to function as a biological unit; in general, the temporary association of an enzyme with its substrate(s) should not be considered or represented as a complex. A complex is the physical product of an interaction (complex assembly), thus is not an interaction itself. Examples of this class include complexes of multiple protein monomers and complexes of proteins and small molecules.

NOTE: Complexes can be defined recursively to describe smaller complexes within larger complexes, e.g., a participant in a complex can itself be a complex. 

NOTE: The boundaries on the size of complexes described by this class are not defined here, although elements of the cell as large and dynamic as, e.g., a mitochondrion would typically not be described using this class (later versions of this ontology may include a cellularComponent class to represent these).  The strength of binding of the components is also not described.^^http://www.w3.org/2001/XMLSchema#string <br>
 * <br>
 * <br>
 */
public interface complex extends net.sourceforge.ondex.parser.biopaxmodel.physicalEntity, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.biopax.org/release/biopax-level2.owl#complex");

    /**
	 * The Jena Property for COMPONENTS 
	 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#COMPONENTS)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : Defines the physicalEntity subunits of this complex.@en <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property COMPONENTSProperty = ResourceFactory.createProperty("http://www.biopax.org/release/biopax-level2.owl#COMPONENTS");

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
	 * Get an Iterator the 'COMPONENTS' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant}
	 * @see			#COMPONENTSProperty
	 */
    public java.util.Iterator getCOMPONENTS() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'COMPONENTS' property
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant} to add
	 * @see			#COMPONENTSProperty
	 */
    public void addCOMPONENTS(net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant COMPONENTS) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'COMPONENTS' property
	 * @return		The anoymous {@link net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant} created
	 * @see			#COMPONENTSProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant addCOMPONENTS() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'COMPONENTS' property.  This
	 * method is equivalent constructing a new instance of {@link net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant} with the factory
	 * and calling addCOMPONENTS(net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant COMPONENTS)
	 * The resource argument have rdf:type http://www.biopax.org/release/biopax-level2.owl#physicalEntityParticipant.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#COMPONENTSProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant addCOMPONENTS(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'COMPONENTS' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant} to remove
	 * @see			#COMPONENTSProperty
	 */
    public void removeCOMPONENTS(net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant COMPONENTS) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Get an Iterator the 'ORGANISM' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource}
	 * @see			#ORGANISMProperty
	 */
    public java.util.Iterator getORGANISM() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'ORGANISM' property
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource} to add
	 * @see			#ORGANISMProperty
	 */
    public void addORGANISM(net.sourceforge.ondex.parser.biopaxmodel.bioSource ORGANISM) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'ORGANISM' property
	 * @return		The anoymous {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource} created
	 * @see			#ORGANISMProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.bioSource addORGANISM() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'ORGANISM' property.  This
	 * method is equivalent constructing a new instance of {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource} with the factory
	 * and calling addORGANISM(net.sourceforge.ondex.parser.biopaxmodel.bioSource ORGANISM)
	 * The resource argument have rdf:type http://www.biopax.org/release/biopax-level2.owl#bioSource.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#ORGANISMProperty
	 */
    public net.sourceforge.ondex.parser.biopaxmodel.bioSource addORGANISM(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'ORGANISM' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link net.sourceforge.ondex.parser.biopaxmodel.bioSource} to remove
	 * @see			#ORGANISMProperty
	 */
    public void removeORGANISM(net.sourceforge.ondex.parser.biopaxmodel.bioSource ORGANISM) throws com.ibm.adtech.jastor.JastorException;
}
