package cheesymock;

/**
 * Delegate that can be used to override selected methods/properties of a "real"
 * target. The class provides a "fallback" strategy where methods/properties
 * will primarily be resolved in the delegate and then in the wrapped target.
 * <code>WrappingDelegate</code> can be used in any method where
 * <code>{@link Cheesy}</code> accepts a delegate object. Usage:<code><pre>
 * 
 * UserObject myObject = createUserObject();
 * 
 *   // requires cglib, but works with interfaces as well.
 * Cheesy.mock( myObject.getClass(), new WrappingDelegate(myObject) {
 * 
 *   // override selected method/properties of UserObject. Any method
 *   // or property that is not overridden will be invoked on the wrapped
 *   // delegate 'myObject'.
 * 
 * });
 * 
 * @author Martin Algesten
 */
public class WrappingDelegate<T> {

    /**
     * Made protected so that it can be accessed in subclass. The name is
     * prefixed with '_' to make more likely to avoid name clashes with "real"
     * methods/properties.
     */
    protected T _target;

    /**
     * The given target will be invoked as a "fallback" strategy if the delegate
     * doesn't provide an implementation when resolving a method/property.
     * 
     * @param target
     */
    public WrappingDelegate(T target) {
        this._target = target;
    }
}
