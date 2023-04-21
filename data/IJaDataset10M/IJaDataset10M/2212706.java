package gov.sns.tools.data;

import java.util.List;

/** Generate a compound qualifier using the "or" operation. */
public class OrQualifier extends CompoundQualifier {

    /**
	 * Primary Constructor
	 * @param reserve the initial capacity reserved for holding qualifiers.
	 */
    public OrQualifier(final int reserve) {
        super(reserve);
    }

    /**
	 * Constructor with a variable number of qualifiers.
	 * @param qualfiers the qualfiers to append.
	 */
    public OrQualifier(final Qualifier... qualifiers) {
        this(qualifiers.length);
        for (final Qualifier qualifier : qualifiers) {
            append(qualifier);
        }
    }

    /**
	 * Constructor with a list of qualifiers.
	 * @param qualfiers the qualifiers to append.
	 */
    public OrQualifier(final List<? extends Qualifier> qualifiers) {
        this(qualifiers.size());
        for (final Qualifier qualifier : qualifiers) {
            append(qualifier);
        }
    }

    /** Constructor */
    public OrQualifier() {
        this(DEFAULT_RESERVE_CAPACITY);
    }

    /** 
	* Determine if the specified object satisfies the criteria of atleast one of the sub qualifiers.
	* @param object The object to test
	* @return true if the object is a match and false if not
	*/
    public boolean matches(final Object object) {
        for (int index = 0; index < _qualifierCount; index++) {
            if (_qualifiers[index].matches(object)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * The binary operator token.
	 * @return "|" to represent this "or" operation.
	 */
    @Override
    public String binaryToken() {
        return "|";
    }
}
