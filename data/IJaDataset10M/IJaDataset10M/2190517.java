package jmud.command;

import jmud.Char;
import jmud.Place;
import jmud.util.Separators;
import jmud.util.StrUtil;
import jmud.util.log.Log;
import jmud.util.bit.Bit;
import jmud.util.BoolUtil;
import jmud.util.color.Color;
import jgp.container.Vector;
import jgp.adaptor.Finder;
import jgp.algorithm.Transformer;

class Go extends Command {

    Go(String name, int mRank, int mPos, int opt) {
        super(name, mRank, mPos, opt);
    }

    static final String SYNTAX = "Sintaxe: va <n�mero da sala|nome do personagem>";

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        if (!toker.hasMoreTokens()) {
            aChar.send(SYNTAX);
            return;
        }
        IndexedToken indTok = toker.nextIndexedToken();
        String trg = indTok.getTarget();
        Place plc = null;
        if (trg == "") {
            int roomId = indTok.getIndex();
            plc = theWorld.findRoomById(roomId);
            if (plc == null) {
                aChar.send("Sala n�o existente.");
                return;
            }
        } else {
            Char vict = theWorld.findCharByName(indTok, aChar);
            if (vict == null) {
                aChar.send("Esse personagem n�o se encontra neste mundo.");
                return;
            }
            plc = vict.getPlace();
        }
        aChar.getPlace().actionNotToChar("$p desaparece em uma nuvem de fuma�a.", true, aChar);
        aChar.moveTo(plc);
        aChar.getPlace().actionNotToChar("$p aparece em uma explos�o brilhante.", true, aChar);
        aChar.look();
    }
}
