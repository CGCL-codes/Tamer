package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Found a minus sign at the start of a token; may be the start of a number
 * @author Michael A. MacDonald
 *
 */
class InitialMinus extends LexStateBase {

    /**
	 * @param parent
	 * @param reader
	 */
    public InitialMinus(LexState parent, IldasmReader reader) {
        super(parent, reader);
    }

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
        LexState result;
        if (c == '.') {
            result = new FloatState(m_parent, m_reader, "-0.");
        } else if (CharClass.isDigit(c)) {
            result = new NumberState(m_parent, m_reader);
            result = result.nextCharacter('-');
            result = result.nextCharacter(c);
        } else {
            m_reader.processToken(IldasmParser.t_minus, "-");
            result = m_parent;
        }
        return result;
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
        m_reader.processToken(IldasmParser.t_minus, "-");
        return m_parent.endOfFile();
    }
}
