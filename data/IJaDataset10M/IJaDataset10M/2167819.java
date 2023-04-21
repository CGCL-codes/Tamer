package com.code316.flash;

import java.io.Reader;

public interface DeckBuilder {

    /**
 * 
 * @return com.code316.flash.Deck
 * @param uri java.lang.String
 */
    public Deck build(Reader in) throws java.io.IOException;
}
