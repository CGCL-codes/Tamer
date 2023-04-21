package wotlas.server.message.description;

import wotlas.common.message.description.AccountRecoverMessage;
import wotlas.common.message.description.YourAccountDataMessage;
import wotlas.libs.net.NetMessageBehaviour;
import wotlas.server.PlayerImpl;
import wotlas.server.ServerDirector;

/**
 * Associated behaviour to the AccountRecoverMessage...
 *
 * @author Petrus
 */
public class AccountRecoverMsgBehaviour extends AccountRecoverMessage implements NetMessageBehaviour {

    /** Constructor.
     */
    public AccountRecoverMsgBehaviour() {
        super();
    }

    /** Associated code to this Message...
     *
     * @param sessionContext an object giving specific access to other objects needed to process
     *        this message.
     */
    public void doBehaviour(Object sessionContext) {
        PlayerImpl player = (PlayerImpl) sessionContext;
        player.sendMessage(new YourAccountDataMessage(this.primaryKey, player.getPlayerName(), ServerDirector.getServerID()));
    }
}
