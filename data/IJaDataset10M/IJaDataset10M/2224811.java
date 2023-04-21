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

class Say extends Command {

    Say(String name, int mRank, int mPos, int opt) {
        super(name, mRank, mPos, opt);
    }

    public void execute(Char aChar, CommandTokenizer toker, String cmd) {
        if (!toker.hasMoreTokens()) {
            aChar.send("O que voc� quer falar?");
            return;
        }
        aChar.getPlace().action("$p disse '" + toker.getTail() + "'", false, aChar);
    }
}
