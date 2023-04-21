package handlers.voicedcommandhandlers;

import l2.universe.gameserver.handler.IVoicedCommandHandler;
import l2.universe.gameserver.model.entity.events.MonsterRush;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;

public class MonsterAss implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = { "mrjoin", "mrleave" };

    @Override
    public boolean useVoicedCommand(final String command, final L2PcInstance playerInstance, final String target) {
        if (command.equalsIgnoreCase("mrjoin")) MonsterRush.doReg(playerInstance); else if (command.equalsIgnoreCase("mrleave")) MonsterRush.doUnReg(playerInstance);
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}
