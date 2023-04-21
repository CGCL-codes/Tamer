package admincommands;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.drop.DropList;
import nakayo.gameserver.model.drop.DropTemplate;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.services.DropService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.utils.i18n.CustomMessageId;
import nakayo.gameserver.utils.i18n.LanguageHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ATracer
 */
public class AddDrop extends AdminCommand {

    public AddDrop() {
        super("드롭추가");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDDROP) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }
        if (params.length != 5) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDDROP_SYNTAX));
            return;
        }
        try {
            final int mobId = Integer.parseInt(params[0]);
            final int itemId = Integer.parseInt(params[1]);
            final int min = Integer.parseInt(params[2]);
            final int max = Integer.parseInt(params[3]);
            final int chance = Integer.parseInt(params[4]);
            DropList dropList = DropService.getInstance().getDropList();
            DropTemplate dropTemplate = new DropTemplate(mobId, itemId, min, max, chance);
            dropList.addDropTemplate(mobId, dropTemplate);
            DB.insertUpdate("INSERT INTO droplist (" + "`mobId`, `itemId`, `min`, `max`, `chance`)" + " VALUES " + "(?, ?, ?, ?, ?)", new IUStH() {

                @Override
                public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, mobId);
                    ps.setInt(2, itemId);
                    ps.setInt(3, min);
                    ps.setInt(4, max);
                    ps.setInt(5, chance);
                    ps.execute();
                }
            });
        } catch (Exception ex) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETERS_ONLY));
            return;
        }
    }
}
