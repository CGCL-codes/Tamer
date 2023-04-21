package com.io_software.catools.search;

import java.io.Serializable;
import com.abb.util.Pair;
import java.util.Set;

/** Describes an interface and a default implementation that takes an
    arbitrary object and converts it into a {@link Searchable}
    object. Subclasses may implement sophisticated strategies how this
    conversion works.<p>

    Instances of this class can be used to parameterize an {@link
    AttributeModel} model and cause a specific conversion to be
    applied to the attribute values when a query is to be applied to
    them.

    @version $Id: ValueToSearchableConverter.java,v 1.4 2001/01/15 09:45:07 aul Exp $
    @author Axel Uhl
*/
public class ValueToSearchableConverter implements Serializable {

    /** Takes an object and converts it into a {@link Searchable}
	object.<p>

	The default conversion strategy implemented here works as
	follows: If the passed object is a {@link String} or a {@link
	StringBuffer} or a {@link Char} object, the instance is
	converted into a {@link TextStringSearchable}. If the object is
	already instance of a subclass of {@link Searchable} then the
	object is taken as is. In all other cases the object is
	wrapped by a {@link ObjectWrapper} instance, making the object
	searchable by an {@link OQLQuery}.

	@param o the object to convert into a {@link Searchable}
	@return the converted object <tt>o</tt> as a searchable object
	according to the above rules.
      */
    public Searchable convert(Object o) {
        Searchable result;
        if (o instanceof String || o instanceof StringBuffer) result = new TextStringSearchable(o.toString()); else if (o instanceof Searchable) result = (Searchable) o; else result = new ObjectWrapper(o);
        return result;
    }
}
