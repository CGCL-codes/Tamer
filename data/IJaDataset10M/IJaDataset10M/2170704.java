package org.jrman.parser.keywords;

import org.jrman.parameters.ParameterList;
import org.jrman.parser.Tokenizer;

public class KeywordDisplay extends FrameKeywordParser {

    public void parse(Tokenizer st) throws Exception {
        match(st, TK_STRING);
        String name = st.sval;
        match(st, TK_STRING);
        String type = st.sval;
        match(st, TK_STRING);
        String mode = st.sval;
        ParameterList parameters = parseParameterList(st);
        parser.setDisplay(name, type, mode, parameters);
    }
}
