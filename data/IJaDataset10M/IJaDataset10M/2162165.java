package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexing a (possibly signed) decimal integer; if we find something that makes us
 * think it's a float, we'll change to that.
 * @author Michael A. MacDonald
 *
 */
class NumberState extends LexStateBase {

    private StringBuilder m_sb;

    /**
	 * @param parent
	 * @param reader
	 */
    public NumberState(LexState parent, IldasmReader reader) {
        super(parent, reader);
        m_sb = new StringBuilder();
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
        processToken();
        return m_parent.endOfFile();
    }

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
        LexState result = this;
        if (CharClass.isDigit(c) || (c == '-' && m_sb.length() == 0)) {
            m_sb.append(c);
        } else if (c == '.' || c == 'E' || c == 'e') {
            if (m_reader.expectedReserved("FLOAT64") != null) {
                if (c == '.') {
                    m_sb.append(c);
                } else m_sb.append(".0E");
                result = new FloatState(m_parent, m_reader, m_sb.toString());
            } else {
                processToken();
                result = m_parent.nextCharacter(c);
            }
        } else {
            processToken();
            result = m_parent.nextCharacter(c);
        }
        return result;
    }

    /**
	 * Send numeric value to parser
	 *
	 */
    private void processToken() throws LexException, RuleActionException {
        try {
            long value = Long.parseLong(m_sb.toString());
            m_reader.processToken(IldasmParser.t_INT64, new Long(value));
        } catch (NumberFormatException nfe) {
            throw new LexException("Bad integer format: " + nfe.getMessage());
        }
    }
}
