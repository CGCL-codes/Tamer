package jmud.command;

import jmud.Char;
import jmud.util.Separators;
import jmud.util.StrUtil;
import jmud.util.log.Log;
import jmud.util.bit.Bit;
import jmud.util.BoolUtil;
import jmud.util.color.Color;
import jgp.container.Vector;
import jgp.adaptor.Finder;
import jgp.algorithm.Transformer;

class Wake extends Command {

    Wake(String name, int mRank, int mPos, int opt) {
        super(name, mRank, mPos, opt);
    }

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        if (aChar.isFighting()) {
            aChar.send("Voc� j� est� ocupado defendendo sua vida.");
            return;
        }
        if (aChar.getPosition() >= Char.P_RESTING) aChar.send("Voc� j� est� " + StrUtil.adjustSex("acordad*.", aChar.isFemale())); else {
            aChar.setPosition(Char.P_RESTING);
            aChar.getPlace().action("$p acordou.", true, aChar);
        }
    }
}
