package jelb.commands.bot;

import jelb.Locale;
import jelb.TradeBot;
import jelb.commands.Command;
import jelb.common.ParseException;
import jelb.struct.Account;
import jelb.struct.GuildMember;
import jelb.struct.Owner;

public class BotAddGuildMember extends Command<TradeBot> {

    public BotAddGuildMember(String name, String commandHelp) {
        super(name, commandHelp);
    }

    @Override
    public boolean isGlobalTradeLock() {
        return true;
    }

    @Override
    public boolean isPersonalTradeLock() {
        return false;
    }

    @Override
    public void invoke(TradeBot bot, String playerName, String[] params) {
        try {
            String cmd = jelb.common.Parser.parseString(params, 0, 0);
            if (cmd.equals("add")) {
                String player = jelb.common.Parser.parseString(params, 1);
                bot.data.getGuild().members.add(new GuildMember(player));
                bot.sendRawText("#accept " + player);
                bot.sendRawText("#change_rank " + player + " 6");
                bot.data.getAccounts().add(new Account(new Owner(player)));
                bot.appendAnserw(playerName, jelb.Locale.MSG_DONE_INFO);
                bot.sendGuildMessage("Welcome " + player + " I am " + bot.getLogin() + " Your Guild Trade Bot");
                bot.sendGuildMessage(Locale.MSG_GUILD_JOINED);
            } else if (cmd.equals("del")) {
                String player = jelb.common.Parser.parseString(params, 1);
                bot.data.getGuild().members.remove(player);
                bot.sendRawText("#remove " + player);
                bot.appendAnserw(player, "You got removed from our guild, the stuff on your account on me is seized");
                bot.appendAnserw(playerName, jelb.Locale.MSG_DONE_INFO);
            } else {
                throw new ParseException();
            }
        } catch (ParseException pex) {
            if (params != null && params.length > 0) this.onSyntaxError(bot, playerName); else bot.appendAnserw(playerName, bot.data.getGuild().members.toStringArray());
        }
    }
}
