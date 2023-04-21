package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class L1InsertSpawn implements L1CommandExecutor {

    private static Logger _log = Logger.getLogger(L1InsertSpawn.class.getName());

    private L1InsertSpawn() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1InsertSpawn();
    }

    @Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        String msg = null;
        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String type = tok.nextToken();
            int npcId = Integer.parseInt(tok.nextToken().trim());
            L1Npc template = NpcTable.getInstance().getTemplate(npcId);
            if (template == null) {
                msg = "找不到符合條件的NPC。";
                return;
            }
            if (type.equalsIgnoreCase("mob")) {
                if (!template.getImpl().equals("L1Monster")) {
                    msg = "指定的NPC不是L1Monster類型。";
                    return;
                }
                SpawnTable.storeSpawn(pc, template);
            } else if (type.equalsIgnoreCase("npc")) {
                NpcSpawnTable.getInstance().storeSpawn(pc, template);
            }
            L1SpawnUtil.spawn(pc, npcId, 0, 0);
            msg = new StringBuilder().append(template.get_name()).append(" (" + npcId + ") ").append("新增到資料庫中。").toString();
        } catch (Exception e) {
            _log.log(Level.SEVERE, "", e);
            msg = "請輸入 : " + cmdName + " mob|npc NPCID 。";
        } finally {
            if (msg != null) {
                pc.sendPackets(new S_SystemMessage(msg));
            }
        }
    }
}
