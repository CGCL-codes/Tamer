package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.apache.log4j.Logger;

/**
 * Handler for "/loc" command
 *
 * @author SoulKeeper
 * @author EvilSpirit
 */
public class CM_CLIENT_COMMAND_LOC extends AionClientPacket {

    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(CM_CLIENT_COMMAND_LOC.class);

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     */
    public CM_CLIENT_COMMAND_LOC(int opcode) {
        super(opcode);
    }

    /**
     * Nothing to do
     */
    @Override
    protected void readImpl() {
    }

    /**
     * Logging
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        log.info("Received \"/loc\" command");
        sendPacket(SM_SYSTEM_MESSAGE.CURRENT_LOCATION(player.getWorldId(), player.getX(), player.getY(), player.getZ()));
    }
}
