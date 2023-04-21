package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_TradeOK extends ClientBasePacket {

    private static final String C_TRADE_CANCEL = "[C] C_TradeOK";

    private static Logger _log = Logger.getLogger(C_TradeOK.class.getName());

    public C_TradeOK(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);
        L1PcInstance player = clientthread.getActiveChar();
        L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
        if (trading_partner != null) {
            player.setTradeOk(true);
            if (player.getTradeOk() && trading_partner.getTradeOk()) {
                if (player.getInventory().getSize() < (180 - 16) && trading_partner.getInventory().getSize() < (180 - 16)) {
                    L1Trade trade = new L1Trade();
                    trade.TradeOK(player);
                } else {
                    player.sendPackets(new S_ServerMessage(263));
                    trading_partner.sendPackets(new S_ServerMessage(263));
                    L1Trade trade = new L1Trade();
                    trade.TradeCancel(player);
                }
            }
        }
    }

    @Override
    public String getType() {
        return C_TRADE_CANCEL;
    }
}
