package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Ignore characters until the end of line...
 * @author Michael A. MacDonald
 *
 */
class CommentState extends LexStateBase {

    /**
	 * @param parent
	 * @param reader
	 */
    public CommentState(LexState parent, IldasmReader reader) {
        super(parent, reader);
    }

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
        if (c == '\n') return m_parent;
        return this;
    }
}
