package org.omg.CORBA;

/**
 * Stores exceptions that can be thrown when invoking a method of an
 * CORBA {@link org.omg.CORBA.Object}.
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org).
 */
public abstract class ExceptionList {

    /**
   * Add the typecode of the given exception to the list.
   */
    public abstract void add(TypeCode an_exception);

    /**
   * Get the number of the stored exceptions.
   */
    public abstract int count();

    /**
   * Get the item at the given position.
   * @param at the index
   * @throws Bounds if the index is out of range.
   */
    public abstract TypeCode item(int at) throws Bounds;

    /**
   * Remove the item at the given position.
   * @param at the index
   * @throws Bounds if the index is out of range.
   */
    public abstract void remove(int at) throws Bounds;
}
