package org.tex4java.tex.parser.primitives;

import org.tex4java.Manager;
import org.tex4java.tex.environment.*;
import org.tex4java.tex.parser.*;
import org.tex4java.tex.scanner.tokens.*;

/**
 * Parser for a SubScriptToken.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.2 $
 */
public class SubScriptParser extends MathShiftParser {

    public SubScriptParser(Manager manager) {
        super(manager);
    }

    public void parse(Token token, TokenList tokens) throws Exception {
        if (!(env.peekMode() instanceof MathMode)) {
            throw new Exception("Subscript outside of MathMode");
        }
        MathList mlist = env.getScope().mlist;
        scanMath(tokens, mlist, Scope.ML_SUBSCRIPT);
    }
}
