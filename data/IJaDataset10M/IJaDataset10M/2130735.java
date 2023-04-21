package net.sourceforge.pinyinlookup.engine.rules.impl;

import net.sourceforge.pinyinlookup.dict.Dict;
import net.sourceforge.pinyinlookup.engine.rules.IConversionContext;
import net.sourceforge.pinyinlookup.engine.rules.IPinyinRule;
import net.sourceforge.pinyinlookup.pinyin.PinyinChar;

/**
 * Rule to change the tone of "只" to 1 if preceded
 * by a number, else set it to 3.
 * 
 * @author Vincent Petry <PVince81@users.sourceforge.net>
 */
public class RuleZhiUnitTone implements IPinyinRule {

    /**
	 * @see net.sourceforge.pinyinlookup.engine.rules.IPinyinRule#processBlock(net.sourceforge.pinyinlookup.engine.rules.IConversionContext)
	 */
    @Override
    public void processBlock(IConversionContext context) {
        if (context.getCurrentChar().getHanzi() == '只') {
            PinyinChar previousChar = context.getPreviousChar();
            if (previousChar == null) {
                return;
            }
            PinyinChar c = context.getCurrentChar();
            if (previousChar.getHanzi() == '几' || Dict.isNumber(previousChar.getHanzi()) || (context.isPrecededBy("多少"))) {
                c.setTone(1);
            } else {
                c.setTone(3);
            }
        }
    }
}
