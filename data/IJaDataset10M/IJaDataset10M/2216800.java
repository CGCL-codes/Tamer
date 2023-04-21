package gov.lanl.tools.data.context;

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
	 * Constructor with two qualifiers.
	 * @param firstQualifier the first qualifier to append.
	 * @param secondQualifier the second qualifier to append.
	 */
    public OrQualifier(final IQualifier firstQualifier, final IQualifier secondQualifier) {
        this(2);
        append(firstQualifier).append(secondQualifier);
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
    public String binaryToken() {
        return "|";
    }
}
