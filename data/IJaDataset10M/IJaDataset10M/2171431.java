package org.wsmostudio.grounding.sawsdl.ui.text;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class XMLScanner extends RuleBasedScanner {

    IToken procInstr;

    public XMLScanner(ColorManager manager) {
        procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
        IRule[] rules = new IRule[2];
        rules[0] = new SingleLineRule("<?", "?>", procInstr);
        rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());
        setRules(rules);
    }
}
