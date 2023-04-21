package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import java.util.Random;
import l1j.server.server.ClientThread;
import l1j.server.server.FishingTimeController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_FishClick extends ClientBasePacket {

    private static final String C_FISHCLICK = "[C] C_FishClick";

    private static Logger _log = Logger.getLogger(C_FishClick.class.getName());

    private static Random _random = new Random();

    public C_FishClick(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);
        L1PcInstance pc = clientthread.getActiveChar();
        long currentTime = System.currentTimeMillis();
        long time = pc.getFishingTime();
        if (currentTime < (time + 500) && currentTime > (time - 500) && pc.isFishingReady()) {
            finishFishing(pc);
            int chance = _random.nextInt(200) + 1;
            if (chance >= 0 && chance < 30) {
                successFishing(pc, 41298);
            } else if (chance >= 30 && chance < 45) {
                successFishing(pc, 41300);
            } else if (chance >= 45 && chance < 60) {
                successFishing(pc, 41299);
            } else if (chance >= 60 && chance < 70) {
                successFishing(pc, 41296);
            } else if (chance >= 70 && chance < 80) {
                successFishing(pc, 41297);
            } else if (chance >= 80 && chance < 90) {
                successFishing(pc, 41301);
            } else if (chance >= 90 && chance < 100) {
                successFishing(pc, 41302);
            } else if (chance >= 100 && chance < 110) {
                successFishing(pc, 41303);
            } else if (chance >= 110 && chance < 120) {
                successFishing(pc, 41304);
            } else if (chance >= 120 && chance < 123) {
                successFishing(pc, 41306);
            } else if (chance >= 123 && chance < 126) {
                successFishing(pc, 41307);
            } else if (chance >= 126 && chance < 129) {
                successFishing(pc, 41305);
            } else if (chance >= 129 && chance < 132) {
                successFishing(pc, 21051);
            } else if (chance >= 132 && chance < 135) {
                successFishing(pc, 21052);
            } else if (chance >= 135 && chance < 138) {
                successFishing(pc, 21053);
            } else if (chance >= 138 && chance < 141) {
                successFishing(pc, 21054);
            } else if (chance >= 141 && chance < 144) {
                successFishing(pc, 21055);
            } else if (chance >= 144 && chance < 147) {
                successFishing(pc, 21056);
            } else if (chance >= 147 && chance < 157) {
                successFishing(pc, 41252);
            } else if (chance >= 157 && chance < 167) {
                successFishing(pc, 41308);
            } else if (chance >= 167 && chance < 197) {
                successFishing(pc, 500109);
            } else {
                pc.sendPackets(new S_ServerMessage(1136, ""));
            }
        } else {
            finishFishing(pc);
            pc.sendPackets(new S_ServerMessage(1136, ""));
        }
    }

    private void finishFishing(L1PcInstance pc) {
        pc.setFishingTime(0);
        pc.setFishingReady(false);
        pc.setFishing(false);
        pc.sendPackets(new S_CharVisualUpdate(pc));
        pc.broadcastPacket(new S_CharVisualUpdate(pc));
        FishingTimeController.getInstance().removeMember(pc);
    }

    private void successFishing(L1PcInstance pc, int itemId) {
        String message = null;
        switch(itemId) {
            case 41296:
                message = "$5249";
                break;
            case 41297:
                message = "$5250";
                break;
            case 41298:
                message = "$5256";
                break;
            case 41299:
                message = "$5257";
                break;
            case 41300:
                message = "$5258";
                break;
            case 41301:
                message = "$5259";
                break;
            case 41302:
                message = "$5260";
                break;
            case 41303:
                message = "$5261";
                break;
            case 41304:
                message = "$5262";
                break;
            case 41305:
                message = "$5264";
                break;
            case 41306:
                message = "$5263";
                break;
            case 41307:
                message = "$5265";
                break;
            case 21051:
                message = "$5269";
                break;
            case 21052:
                message = "$5270";
                break;
            case 21053:
                message = "$5271";
                break;
            case 21054:
                message = "$5272";
                break;
            case 21055:
                message = "$5273";
                break;
            case 21056:
                message = "$5274";
                break;
            case 41252:
                message = "$5248";
                break;
            default:
                break;
        }
        L1ItemInstance item = ItemTable.getInstance().createItem(itemId);
        if (item != null) {
            item.startItemOwnerTimer(pc);
            int dropX = 0;
            int dropY = 0;
            int x = pc.getX();
            int y = pc.getY();
            switch(pc.getHeading()) {
                case 1:
                    x--;
                    y++;
                    break;
                case 2:
                    x--;
                    break;
                case 3:
                    x--;
                    y--;
                    break;
                case 4:
                    y--;
                    break;
                case 5:
                    x++;
                    y--;
                    break;
                case 6:
                    x++;
                    break;
                case 7:
                    x++;
                    y++;
                    break;
                case 0:
                case 8:
                    y++;
                    break;
                default:
                    break;
            }
            dropX = x;
            dropY = y;
            L1World.getInstance().getInventory(dropX, dropY, pc.getMapId()).storeItem(item, "���ü���");
            pc.sendPackets(new S_ServerMessage(1185, message));
        }
    }

    @Override
    public String getType() {
        return C_FISHCLICK;
    }
}
