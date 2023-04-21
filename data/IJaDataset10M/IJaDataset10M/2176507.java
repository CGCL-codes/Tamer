package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.IDLEntity;

/**
 * Defines the instruction, how the newly specified policies can be
 * taken into consideration. The policies can be either
 * added to the current policies or replace them.
 *
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org)
 */
public class SetOverrideType implements Serializable, IDLEntity {

    /**
   * Use v 1.4 serialVersionUID for interoperability.
   */
    private static final long serialVersionUID = -2761857189425106972L;

    /**
   * Add the new policies to the existing policies.
   */
    public static final int _ADD_OVERRIDE = 1;

    /**
   * Replace the new existing policies by the new policies.
   */
    public static final int _SET_OVERRIDE = 0;

    /**
   * Add the new policies to the existing policies.
   * An instance of SetOverrideType, initialized to _ADD_OVERRIDE.
   */
    public static final SetOverrideType ADD_OVERRIDE = new SetOverrideType(_ADD_OVERRIDE);

    /**
   * Replace the new existing policies by the new policies.
   * An instance of SetOverrideType, initialized to _SET_OVERRIDE.
   */
    public static final SetOverrideType SET_OVERRIDE = new SetOverrideType(_SET_OVERRIDE);

    private final int _value;

    /**
   * No other instances can be created.
   */
    protected SetOverrideType(int a_value) {
        _value = a_value;
    }

    /**
   * Returns the SetOverrideType, matching the given integer constant
   * @param kind one of _ADD_OVERRIDE or _SET_OVERRIDE.
   *
   * @return one of ADD_OVERRIDE or SET_OVERRIDE.
   *
   * @throws BAD_PARAM if the parameter is not one of these two values.
   */
    public static SetOverrideType from_int(int kind) {
        switch(kind) {
            case _ADD_OVERRIDE:
                return ADD_OVERRIDE;
            case _SET_OVERRIDE:
                return SET_OVERRIDE;
            default:
                throw new BAD_PARAM("invalid add/override type " + kind);
        }
    }

    /**
   * Returns a short string representation.
   *
   * @return either "add" or "replace".
   */
    public String toString() {
        return (_value == _ADD_OVERRIDE) ? "add" : "replace";
    }

    /**
   * Returns the value, representing stored instruction.
   *
   * @return one of ADD_OVERRIDE or SET_OVERRIDE
   */
    public int value() {
        return _value;
    }
}
