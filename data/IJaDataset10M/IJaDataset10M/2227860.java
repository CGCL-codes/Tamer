package org.fao.fenix.domain.info.content;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.info.content.attribute.Range;

/**
 * Fenix Content type which represents one recored or one element of
 * information. Usually represents one record of a Dataset.
 * 
 * FreeRange is Free range content.
 * 
 * Range content is content which specifies a numeric range.
 * 
 * Examples of FreeRange are 0..5, 2..4, 1001.2000, 0.0 .. 0..9 etc.
 * 
 */
@Entity
public class FreeRange extends Content {

    /**
	 * Numeric Range
	 */
    @Embedded
    Range range;

    public Range getRange() {
        return range;
    }

    /**
	 * @param range
	 * @throws FenixException
	 *             when not both values of range are filled
	 */
    public void setRange(Range range) {
        if (range.lowerValue == null || range.upperValue == null) {
            throw new FenixException("Both lowerValue and upperValue or range should be filled");
        }
        this.range = range;
    }
}
