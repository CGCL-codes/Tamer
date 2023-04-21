package wotlas.libs.net.message;

import wotlas.libs.net.NetMessageBehaviour;

/** 
 * Associated behaviour to the EndOfConnectionMessage...
 *
 * @author Aldiss
 * @see wotlas.libs.net.message.EndOfConnectionMessage
 */
public class EndOfConnectionMsgBehaviour extends EndOfConnectionMessage implements NetMessageBehaviour {

    /** Constructor.
     */
    public EndOfConnectionMsgBehaviour() {
        super();
    }

    /** Associated code to the EndOfConnectionMessage... well, we do nothing special...
     *  the messages IDs were the only data...
     *
     * @param sessionContext an object giving specific access to other objects needed to process
     *        this message.
     */
    public void doBehaviour(Object sessionContext) {
    }
}
