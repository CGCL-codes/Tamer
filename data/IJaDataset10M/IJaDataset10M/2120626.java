package org.cleartk.token.tokenizer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.cleartk.token.tokenizer.Token;
import org.junit.Test;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philip Ogren
 */
public class TokenTest {

    @Test
    public void testToken() {
        Token token = new Token(0, 0, "");
        assertEquals(token.getBegin(), 0);
        assertEquals(token.getEnd(), 0);
        assertEquals("", token.getTokenText());
        token = new Token(0, 0, null);
        assertEquals(token.getBegin(), 0);
        assertEquals(token.getEnd(), 0);
        assertEquals("", token.getTokenText());
        IllegalArgumentException iae = null;
        try {
            token = new Token(2, 0, "by");
        } catch (IllegalArgumentException e) {
            iae = e;
        }
        assertNotNull(iae);
        assertTrue(iae.getMessage().endsWith("begin=2, end=0, token text='by'"));
        iae = null;
        try {
            token = new Token(0, 3, "by");
        } catch (IllegalArgumentException e) {
            iae = e;
        }
        assertNotNull(iae);
        assertTrue(iae.getMessage().endsWith("token text length=2, (end - begin)=3, token text='by'"));
        token = new Token(0, 4, "asdf");
        assertEquals(token.getBegin(), 0);
        assertEquals(token.getEnd(), 4);
        assertEquals("asdf", token.getTokenText());
        Token token2 = new Token(0, 4, "asdf");
        assertTrue(token.equals(token2));
        assertEquals(token.hashCode(), token2.hashCode());
        assertEquals(token.hashCode(), "asdf".hashCode());
    }

    @Test
    public void miscTests() {
        assertFalse(new Token(0, 4, "asdf").equals("asdf"));
        assertEquals("asdf[0,4]", new Token(0, 4, "asdf").toString());
    }
}
