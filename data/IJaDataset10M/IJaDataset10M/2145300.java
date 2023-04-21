package jelb.commands.guard;

import jelb.GuardBot;
import jelb.commands.Command;

public class LastKilledCommand extends Command<GuardBot> {

    public LastKilledCommand(String name, String commandHelp) {
        super(name, commandHelp);
    }

    @Override
    public void invoke(GuardBot bot, String playerName, String[] params) {
        bot.sendLastKilled(playerName);
    }

    @Override
    public boolean isGlobalTradeLock() {
        return false;
    }

    @Override
    public boolean isPersonalTradeLock() {
        return false;
    }
}
