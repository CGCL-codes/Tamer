package com.altova.io;

import java.io.StringReader;

public class StringInput extends ReaderInput {

    private String content;

    public StringInput(String content) {
        super(new StringReader(content));
        this.content = content;
    }

    public String getString() {
        return content;
    }
}
