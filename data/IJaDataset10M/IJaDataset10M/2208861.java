package nakayo.gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import nakayo.gameserver.configs.main.CustomConfig;
import nakayo.gameserver.dao.PlayerPasskeyDAO;
import nakayo.gameserver.model.account.CharacterPasskey.ConnectType;
import nakayo.gameserver.model.account.PlayerAccountData;
import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import nakayo.gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import nakayo.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import nakayo.gameserver.services.PlayerService;

/**
 * In this packets aion client is requesting deletion of character.
 *
 * @author -Nemesiss-
 */
public class CM_DELETE_CHARACTER extends AionClientPacket {

    /**
     * PlayOk2 - we dont care...
     */
    @SuppressWarnings("unused")
    private int playOk2;

    /**
     * ObjectId of character that should be deleted.
     */
    private int chaOid;

    /**
     * Constructs new instance of <tt>CM_DELETE_CHARACTER </tt> packet
     *
     * @param opcode
     */
    public CM_DELETE_CHARACTER(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        playOk2 = readD();
        chaOid = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();
        PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(chaOid);
        if (playerAccData != null && !playerAccData.isLegionMember()) {
            if (CustomConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
                client.getAccount().getCharacterPasskey().setConnectType(ConnectType.DELETE);
                client.getAccount().getCharacterPasskey().setObjectId(chaOid);
                boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());
                if (!isExistPasskey) client.sendPacket(new SM_CHARACTER_SELECT(0)); else client.sendPacket(new SM_CHARACTER_SELECT(1));
            } else {
                PlayerService.deletePlayer(playerAccData);
                client.sendPacket(new SM_DELETE_CHARACTER(chaOid, playerAccData.getDeletionTimeInSeconds()));
            }
        } else {
            client.sendPacket(SM_SYSTEM_MESSAGE.STR_DELETE_CHARACTER_IN_LEGION());
        }
    }
}
