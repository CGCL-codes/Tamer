package jmud.command;

import jmud.Char;

class Syntax extends Command {

    Syntax(String name, int mRank, int mPos, int opt, String syn) {
        super(name, mRank, mPos, opt, syn);
    }

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        if (!toker.hasMoreTokens()) {
            sendSyntax(aChar);
            return;
        }
        String cmdName = toker.nextToken();
        Command comm = CommandTable.findCommandByName(aChar, cmdName);
        if (comm == null) {
            aChar.send("Comando n�o encontrado.");
            return;
        }
        String syn = comm.getSyntax();
        if (syn == null) aChar.send("Sintaxe: n�o dispon�vel."); else comm.sendSyntax(aChar);
        if (aChar.isAdmin()) comm.sendProperties(aChar);
    }
}
