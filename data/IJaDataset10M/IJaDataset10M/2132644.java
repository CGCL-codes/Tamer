package org.tex4java.tex.parser.primitives;

import org.tex4java.Manager;
import org.tex4java.tex.parser.*;
import org.tex4java.tex.scanner.tokens.*;

/**
 * Parser for \futurelet.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.2 $
 */
public class FutureLetParser extends Parser {

    public FutureLetParser(Manager manager) {
        super(manager);
    }

    public void parse(Token token, TokenList tokens) throws Exception {
        Token t = tokens.get();
        if (t instanceof MacroToken) {
            Token tskip = tokens.get();
            Token t2 = tokens.get();
            if (t2 instanceof MacroToken) {
                Object cs = env.getScope().getControlSequence(((MacroToken) t2).macro);
                env.getScope().addControlSequence(((MacroToken) t).macro, cs);
            } else {
                env.getScope().addControlSequence(((MacroToken) t).macro, t2);
            }
            tokens.addFirst(t2);
            tokens.addFirst(tskip);
        } else {
            throw new Exception("Expect control sequence after \\let.");
        }
    }
}
