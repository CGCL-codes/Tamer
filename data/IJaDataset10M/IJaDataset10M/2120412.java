package org.jrman.parser.keywords;

import org.jrman.parser.Tokenizer;

public class KeywordOrientation extends MotionKeywordParser {

    public void parse(Tokenizer st) throws Exception {
        match(st, TK_STRING);
    }
}
