package de.bioutils.gff3.element.filter;

import de.bioutils.gff3.attribute.Attribute;
import de.bioutils.gff3.element.GFF3Element;

/**
 * <p>
 * Filtering for this filter is based on whether this
 * {@code GFF3Element} contains any {@code Attribute} with key equals {@code
 * attributeKey}.
 * </p>
 * 
 * @author Alexander Kerner
 * @lastVist 2009-11-03
 * @see GFF3Element
 * @see Attribute
 */
public class GFF3ElementAttributeKeyFilter implements GFF3ElementFilter {

    private final String attributeKey;

    /**
	 * 
	 * @param attributeKey
	 *            attribute key string, that must match {@code
	 *            Attribute.getKey()} (case sensitive) in order to make this
	 *            filter accept {@code element}.
	 */
    public GFF3ElementAttributeKeyFilter(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public boolean accept(GFF3Element e) {
        for (Attribute a : e.getAttributes()) {
            if (a.getKey().equals(attributeKey)) return true;
        }
        return false;
    }
}
