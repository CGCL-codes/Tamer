package org.fao.fenix.domain.info.content;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * Fenix Content type which represents one recored or one element of
 * information. Usually represents one record of a Dataset.
 * 
 * Content type which holds a numeric value.
 * 
 */
@Entity
@MappedSuperclass
public class Numeric extends Content {

    /**
	 * The actual numeric value
	 */
    Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
