package com.google.code.luar.syntax.input.impl;

import java.io.*;
import org.junit.Test;
import com.google.code.luar.syntax.input.*;
import static org.junit.Assert.*;

public class LexerInputImplTest {

    private String inputString;

    public LexerInputImplTest() {
        inputString = "Test input with something to feed the lexer input";
    }

    @Test
    public void testReadForLookAhead() throws IOException {
        LexerInput input = new LexerInputImpl(new StringReader(inputString), 10, 3);
        StringBuffer sb = new StringBuffer(inputString.length());
        while ((input.readForLookAhead(1)) != LexerInput.EOF) {
            sb.append(input.read());
        }
        assertEquals("Must be the inputString", inputString, sb.toString());
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        LexerInput input = new LexerInputImpl(new StringReader(inputString));
        input.close();
        input.readForLookAhead(1);
    }
}
