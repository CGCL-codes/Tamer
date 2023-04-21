package org.modelibra.persistency.xml;

import org.modelibra.Domain;
import org.modelibra.config.DomainConfig;
import org.modelibra.persistency.xml.notes.Notes;

/**
 * Xml domain.
 * 
 * @author Dzenan Ridjanovic
 * @version 2008-10-23
 */
public class Xml extends Domain {

    private Notes notes;

    /**
	 * Constructs the domain and its model.
	 * 
	 * @param domainConfig
	 *            domain configuration
	 */
    public Xml(DomainConfig domainConfig) {
        super(domainConfig);
        notes = new Notes(this);
    }

    /**
	 * Gets the Notes model.
	 * 
	 * @return Notes model
	 */
    public Notes getNotes() {
        return notes;
    }
}
