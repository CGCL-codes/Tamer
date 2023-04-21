package jp.go.aist.six.oval.model.definitions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jp.go.aist.six.oval.model.common.DatatypeEnumeration;

/**
 * The external variable extends the VariableType and
 * defines a variable with some external source.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: ExternalVariable.java 2281 2012-04-04 08:07:53Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class ExternalVariable extends VariableType {

    private final Collection<PossibleValueType> possible_value = new ArrayList<PossibleValueType>();

    private final Collection<PossibleRestrictionType> possible_restriction = new ArrayList<PossibleRestrictionType>();

    /**
     * Constructor.
     */
    public ExternalVariable() {
        this(null, 0);
    }

    public ExternalVariable(final String id, final int version) {
        this(id, version, null);
    }

    public ExternalVariable(final String id, final int version, final String comment) {
        this(id, version, comment, null);
    }

    public ExternalVariable(final String id, final int version, final String comment, final DatatypeEnumeration datatype) {
        super(id, version, comment, datatype);
    }

    /**
     */
    public void setPossibleValue(final Collection<? extends PossibleValueType> possible_values) {
        if (possible_values != possible_value) {
            possible_value.clear();
            if (possible_values != null && possible_values.size() > 0) {
                for (PossibleValueType value : possible_values) {
                    addPossibleValue(value);
                }
            }
        }
    }

    public boolean addPossibleValue(final PossibleValueType possible_value) {
        if (possible_value == null) {
            return false;
        }
        return this.possible_value.add(possible_value);
    }

    public Collection<PossibleValueType> getPossibleValue() {
        return possible_value;
    }

    public Iterator<PossibleValueType> iteratePossibleValue() {
        return possible_value.iterator();
    }

    /**
     */
    public void setPossibleRestriction(final Collection<? extends PossibleRestrictionType> restrictions) {
        if (restrictions != possible_restriction) {
            possible_restriction.clear();
            if (restrictions != null && restrictions.size() > 0) {
                for (PossibleRestrictionType restriction : restrictions) {
                    addPossibleRestriction(restriction);
                }
            }
        }
    }

    public boolean addPossibleRestriction(final PossibleRestrictionType restriction) {
        if (restriction == null) {
            return false;
        }
        return possible_restriction.add(restriction);
    }

    public Collection<PossibleRestrictionType> getPossibleRestriction() {
        return possible_restriction;
    }

    public Iterator<PossibleRestrictionType> iteratePossibleRestriction() {
        return possible_restriction.iterator();
    }

    @Override
    public VariableType.Type ovalGetVariableType() {
        return VariableType.Type.EXTERNAL;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ExternalVariable)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "external_variable[" + super.toString() + ", possible_value=" + getPossibleValue() + ", possible_restriction=" + getPossibleRestriction() + "]";
    }
}
