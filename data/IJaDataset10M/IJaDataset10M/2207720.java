package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_CreateParty extends ClientBasePacket {

    private static final String C_CREATE_PARTY = "[C] C_CreateParty";

    private static Logger _log = Logger.getLogger(C_CreateParty.class.getName());

    public C_CreateParty(byte decrypt[], ClientThread client) throws Exception {
        super(decrypt);
        L1PcInstance pc = client.getActiveChar();
        int type = readC();
        if (type == 0 || type == 1) {
            int targetId = readD();
            L1Object temp = L1World.getInstance().findObject(targetId);
            if (temp instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance) temp;
                if (pc.getId() == targetPc.getId()) {
                    return;
                }
                if (targetPc.isInParty()) {
                    pc.sendPackets(new S_ServerMessage(415));
                    return;
                }
                if (pc.isInParty()) {
                    if (pc.getParty().isLeader(pc)) {
                        targetPc.setPartyID(pc.getId());
                        targetPc.sendPackets(new S_Message_YN(953, pc.getName()));
                    } else {
                        pc.sendPackets(new S_ServerMessage(416));
                    }
                } else {
                    targetPc.setPartyID(pc.getId());
                    targetPc.sendPackets(new S_Message_YN(953, pc.getName()));
                }
            }
        } else if (type == 2) {
            String name = readS();
            L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
            if (targetPc == null) {
                pc.sendPackets(new S_ServerMessage(109));
                return;
            }
            if (pc.getId() == targetPc.getId()) {
                return;
            }
            if (targetPc.isInChatParty()) {
                pc.sendPackets(new S_ServerMessage(415));
                return;
            }
            if (pc.isInChatParty()) {
                if (pc.getChatParty().isLeader(pc)) {
                    targetPc.setPartyID(pc.getId());
                    targetPc.sendPackets(new S_Message_YN(951, pc.getName()));
                } else {
                    pc.sendPackets(new S_ServerMessage(416));
                }
            } else {
                targetPc.setPartyID(pc.getId());
                targetPc.sendPackets(new S_Message_YN(951, pc.getName()));
            }
        }
    }

    @Override
    public String getType() {
        return C_CREATE_PARTY;
    }
}
