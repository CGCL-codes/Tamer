package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Extends creates an inheritance relationship between two classes (instead of
 * classes, interfaces can also be used, since inheritance between interfaces
 * is also possible).
 * </p>
 * 
 * <p>
 * Performed stack operations:<br>
 * <code>pop superClass</code> (the class to be inherited)<br>
 * <code>pop subClassConstructor</code> (the constructor of the new class)<br>
 * </p>
 * 
 * <p>
 * ActionScript equivalent: <code>extends</code> keyword
 * </p>
 *
 * @since SWF 7
 */
public final class Extends extends Action {

    /**
   * Creates a new Extends action.
   */
    public Extends() {
        code = ActionConstants.EXTENDS;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"Extends"</code>
   */
    public String toString() {
        return "Extends";
    }
}
