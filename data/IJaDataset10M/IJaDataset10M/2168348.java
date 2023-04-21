package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for Repeat-While ontology class<br>
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#Repeat-While)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment :  
    The Repeat-While class specializes the ControlConstruct class
    where the with properties "whileCondition" (range of type Condition)
    and ``whileProcess'' (range of type Repeat).  Repeat-While tests for 
    the condition, exits if it is false and does the operation if the
    condition is true, then loops. 
   <br>
 * <br>
 * <br>
 */
public interface Repeat_DASH_While extends org.marcont.services.definitions.process.Iterate, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#Repeat-While");

    /**
	 * The Jena Property for whileCondition 
	 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#whileCondition)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property whileConditionProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Process.owl#whileCondition");

    /**
	 * The Jena Property for whileProcess 
	 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#whileProcess)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property whileProcessProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Process.owl#whileProcess");

    /**
	 * Gets the 'whileCondition' property value
	 * @return		{@link com.ibm.adtech.jastor.Thing}
	 * @see			#whileConditionProperty
	 */
    public com.ibm.adtech.jastor.Thing getWhileCondition() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileCondition' property value
	 * @param		{@link com.ibm.adtech.jastor.Thing}
	 * @see			#whileConditionProperty
	 */
    public void setWhileCondition(com.ibm.adtech.jastor.Thing whileCondition) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileCondition' property value to an anonymous node
	 * @return		{@link com.ibm.adtech.jastor.Thing}, the created value
	 * @see			#whileConditionProperty
	 */
    public com.ibm.adtech.jastor.Thing setWhileCondition() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileCondition' property value to the given resource
	 * The resource argument should have rdf:type http://www.w3.org/2000/01/rdf-schema#Resource.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		{@link com.hp.hpl.jena.rdf.model.Resource} must not be be null.
	 * @return		{@link com.ibm.adtech.jastor.Thing}, the newly created value
	 * @see			#whileConditionProperty
	 */
    public com.ibm.adtech.jastor.Thing setWhileCondition(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Gets the 'whileProcess' property value
	 * @return		{@link org.marcont.services.definitions.process.ControlConstruct}
	 * @see			#whileProcessProperty
	 */
    public org.marcont.services.definitions.process.ControlConstruct getWhileProcess() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileProcess' property value
	 * @param		{@link org.marcont.services.definitions.process.ControlConstruct}
	 * @see			#whileProcessProperty
	 */
    public void setWhileProcess(org.marcont.services.definitions.process.ControlConstruct whileProcess) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileProcess' property value to an anonymous node
	 * @return		{@link org.marcont.services.definitions.process.ControlConstruct}, the created value
	 * @see			#whileProcessProperty
	 */
    public org.marcont.services.definitions.process.ControlConstruct setWhileProcess() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Sets the 'whileProcess' property value to the given resource
	 * The resource argument should have rdf:type http://www.daml.org/services/owl-s/1.2/Process.owl#ControlConstruct.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		{@link com.hp.hpl.jena.rdf.model.Resource} must not be be null.
	 * @return		{@link org.marcont.services.definitions.process.ControlConstruct}, the newly created value
	 * @see			#whileProcessProperty
	 */
    public org.marcont.services.definitions.process.ControlConstruct setWhileProcess(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;
}
