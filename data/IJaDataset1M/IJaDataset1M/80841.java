package com.bm.introspectors;

import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class represents informations about PK fields.
 * 
 * @author Daniel Wiese
 * 
 */
public class PrimaryKeyInfo {

    private GeneratedValue genValue;

    private final Id idValue;

    private final EmbeddedId embeddedIDValue;

    /**
	 * Default constructor.
	 * 
	 * @param genType -
	 *            the generator type
	 */
    public PrimaryKeyInfo(Id id) {
        this.idValue = id;
        this.embeddedIDValue = null;
    }

    /**
	 * Default constructor.
	 * 
	 * @param genType -
	 *            the generator type
	 */
    public PrimaryKeyInfo(EmbeddedId id) {
        this.embeddedIDValue = id;
        this.idValue = null;
    }

    /**
	 * Returns the genType.
	 * 
	 * @return Returns the genType.
	 */
    public GeneratedValue getGenValue() {
        return genValue;
    }

    /**
	 * Returns the id.
	 * 
	 * @return the id
	 */
    public Id getIDValue() {
        return this.idValue;
    }

    /**
	 * Sets the gen value.
	 * @param genValue - the gen value.
	 */
    public void setGenValue(GeneratedValue genValue) {
        this.genValue = genValue;
    }

    /**
	 * Returns the embeddedIDValue.
	 * @return Returns the embeddedIDValue.
	 */
    public EmbeddedId getEmbeddedIDValue() {
        return embeddedIDValue;
    }
}
