package org.marcont.services.definitions.timeEntry;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for TemporalEntity ontology class<br>
 * Use the org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#TemporalEntity)</p>
 * <br>
 * <br>
 * <br>
 */
public interface TemporalEntity extends org.marcont.services.definitions.timeEntry.TemporalThing, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.isi.edu/~pan/damltime/time-entry.owl#TemporalEntity");

    /**
	 * The Jena Property for intContains 
	 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#intContains)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property intContainsProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intContains");

    /**
	 * The Jena Property for after 
	 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#after)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property afterProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#after");

    /**
	 * The Jena Property for intStartedBy 
	 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property intStartedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy");

    /**
	 * The Jena Property for intFinishedBy 
	 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property intFinishedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy");

    /**
	 * The Jena Property for intOverlappedBy 
	 * <p>(URI: http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy)</p>
	 * <br>  
	 */
    public static com.hp.hpl.jena.rdf.model.Property intOverlappedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy");
}
