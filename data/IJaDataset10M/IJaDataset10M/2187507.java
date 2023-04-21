package JavaTron;

/**
 * Defines an adapter class.  An adapter is designed to act as a filter
 * on the object, returning a customized toString() method.  It may
 * sometimes be necessary for users to get the underlying object, so
 * Adapter has one method, getSource().
 *
 * object's toString() method and make a 
 * @author Taylor Gautier
 * @version $Revision: 1.1 $
 */
public interface Adapter {

    /** 
   * Return the object that is the source object.
   */
    public Object getSource();
}
