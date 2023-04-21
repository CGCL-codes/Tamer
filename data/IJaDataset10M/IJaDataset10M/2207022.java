package org.goda.chronic.handlers;

import java.util.LinkedList;
import java.util.List;
import org.goda.chronic.Options;
import org.goda.chronic.utils.Token;
import org.goda.time.MutableInterval;

public class PSRHandler extends SRPHandler {

    @Override
    public MutableInterval handle(List<Token> tokens, Options options) {
        List<Token> newTokens = new LinkedList<Token>();
        newTokens.add(tokens.get(1));
        newTokens.add(tokens.get(2));
        newTokens.add(tokens.get(0));
        return super.handle(newTokens, options);
    }
}
