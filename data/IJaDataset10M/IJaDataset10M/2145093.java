package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.VisibleObject;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.utils.i18n.CustomMessageId;
import nakayo.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Phantom
 */
public class AddSkill extends AdminCommand {

    public AddSkill() {
        super("스킬");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDSKILL) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }
        if (params.length != 2) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSKILL_SYNTAX));
            return;
        }
        VisibleObject target = admin.getTarget();
        int skillId = 0;
        int skillLevel = 0;
        try {
            skillId = Integer.parseInt(params[0]);
            skillLevel = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
            return;
        }
        if (target instanceof Player) {
            Player player = (Player) target;
            player.getSkillList().addSkill(player, skillId, skillLevel, true);
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, skillId, player.getName()));
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, admin.getName()));
        }
    }
}
