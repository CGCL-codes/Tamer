package org.jrman.parser.keywords;

import org.jrman.parameters.ParameterList;
import org.jrman.parser.Tokenizer;

public class KeywordAreaLightSource extends MotionKeywordParser {

    public void parse(Tokenizer st) throws Exception {
        match(st, TK_STRING);
        String name = st.sval;
        match(st, TK_NUMBER);
        int sequenceNumber = (int) st.nval;
        ParameterList parameters = parseParameterList(st);
        parser.createAreaLightSource(name, sequenceNumber, parameters);
    }
}
