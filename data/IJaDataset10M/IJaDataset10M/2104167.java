package distrit;

/** A task that can also perform I/O during the task opearation
 * through get and set methods.
 *
 * @author Michael Garvie
 * @version
 */
public interface InteractiveTask extends Task {

    /** Used to get output from the task
     * @param params can be null if this task doesn't need to know WHAT it has to output
     * @return Any amount of information can be provided by
     * the task packaged in an Object.
     */
    public Object get(Object params);

    /** Used to send input to the task
     * @param paramsAndWhat this could be anything, either a single object if
     * the task knows what to do with it, or a container
     * with variable->value pairs..
     */
    public void set(Object paramsAndWhat);
}
