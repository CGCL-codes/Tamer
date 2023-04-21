package jmud.command;

import jmud.Char;
import jmud.Player;
import jmud.Authenticable;

class NewbieLock extends Command {

    NewbieLock(String name, int mRank, int mPos, int opt) {
        super(name, mRank, mPos, opt);
    }

    static final String SYNTAX = "Sintaxe: iniciantes [habilite|desabilite]";

    static final String DISABLE = "desabilite";

    static final String ENABLE = "habilite";

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        Authenticable newbieLock = theManager.getNewbieLock();
        if (!toker.hasMoreTokens()) {
            if (newbieLock == null) {
                aChar.send("A cria��o de novos personagens est� habilitada.");
            } else aChar.send("Cria��o de personagens desabilitada por: " + newbieLock.getString());
            return;
        }
        String opt = toker.nextToken();
        if (DISABLE.startsWith(opt)) {
            if (newbieLock == null) {
                theManager.setNewbieLock(((Player) aChar).getCredentials());
                aChar.send("Desabilitando cria��o de novos personagens.");
            } else aChar.send("A cria��o de personagens j� est� desabilitada.");
        } else if (ENABLE.startsWith(opt)) {
            if (newbieLock == null) aChar.send("A cria��o de personagens j� est� habilitada."); else {
                if (aChar.getRank() < newbieLock.getRank()) {
                    aChar.send("Voc� n�o tem poder para remover a trava de cria��o de personagens.");
                    return;
                }
                theManager.setNewbieLock(null);
                aChar.send("Habilitando cria��o de novos personagens.");
            }
        } else aChar.send(SYNTAX);
    }
}
