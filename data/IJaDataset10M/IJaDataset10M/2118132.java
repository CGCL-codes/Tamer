package jaskuss.ldr;

/**There's one InstantiationDescriptor per every instance
 * that gets created by using the jaskuss.ldr.Instantiator.
 *
 * @author martin.vahi(the_glowing_A)eesti.ee
 */
public class InstantiationDescriptor {

    public boolean already_instantiated_ = false;

    /** The instantiation_in_progress==true, if the
     * instantiation process has started, but the 
     * instantiation is not yet complete.
     */
    public boolean instantiation_in_progress_ = false;

    /** The fully qualified name of the class.*/
    public String class_name_;

    public Object reference_ = null;
}
