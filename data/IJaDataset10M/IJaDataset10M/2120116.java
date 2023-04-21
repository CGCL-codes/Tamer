package parsers.sax;

import parsers.sax.states.PrologOrRootState;
import parsers.sax.states.SAXParserState;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

public class SAXParser {

    protected Stack<String> stack;

    protected SAXParserState state;

    protected SAXHandler handler;

    protected Attributes xmlDocumentAttributes;

    public SAXParser(SAXHandler handler) {
        stack = new Stack<String>();
        xmlDocumentAttributes = new Attributes();
        state = new PrologOrRootState(xmlDocumentAttributes, handler);
        this.handler = handler;
    }

    public void parse(InputStreamReader sb) throws java.io.IOException {
        BufferedReader br = new BufferedReader(sb);
        char[] c = new char[1];
        boolean eofReached = (-1 == br.read(c));
        while (!eofReached) {
            if (c[0] == '&' && state.canEscape()) {
                char[] auxCharArray = new char[3];
                br.read(auxCharArray);
                String threeCharString = "" + auxCharArray[0] + auxCharArray[1] + auxCharArray[2];
                if (threeCharString.equals("lt;")) {
                    c[0] = '<';
                    state = state.consumeCharacter(c[0], stack, true);
                } else if (threeCharString.equals("gt;")) {
                    c[0] = '>';
                    state = state.consumeCharacter(c[0], stack, true);
                } else if (threeCharString.equals("amp")) {
                    auxCharArray = new char[1];
                    if ((br.read(auxCharArray) != -1) && (auxCharArray[0] == ';')) {
                        c[0] = '&';
                        state = state.consumeCharacter(c[0], stack, true);
                    } else {
                        throw new SAXParserException("Invalid entity.");
                    }
                } else if (threeCharString.equals("quo")) {
                    auxCharArray = new char[2];
                    if ((br.read(auxCharArray) != -1) && (("" + auxCharArray[0] + auxCharArray[1]).equals("t;"))) {
                        c[0] = '"';
                        state = state.consumeCharacter(c[0], stack, true);
                    } else {
                        throw new SAXParserException("Invalid entity.");
                    }
                } else if (threeCharString.equals("apo")) {
                    auxCharArray = new char[2];
                    if ((br.read(auxCharArray) != -1) && (("" + auxCharArray[0] + auxCharArray[1]).equals("s;"))) {
                        c[0] = '\'';
                        state = state.consumeCharacter(c[0], stack, true);
                    } else {
                        throw new SAXParserException("Invalid entity.");
                    }
                } else {
                    throw new SAXParserException("Invalid entity.");
                }
            } else {
                state = state.consumeCharacter(c[0], stack, false);
            }
            eofReached = (-1 == br.read(c));
        }
        if (!state.canFinalize()) {
            throw new SAXParserException("The document was bad formatted or incomplete");
        } else {
            handler.endDocument();
        }
    }
}
