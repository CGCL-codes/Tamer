package parsers.sax.states.doctype;

import parsers.sax.states.*;
import parsers.sax.states.doctype.DOCTYPEWaitingBracketOrSystemState;
import java.util.Stack;
import parsers.sax.SAXHandler;
import parsers.sax.SAXParserException;

/**
 *
 * @author mateo
 */
public class ReadingDOCTYPENameState extends SAXParserState {

    private String doctypeName;

    ReadingDOCTYPENameState(String doctypeName, SAXHandler userHandler) {
        super(userHandler);
        this.doctypeName = doctypeName;
    }

    @Override
    public SAXParserState consumeCharacter(char c, Stack<String> stack, boolean escaped) throws SAXParserException {
        if ((c == ' ') || (c == '\t')) {
            return new DOCTYPEWaitingBracketOrSystemState(doctypeName, handler);
        } else if ((c == '\n') || (c == '\r')) {
            return new DOCTYPEWaitingBracketOrSystemState(doctypeName, handler);
        } else {
            doctypeName += c;
            return this;
        }
    }

    @Override
    public boolean canEscape() {
        return false;
    }

    @Override
    public boolean canFinalize() {
        return false;
    }
}
