package org.avaje.ebean.enhance.subclass;

/**
 * Used to generate a subclass based on a bean.
 * <p>
 * It does not have the fields or private methods of the read class. It replaces
 * the method code with calls to super instead. It may need to add hashCode()
 * and equals() methods to make sure a reference is loaded prior to either of
 * these methods being called. It uses writeReplace() to modify the
 * serialisation.
 * </p>
 */
public class SubClassGenerator {
}
