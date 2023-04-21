package nakayo.gameserver.network.aion.serverpackets;

import nakayo.gameserver.model.account.Account;
import nakayo.gameserver.model.account.PlayerAccountData;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

/**
 * In this packet Server is sending Character List to client.
 *
 * @author Nemesiss, AEJTester
 */
public class SM_CHARACTER_LIST extends PlayerInfo {

    /**
     * PlayOk2 - we dont care...
     */
    private final int playOk2;

    /**
     * Constructs new <tt>SM_CHARACTER_LIST </tt> packet
     */
    public SM_CHARACTER_LIST(int playOk2) {
        this.playOk2 = playOk2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, playOk2);
        Account account = con.getAccount();
        writeC(buf, account.size());
        for (PlayerAccountData playerData : account.getSortedAccountsList()) {
            writePlayerInfo(buf, playerData);
            writeD(buf, 0);
            writeD(buf, 0);
            writeD(buf, 0);
            writeC(buf, 0);
            writeC(buf, 0);
            writeB(buf, new byte[28]);
        }
    }
}
