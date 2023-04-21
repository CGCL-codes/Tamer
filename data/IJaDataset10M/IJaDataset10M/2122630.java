package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for Perform ontology class<br>
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#Perform)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : 
    The PERFORM construct is how one references a process in a composite
    process.  This is analogous to a function call in a program body. The
    inputs to the PERFORM are described using the hasDataFrom property.
   <br>
 * <br>
 * <br>
 */
public interface Perform extends org.marcont.services.definitions.process.ControlConstruct, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#Perform");

    /**
	 * The Jena Property for hasDataFrom 
	 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#hasDataFrom)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : 
    This property has as range a Binding object, which may either indicate
    constants or values that are derived from the parameters (typically
    outputs) of other performs in the SAME COMPOSITE PROCESS.
   <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property hasDataFromProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Process.owl#hasDataFrom");

    /**
	 * The Jena Property for process 
	 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#process)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property processProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Process.owl#process");

    /**
	 * Individual for URI: http://www.daml.org/services/owl-s/1.2/Process.owl#ThisPerform
	 */
    public static com.hp.hpl.jena.rdf.model.Resource ThisPerform = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#ThisPerform");

    /**
	 * Individual for URI: http://www.daml.org/services/owl-s/1.2/Process.owl#TheParentPerform
	 */
    public static com.hp.hpl.jena.rdf.model.Resource TheParentPerform = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#TheParentPerform");

    /**
	 * Get an Iterator the 'hasDataFrom' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link org.marcont.services.definitions.process.Binding}
	 * @see			#hasDataFromProperty
	 */
    public java.util.Iterator getHasDataFrom() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'hasDataFrom' property
	 * @param		The {@link org.marcont.services.definitions.process.Binding} to add
	 * @see			#hasDataFromProperty
	 */
    public void addHasDataFrom(org.marcont.services.definitions.process.Binding hasDataFrom) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'hasDataFrom' property
	 * @return		The anoymous {@link org.marcont.services.definitions.process.Binding} created
	 * @see			#hasDataFromProperty
	 */
    public org.marcont.services.definitions.process.Binding addHasDataFrom() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * 
	 * The resource argument have rdf:type http://www.daml.org/services/owl-s/1.2/Process.owl#Binding.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#hasDataFromProperty
	 */
    public org.marcont.services.definitions.process.Binding addHasDataFrom(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'hasDataFrom' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link org.marcont.services.definitions.process.Binding} to remove
	 * @see			#hasDataFromProperty
	 */
    public void removeHasDataFrom(org.marcont.services.definitions.process.Binding hasDataFrom) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Gets the 'process' property value
	 * @return		{@link org.marcont.services.definitions.process.Process}
	 * @see			#processProperty
	 */
    public org.marcont.services.definitions.process.Process getProcess() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'process' property value
	 * @param		{@link org.marcont.services.definitions.process.Process}
	 * @see			#processProperty
	 */
    public void setProcess(org.marcont.services.definitions.process.Process process) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'process' property value to an anonymous node
	 * @return		{@link org.marcont.services.definitions.process.Process}, the created value
	 * @see			#processProperty
	 */
    public org.marcont.services.definitions.process.Process setProcess() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'process' property value to the given resource
	 * The resource argument should have rdf:type http://www.daml.org/services/owl-s/1.2/Process.owl#Process.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		{@link com.hp.hpl.jena.rdf.model.Resource} must not be be null.
	 * @return		{@link org.marcont.services.definitions.process.Process}, the newly created value
	 * @see			#processProperty
	 */
    public org.marcont.services.definitions.process.Process setProcess(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;
}
