package net.sf.ij_plugins.util;

import net.sf.ij_plugins.IJPluginsRuntimeException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for representing enumeration. Based on pattern presented by Joshua Bloch in
 * "Effective Java" 2001.
 *
 * @author Jarek Sacha
 * @since June 18, 2002
 */
public abstract class Enumeration {

    private static List<Enumeration> allMembers = new ArrayList<Enumeration>();

    private final String name;

    /**
     * Constructor for the Enumeration object
     *
     * @param name Description of Parameter
     */
    protected Enumeration(final String name) {
        try {
            byName(name);
            throw new IJPluginsRuntimeException("An Enumeration cannot have two members with the same name ['" + name + "].");
        } catch (final IllegalArgumentException ex) {
        }
        this.name = name;
        allMembers.add(this);
    }

    /**
     * Returns a reference to the named member of this Enumeration, throes IllegalArgumentException
     * of name is not found.<p>
     * <p/>
     * <strong>API NOTE:</strong> This method looks as it should have been defines as static.
     * However, this could lead to an unpredictable behavior of this method due to the way Java
     * initializes static member variables of a class. In particular, if this method was static it
     * would be possible to call it before static member variables of the Enumeration class for
     * which it was called were initialized. As a result if this method was static it could throw
     * IllegalArgumentException even when its argument was a valid member name.
     *
     * @param memberName A name of the Enumeration member.
     * @return Reference to a member with given <code>memberName</code>.
     * @throws IllegalArgumentException If a member with given <code>memberName</code> cannot be
     *                                  found.
     */
    public final Enumeration byName(final String memberName) throws IllegalArgumentException {
        for (final Enumeration member : allMembers) {
            if (member.name.equals(memberName)) {
                return member;
            }
        }
        throw new IllegalArgumentException("Invalid member name '" + memberName + "'.");
    }

    /**
     * Return name a
     *
     * @return Description of the Returned Value
     */
    @Override
    public final String toString() {
        return name;
    }
}
