package org.tex4java.tex.parser.primitives;

import java.util.*;
import org.tex4java.Manager;
import org.tex4java.tex.parser.*;
import org.tex4java.tex.scanner.tokens.*;

/**
 * Parser for \divide.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.1.1.1 $
 */
public class DivideParser extends Parser {

    public DivideParser(Manager manager) {
        super(manager);
    }

    public void parse(Token token, TokenList tokens) throws Exception {
        Token t = getToken(tokens);
        if (t instanceof MacroToken) {
            Object rp = env.getScope().getControlSequence(((MacroToken) t).macro);
            if (rp instanceof GenericRegisterParser) {
                GenericRegisterParser grp = (GenericRegisterParser) rp;
                grp = grp.get(tokens);
                Object regValue = grp.getValue();
                GenericKeywordParser by = new GenericKeywordParser(manager);
                by.keywords = new LinkedList();
                by.keywords.add("by");
                by.parse(null, tokens);
                NumberParser number = new NumberParser(manager);
                number.parse(null, tokens);
                if (regValue instanceof Integer) {
                    grp.setValue(new Integer(((Integer) regValue).intValue() / (int) number.number));
                }
                if (regValue instanceof Double) {
                    grp.setValue(new Double(((Double) regValue).intValue() / (double) number.number));
                }
            } else {
                throw new Exception("Register expected.");
            }
        } else {
            throw new Exception("Register expected.");
        }
    }
}
