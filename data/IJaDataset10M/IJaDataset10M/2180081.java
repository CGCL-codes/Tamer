package org.jrman.parser.keywords;

import org.jrman.parser.Tokenizer;

public class KeywordScreenWindow extends FrameKeywordParser {

    public void parse(Tokenizer st) throws Exception {
        boolean array = false;
        int token = st.nextToken();
        if (token == TK_LBRACE) array = true; else st.pushBack();
        match(st, TK_NUMBER);
        float left = (float) st.nval;
        match(st, TK_NUMBER);
        float right = (float) st.nval;
        match(st, TK_NUMBER);
        float bottom = (float) st.nval;
        match(st, TK_NUMBER);
        float top = (float) st.nval;
        if (array) match(st, TK_RBRACE);
        parser.setScreenWindow(left, right, bottom, top);
    }
}
