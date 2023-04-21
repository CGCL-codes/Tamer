package jmud.command;

import jmud.Char;
import jmud.Player;
import jmud.util.StrUtil;
import jmud.util.Separators;

class Priviledges extends Command {

    Priviledges(String name, int mRank, int mPos, int opt) {
        super(name, mRank, mPos, opt);
    }

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        final int WIDTH = 15;
        final int COLUMNS = 5;
        String cmdList = "";
        int trgRank = aChar.getRank();
        if (toker.hasMoreTokens()) {
            if (aChar.forbidAdminParam()) return;
            int rank = aChar.getRank();
            String trg = toker.nextToken();
            try {
                trgRank = Integer.parseInt(trg);
            } catch (NumberFormatException e) {
                Player plr = theWorld.findPlayerByName(trg);
                if (plr == null) {
                    aChar.send("Jogador n�o encontrado.");
                    return;
                }
                trgRank = plr.getRank();
            }
            if (trgRank > rank) trgRank = rank;
            for (int i = 0, j = 0; i < CommandTable.theCommands.length; ++i) {
                Command comm = CommandTable.theCommands[i];
                if (comm.isPriviledged() && trgRank >= comm.getMinRank()) {
                    cmdList += StrUtil.rightPad(comm.getName(), WIDTH);
                    if (++j % COLUMNS == 0) cmdList += Separators.NL;
                }
            }
        } else for (int i = 0, j = 0; i < CommandTable.theCommands.length; ++i) {
            Command comm = CommandTable.theCommands[i];
            if (comm.isPriviledged() && aChar.canExecute(comm)) {
                cmdList += StrUtil.rightPad(comm.getName(), WIDTH);
                if (++j % COLUMNS == 0) cmdList += Separators.NL;
            }
        }
        aChar.send("Privil�gios para posto " + trgRank + ":" + Separators.NL + cmdList);
    }
}
