package org.fudaa.ctulu.gis.mif;

import java.util.Iterator;
import org.geotools.feature.AttributeType;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterType;
import org.geotools.filter.LengthFunction;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.LogicFilter;

/**
 * <p>
 * Proposal: AttributeType utilities class.
 * </p>
 * @author Luca S. Percich, AMA-MI
 * @source $URL$
 */
public final class AttributeTypes {

    private AttributeTypes() {
    }

    public static final int FIELD_LENGTH_UNDEFINED = 0;

    /**
     * <p>
     * Returns the field length defined via a restrinction for an attribute
     * type
     * </p>
     * 
     * <p>
     * This should be considered the maximum allowed length for the given
     * attribute, which means:
     * 
     * <ul>
     * <li>
     * the max number of chars in a <b>string</b>
     * </li>
     * <li>
     * the maximum precision for a <b>float</b>
     * </li>
     * <li>
     * no meaning for <b>Integer</b>, <b>Boolean</b>, <b>Date</b>?
     * </li>
     * </ul>
     * </p>
     *
     * @param _attr The attribute type
     * @param _defaultLength The default field length
     *
     * @return The defined field length, or defaultLength if no maximum length
     *         has been defined.
     */
    public static int getFieldLength(final AttributeType _attr, final int _defaultLength) {
        int length = getFieldLengthFromFilter(_attr.getRestriction());
        if (length == FIELD_LENGTH_UNDEFINED) {
            length = _defaultLength;
        }
        return length;
    }

    /**
     * <p>
     * Returns the field length defined via a restrinction for an attribute.
     * type
     * </p>
     *
     * @param _attr
     *
     * @return the defined field length, or FIELD_LENGTH_UNDEFINED if no
     *         maximum length has been defined.
     */
    public static int getFieldLength(final AttributeType _attr) {
        return getFieldLength(_attr, FIELD_LENGTH_UNDEFINED);
    }

    /**
     * <p>
     * Obtains a field length from a filter possibly containing a
     * LengthFunction expression.
     * </p>
     *
     * @param _filter the given filter
     *
     * @return The maximum field length found in the filter, or
     *         FIELD_LENGTH_UNDEFINED if none was found;
     */
    public static int getFieldLengthFromFilter(final Filter _filter) {
        int length = FIELD_LENGTH_UNDEFINED;
        if ((_filter != null) && (_filter != Filter.ALL) && (_filter != Filter.NONE)) {
            final short filterType = _filter.getFilterType();
            if ((filterType == FilterType.COMPARE_LESS_THAN) || (filterType == FilterType.COMPARE_LESS_THAN_EQUAL) || (filterType == FilterType.COMPARE_EQUALS)) {
                try {
                    final CompareFilter cf = (CompareFilter) _filter;
                    if (cf.getLeftValue() instanceof LengthFunction) {
                        length = Integer.parseInt(((LiteralExpression) cf.getRightValue()).getLiteral().toString());
                    } else {
                        if (cf.getRightValue() instanceof LengthFunction) {
                            length = Integer.parseInt(((LiteralExpression) cf.getLeftValue()).getLiteral().toString());
                        }
                    }
                    if (filterType == FilterType.COMPARE_LESS_THAN) {
                        length--;
                    }
                } catch (final NumberFormatException e) {
                }
            } else if ((filterType == FilterType.LOGIC_AND) || (filterType == FilterType.LOGIC_OR)) {
                for (final Iterator it = ((LogicFilter) _filter).getFilterIterator(); it.hasNext(); ) {
                    final Filter subFilter = (Filter) it.next();
                    final int subLength = getFieldLengthFromFilter(subFilter);
                    if (subLength > length) {
                        length = subLength;
                    }
                }
            }
        }
        return length;
    }
}
