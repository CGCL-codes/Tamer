package hu.schmidtsoft.parser.tokenizer.impl;

import hu.schmidtsoft.parser.language.ITokenType;
import hu.schmidtsoft.parser.tokenizer.ITokenRecognizer;
import hu.schmidtsoft.parser.tokenizer.ITokenizerDef;
import java.util.List;

public class TokenizerDef implements ITokenizerDef {

    List<ITokenRecognizer> recognizers;

    ITokenType eof;

    public ITokenType getEof() {
        return eof;
    }

    public void setEof(ITokenType eof) {
        this.eof = eof;
    }

    public List<ITokenRecognizer> getRecognizers() {
        return recognizers;
    }

    public TokenizerDef(List<ITokenRecognizer> recognizers) {
        super();
        this.recognizers = recognizers;
    }
}
