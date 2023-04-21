package org.paccman.calc.parser;

import java.math.MathContext;
import org.paccman.calc.parser.LexParser.ParseException;

/**
 *
 * @author joao
 */
public class CalcParser {

    LexParser lexParser;

    YaccParser yaccParser;

    /**
     * 
     * @param mathContext
     * @throws org.paccman.calc.parser.LexParser.ParseException
     */
    public CalcParser(MathContext mathContext) throws ParseException {
        yaccParser = new YaccParser(mathContext);
        lexParser = new LexParser(yaccParser);
        reset();
    }

    /**
     * Returns the current displayed value.
     * @return
     */
    public String getDisplay() {
        switch(lexParser.state) {
            case ParseOperand:
                return lexParser.getOperandDisplay();
            case Idle:
            case ReadOp:
            case WaitNext:
            case WaitOp:
                return yaccParser.getTopOperand().toString();
        }
        throw new IllegalStateException("Invalid state: " + lexParser.state.toString());
    }

    /**
     * 
     * @param c
     * @return
     * @throws org.paccman.calc.parser.LexParser.ParseException
     */
    public String parseChar(char c) throws ParseException {
        lexParser.parseChar(c);
        return getDisplay();
    }

    /**
     *
     * @throws org.paccman.calc.parser.LexParser.ParseException
     * 
     */
    public void reset() throws ParseException {
        lexParser.reset();
        yaccParser.reset();
    }
}
