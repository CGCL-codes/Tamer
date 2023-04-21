package org.freeform.tokens;

import java.io.IOException;

public interface TokenLex extends TokenIterator {

    boolean eat(String str) throws IOException;

    boolean peek(String str) throws IOException;

    void begin(Object e);

    Tokens tokens(Object e);

    void fail(Object e);

    void fail(Object e, Object reason);

    void ok(Object e);

    void pushLabel(String name);

    void popLabel(String name);

    boolean expect(String str) throws IOException;

    Object trace();

    Object failure();
}
