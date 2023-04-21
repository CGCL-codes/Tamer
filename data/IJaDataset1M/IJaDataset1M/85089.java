package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for Input ontology class<br>
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#Input)</p>
 * <br>
 * <br>
 * <br>
 */
public interface Input extends org.marcont.services.definitions.process.Parameter, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#Input");

    /**
	 * The Jena Property for restInputParameter 
	 * <p>(URI: http://www.marcont.org/services/owl-s/1.2/Grounding.owl#restInputParameter)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment : a binding to a concrete implementaion of the parameter^^http://www.w3.org/2001/XMLSchema#string <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property restInputParameterProperty = ResourceFactory.createProperty("http://www.marcont.org/services/owl-s/1.2/Grounding.owl#restInputParameter");

    /**
	 * Gets the 'restInputParameter' property value
	 * @return		{@link org.marcont.services.definitions.grounding.RestInputParameter}
	 * @see			#restInputParameterProperty
	 */
    public org.marcont.services.definitions.grounding.RestInputParameter getRestInputParameter() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'restInputParameter' property value
	 * @param		{@link org.marcont.services.definitions.grounding.RestInputParameter}
	 * @see			#restInputParameterProperty
	 */
    public void setRestInputParameter(org.marcont.services.definitions.grounding.RestInputParameter restInputParameter) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'restInputParameter' property value to an anonymous node
	 * @return		{@link org.marcont.services.definitions.grounding.RestInputParameter}, the created value
	 * @see			#restInputParameterProperty
	 */
    public org.marcont.services.definitions.grounding.RestInputParameter setRestInputParameter() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'restInputParameter' property value to the given resource
	 * The resource argument should have rdf:type http://www.marcont.org/services/owl-s/1.2/Grounding.owl#RestInputParameter.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		{@link com.hp.hpl.jena.rdf.model.Resource} must not be be null.
	 * @return		{@link org.marcont.services.definitions.grounding.RestInputParameter}, the newly created value
	 * @see			#restInputParameterProperty
	 */
    public org.marcont.services.definitions.grounding.RestInputParameter setRestInputParameter(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;
}
