package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Populates an object's member (either a property or a method) with a given
 * value. If the member does not exist, it is created. Otherwise, it's value
 * is overwritten.
 * </p>
 * 
 * <p>
 * Performed stack operations:<br>
 * <code>pop value</code> (the new value of the member)<br>
 * <code>pop name</code> (the member's name)<br>
 * <code>pop ref</code> (reference to the object to be accessed)<br>
 * </p>
 * 
 * <p>
 * ActionScript equivalents:<br>
 * 
 * <ul>
 * <li>
 * member assignment (with or without dot operator), e.g.
 * <code>speed=100;</code> or <code>car.speed=100;</code>
 * </li>
 * <li>
 * internally used by the AS compiler to implement various constructs
 * </li>
 * </ul>
 * </p>
 *
 * @since SWF 5
 */
public final class SetMember extends Action {

    /**
   * Creates a new SetMember action.
   */
    public SetMember() {
        code = ActionConstants.SET_MEMBER;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"SetMember"</code>
   */
    public String toString() {
        return "SetMember";
    }
}
