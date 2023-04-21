package ti.chimera.registry;

import oscript.data.Value;
import oscript.data.JavaBridge;

/**
 * Interface implemented by subscribers to the value of a node.  When the
 * node's value changes, it's new value will be published to all the node's
 * subscribers.
 * 
 * @author ;Rob Clark;a0873619;San Diego;;
 * @version 0.1
 */
public interface NodeSubscriber {

    /**
   * Called to publish the new node value to the subscriber.
   * 
   * @param node         the node doing the publishing
   * @param value        the node's new value
   */
    public void publish(Node node, Object value);

    /**
   * An implementation of the <code>NodeSubscriber</code> for script
   * functions.
   */
    public class ScriptFunctionNodeSubscriber implements NodeSubscriber {

        private Value fxn;

        /**
     * Class Constructor
     * 
     * @param fxn          the script function to call
     */
        public ScriptFunctionNodeSubscriber(Value fxn) {
            this.fxn = fxn;
        }

        /**
     * delegate to the script function
     */
        public void publish(Node node, Object value) {
            fxn.callAsFunction(new Value[] { JavaBridge.convertToScriptObject(node), JavaBridge.convertToScriptObject(value) });
        }

        /**
     * Overload equals() to be <code>true</code> if the wrapped fxns
     * are equal, so add/remove from list, etc., works right.
     */
        public boolean equals(Object obj) {
            return (obj instanceof ScriptFunctionNodeSubscriber) && ((ScriptFunctionNodeSubscriber) obj).fxn.equals(fxn);
        }

        /**
     * Overload hashCode() so add/remove from list, etc., works right.
     */
        public int hashCode() {
            return fxn.hashCode();
        }
    }
}
