package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for Split-Join ontology class<br>
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#Split-Join)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : 
    Here the process consists of concurrent execution of a bunch of
    sub-processes.  with barrier synchroniztion. With Split and Split and
    Join, we can define processes which have partial synchronization
    (ex. split all and join some subset).
   <br>
 * <br>
 * <br>
 */
public interface Split_DASH_Join extends org.marcont.services.definitions.process.ControlConstruct, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#Split-Join");

    /**
	 * The Jena Property for components 
	 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#components)</p>
	 * <br>
	 * <br>
	 * RDF Schema Standard Properties <br>
	 * 	comment :  
    The components propery of selected control construct subclasses holds
    a specific arrangement of subprocesses or control constructs. 
    The range is declared at each relevant subclass of ControlConstruct.
   <br>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property componentsProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Process.owl#components");

    /**
	 * Get an Iterator the 'components' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link com.ibm.adtech.jastor.Thing}
	 * @see			#componentsProperty
	 */
    public java.util.Iterator getComponents() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'components' property
	 * @param		The {@link com.ibm.adtech.jastor.Thing} to add
	 * @see			#componentsProperty
	 */
    public void addComponents(com.ibm.adtech.jastor.Thing components) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'components' property
	 * @return		The anoymous {@link com.ibm.adtech.jastor.Thing} created
	 * @see			#componentsProperty
	 */
    public com.ibm.adtech.jastor.Thing addComponents() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * 
	 * The resource argument have rdf:type http://www.w3.org/2000/01/rdf-schema#Resource.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#componentsProperty
	 */
    public com.ibm.adtech.jastor.Thing addComponents(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'components' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link com.ibm.adtech.jastor.Thing} to remove
	 * @see			#componentsProperty
	 */
    public void removeComponents(com.ibm.adtech.jastor.Thing components) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Get an Iterator the 'components' property values.  This Iteartor
	 * may be used to remove all such values.
	 * @return		{@link java.util.Iterator} of {@link org.marcont.services.definitions.process.ControlConstructBag}
	 * @see			#componentsProperty
	 */
    public java.util.Iterator getComponents_asControlConstructBag() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds a value for the 'components' property
	 * @param		The {@link org.marcont.services.definitions.process.ControlConstructBag} to add
	 * @see			#componentsProperty
	 */
    public void addComponents(org.marcont.services.definitions.process.ControlConstructBag components) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Adds an anonymous value for the 'components' property
	 * @return		The anoymous {@link org.marcont.services.definitions.process.ControlConstructBag} created
	 * @see			#componentsProperty
	 */
    public org.marcont.services.definitions.process.ControlConstructBag addComponents_asControlConstructBag() throws com.ibm.adtech.jastor.JastorException;

    /**
	 * 
	 * The resource argument have rdf:type http://www.daml.org/services/owl-s/1.2/Process.owl#ControlConstructBag.  That is, this method
	 * should not be used as a shortcut for creating new objects in the model.
	 * @param		The {@link om.hp.hpl.jena.rdf.model.Resource} to add
	 * @see			#componentsProperty
	 */
    public org.marcont.services.definitions.process.ControlConstructBag addComponents_asControlConstructBag(com.hp.hpl.jena.rdf.model.Resource resource) throws com.ibm.adtech.jastor.JastorException;

    /**
	 * Removes a value for the 'components' property.  This method should not
	 * be invoked while iterator through values.  In that case, the remove() method of the Iterator
	 * itself should be used.
	 * @param		The {@link org.marcont.services.definitions.process.ControlConstructBag} to remove
	 * @see			#componentsProperty
	 */
    public void removeComponents(org.marcont.services.definitions.process.ControlConstructBag components) throws com.ibm.adtech.jastor.JastorException;
}
