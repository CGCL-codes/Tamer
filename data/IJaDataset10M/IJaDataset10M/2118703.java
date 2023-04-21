package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.services.MailService;

/**
 * @author kosyachok
 */
public class CM_DELETE_MAIL extends AionClientPacket {

    int mailObjId;

    public CM_DELETE_MAIL(int opcode) {
        super(opcode);
    }

    @Override
    protected void readImpl() {
        mailObjId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        MailService.getInstance().deleteMail(player, mailObjId);
    }
}
