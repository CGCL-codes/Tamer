package hu.schmidtsoft.parser.tokenizer.recognizer;

import hu.schmidtsoft.parser.language.ITokenType;

public class RecognizerWhiteSpace extends RecognizerAnyLetter {

    public RecognizerWhiteSpace(ITokenType tokenType) {
        super(tokenType, new Character[] { ' ', '\n', '\t', '\r' });
    }
}
