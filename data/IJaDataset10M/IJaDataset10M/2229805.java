package org.mantikhor.llapi;

import java.util.List;

/**
 * This interface describes items that can be assigned
 * properties of type <code>Decoration</code>.
 * 
 * Some of the methods defined by this interface are mutators,
 * that act to modify the <code>Decoration</code> assignments.
 * If an implementation's instances need to be immutable, those
 * methods must all throw an <code>UnsupportedOperationException</code>.
 * 
 * As a convenience, this interface provides a boolean method
 * <code>areDecorationsMutable()</code>, which is required to return
 * <code>true</code> if <em>any method, whether defined by this interface
 * or not</em>, can cause <code>this.getDecorations()</code> to
 * subsequently return a different value.
 * 
 * It's possible that mutator methods capable of modifying this 
 * instance's <code>Decorations</code> would be specified by a 
 * different interface, that is also implemented by the implementing instance.
 * In such a case, if those methods are disabled (e.g. for the purpose
 * of ensuring immutability), a well-behaved implementation would siganal
 * that disabled status by causing those methods to throw an 
 * <code>UnsupportedOperationException</code>.
 * 
 * 
 * @author Bill Blondeau
 *
 */
public interface Decorable {

    /**
     * Convenience method, describing whether the instance implemlents
     * any mutator methods capable of modifying the instance's
     * <code>Decorations</code> (as found in the return of 
     * <code>this.getDecorations()</code>).
     * 
     * @return
     */
    public abstract boolean areDecorationsMutable();

    /**
     * This method obeys the LLAPI General Modification Contract.
     * State parameter outcome relationship: the 
     * parameter is contained in the instance.
     * 
     * Since this method could change the instance's state, 
     * any implementation that does not simply throw an
     * <code>UnsupportedOperationException</code> is sufficient
     * to require <code>areDecorationsMutable()</code> to
     * return <code>true</code>.
     * 
     * @param decoration
     * @return <code>true</code> if the current instance's state was 
     *      changed by the execution of this method, else 
     *      <code>false</code>. 
     * 
     * @throws IllegalArgumentException if the parameter is 
     *      <code>null</code>
     * @throws UnsupportedOperationException if the implementation
     *      does not permit this mutator operation
     */
    public abstract boolean decorate(Decoration decoration) throws IllegalArgumentException, UnsupportedOperationException;

    /**
     * This method obeys the LLAPI General Modification Contract.
     * State parameter outcome relationship: the 
     * parameter is <strong>not</strong> contained in the instance.
     * 
     * Since this method could change the instance's state, 
     * any implementation that does not simply thow an
     * <code>UnsupportedOperationException</code> is sufficient
     * to require <code>areDecorationsMutable()</code> to
     * return <code>true</code>.
     * 
     * @param decoration
     * @return <code>true</code> if the current instance's state was 
     *      changed by the execution of this method, else 
     *      <code>false</code>. 
     * 
     * @throws IllegalArgumentException if the parameter is 
     *      <code>null</code>
     * @throws UnsupportedOperationException if the implementation
     *      does not permit this mutator operation
     */
    public abstract boolean undecorate(Decoration decoration) throws IllegalArgumentException, UnsupportedOperationException;

    /**
     * States whether the instance has any decorations at all.
     * 
     * Convenience method: equivalent to 
     * <code>(! this.getDecorations().isEmpty())</code>
     * @return
     */
    public abstract boolean isDecorated();

    /**
     * States how many properties of type <code>Decoration</code>
     * are contained in the instance.
     * 
     * Convenience method: equivalent to 
     * <code>this.getDecorations().size()</code>.
     * @return
     */
    public abstract int countDecorations();

    /**
     * Provides a <code>List</code> of all <code>Decoration</code>
     * properties contained in the instance. This <code>List</code>
     * is guaranteed to be ordered according to the <em>canonical ordering 
     * of properties</em> established by
     * <code>compareTo(<whatever>)</code>.
     * 
     * This method never returns a null value. If the instance contains 
     * no <code>Decoration</code>s, the returned <code>List</code> 
     * will be empty.
     * 
     * @return
     */
    public abstract List<Decoration> getDecorations();

    /**
     * States whether the parameter is contained in the 
     * instance. This is a convenience method, equivalent
     * to <code>this.contains(decoration)</code>. The reason
     * for its existence is to provide the functionality
     * in a manner more consistent with the idiom of
     * "decoration".
     * 
     * @param decoration
     * @return <code>this.contains(decoration)</code>
     */
    public abstract boolean isDecoratedBy(Decoration decoration);
}
