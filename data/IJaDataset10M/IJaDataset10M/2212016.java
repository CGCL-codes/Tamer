package com.jaeksoft.searchlib.analysis.tokenizer;

import java.io.Reader;
import org.apache.lucene.analysis.Tokenizer;

public class RussianLetterTokenizer extends TokenizerFactory {

    @Override
    public Tokenizer create(Reader reader) {
        return new org.apache.lucene.analysis.ru.RussianLetterTokenizer(reader);
    }
}
