package parser.FPac.elements;

import lexer.CBaseToken;
import lexer.CTokenType;
import lexer.FPac.CFPacKeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Transcoder;

/**
 * @author S. Charton
 * @version $Id$
 */
public class CFPacSubr extends CFPacCodeBloc {

    /**
	 * @param line
	 */
    public CFPacSubr(int line) {
        super(line, null);
    }

    /**
	 * @see parser.FPac.CFPacElement#DoParsing()
	 */
    @Override
    protected boolean DoParsing() {
        CBaseToken tok = GetCurrentToken();
        if (tok.GetKeyword() == CFPacKeywordList.SUBR) {
            tok = GetNext();
            if (tok.GetType() == CTokenType.MINUS) {
                tok = GetNext();
            } else {
                return false;
            }
        }
        if (tok.GetType() == CTokenType.IDENTIFIER) {
            super.m_csName = tok.GetValue();
            tok = GetNext();
        } else {
            Transcoder.logError(getLine(), "Expecting IDENTIFIER");
            return false;
        }
        if (!super.DoParsing()) return false;
        tok = GetCurrentToken();
        if (tok.GetKeyword() == CFPacKeywordList.SUBREND) {
            m_nEndLine = tok.getLine();
            tok = GetNext();
        }
        return true;
    }

    /**
	 * @see parser.CBaseElement#ExportCustom(org.w3c.dom.Document)
	 */
    @Override
    protected Element ExportCustom(Document root) {
        return null;
    }
}
