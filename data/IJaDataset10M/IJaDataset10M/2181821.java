package net.jxta.util;

/**
 * This the interface that all selectable objects expose.
 *
 * <p/>
 * Applications programmers should treat this API as temporary, for now.
 *
 * <p/>
 * A SimpleSelectable object can register SimpleSelector objects so that
 * they are notified whenever this object chooses to report a change.
 *
 * <p/>
 * SimpleSelectors are SimpleSelectable, therefore selectors can be selected.
 *
 * <p/>
 * The change notification interface used to notify a selector is actually
 * specified in SimpleSelectable. As a result, certain implementations may also 
 * allow to register SimpleSelectables that are not Selectors. Selectors themselves do not allow that.
 *
 * @see SimpleSelector
 * @see AbstractSimpleSelectable
 */
public interface SimpleSelectable {

    /**
     * A simple reference object that can be put in a map instead of the one it refers to.
     * SimpleSelectable object often need to be put in maps where distinct objects are to be treated
     * as such, even if they are identical at a semantical level. However, some 
     * SimpleSelectable objects may have semantically equals() and hashCode() 
     * methods rather than the identity ones.
     *
     * <p/>
     * For that reason, whenever a SimpleSelectable needs to be used as a map or set key, its identity
     * reference should be used instead. All SimpleSelectable can return an identity reference. A given
     * SimpleSelectable always provides the same IdentityReference object. IdentityReference never overloads
     * hashCode() and equals() in a way that could make different objects be equal or that could provide
     * different results from invocation to invocation.
     */
    public static class IdentityReference {

        private final SimpleSelectable object;

        /**
         * Creates a new IdentityReference object
         *
         * @param object the selectable
         */
        public IdentityReference(SimpleSelectable object) {
            this.object = object;
        }

        /**
         * @return The object that this one refers to.
         */
        public SimpleSelectable getObject() {
            return object;
        }
    }

    /**
     * @return A canonical IdentityReference for this object.
     * A given SimpleSelectable always provides the same IdentityReference 
     * object. An IdentityReference must never overload hashCode() or equals()
     * in a way that could make different objects be equal or that could provide
     * different results from invocation to invocation.
     */
    public IdentityReference getIdentityReference();

    /**
     * Registers the given selector with this selectable object. This always 
     * causes one change event for this object to be reported through the 
     * selector. As a result, when selecting for a condition, it is not 
     * necessary to verify whether it has already happened or not; the next call 
     * to select will be able to detect it.
     *
     * @param s The SimpleSelector to register
     */
    public void register(SimpleSelector s);

    /**
     * Unregisters the given selector, so that it is no-longer notified when 
     * this object changes.
     *
     * @param s The SimpleSelector to unregister
     */
    public void unregister(SimpleSelector s);

    /**
     * This method is invoked when the given selectable object has changed. This
     * permits to cascade selectable objects, so that one reports a change when 
     * the other changes, without having to select it. This also permits 
     * implementation of this interface by delegating its implementation to a 
     * utility class.  
     * <p/>
     * An implementation may do what it wants about it. For example, a
     * {@link SimpleSelector} will report the change to 
     * {@link SimpleSelector#select} and invoke 
     * {@link AbstractSimpleSelectable#notifyChange()} thereby reporting its own
     * change to cascaded selectors.  Other implementations may only invoke 
     * {@link AbstractSimpleSelectable#notifyChange()} or may perform more 
     * complex tasks.
     *
     * @see AbstractSimpleSelectable
     *
     * @param changedObject the object that has changed.
     */
    public void itemChanged(SimpleSelectable changedObject);
}
