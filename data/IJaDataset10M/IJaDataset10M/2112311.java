package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for Participant ontology class<br>
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#Participant)</p>
 * <br>
 * <br>
 * <br>
 */
public interface Participant extends com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#Participant");

    /**
	 * Individual for URI: http://www.daml.org/services/owl-s/1.2/Process.owl#TheClient
	 */
    public static com.hp.hpl.jena.rdf.model.Resource TheClient = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#TheClient");

    /**
	 * Individual for URI: http://www.daml.org/services/owl-s/1.2/Process.owl#TheServer
	 */
    public static com.hp.hpl.jena.rdf.model.Resource TheServer = ResourceFactory.createResource("http://www.daml.org/services/owl-s/1.2/Process.owl#TheServer");
}
