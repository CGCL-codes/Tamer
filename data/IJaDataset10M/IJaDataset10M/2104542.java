package org.systemsbiology.apps.gui.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * Serializable and Persistent Object representing a mass spec instrument
 * <p> Accession is defined by the Ontology Lookup Service
 * <p>
 * Copyright (C) 2010 by Institute for Systems Biology,
 * Seattle, Washington, USA.  All rights reserved.
 *
 * This source code is distributed under the GNU Lesser
 * General Public License, the text of which is available at:
 *   http://www.gnu.org/copyleft/lesser.html
 *
 */
@Entity
public class MSInstrument extends LightEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2289719343646927196L;

    private Long id;

    private String accession;

    private String name;

    /**
	 * Construct a mass spec instrument
	 */
    public MSInstrument() {
    }

    /**
	 * Construct a mass spec instrument
	 * @param accession accession information as defined by the Ontology Lookup Service
	 * @param name name of instrument
	 */
    public MSInstrument(String accession, String name) {
        this.accession = accession;
        this.name = name;
    }

    /**
	 * Get identifier
	 * @return id
	 */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
	 * Set identifier
	 * @param id identifier
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Set accession information
	 * @param accession accession as defined by Ontology Lookup Service
	 */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
	 * Get accession information
	 * @return accession information as defined by the Ontology Lookup Service
	 */
    public String getAccession() {
        return accession;
    }

    /**
	 * Set the name
	 * @param name name of instrument
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get the name
	 * @return name of instrument
	 */
    public String getName() {
        return name;
    }

    public String toString() {
        String retString = "ID: \t" + this.id + "\n";
        retString += "Accession: \t" + this.accession + "\n";
        retString += "Name: \t" + this.name + "\n";
        return retString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MSInstrument)) {
            return false;
        }
        MSInstrument newObject = (MSInstrument) obj;
        return ((this.accession.equals(newObject.accession)) && (this.name.equals(newObject.name)));
    }
}
