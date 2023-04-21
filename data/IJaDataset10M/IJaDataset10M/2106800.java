package org.norecess.noparagraph.tags;

import org.antlr.runtime.Token;
import org.norecess.noparagraph.frontend.TagToken;
import org.norecess.noparagraph.statemachine.Configuration;

public class TableBodyTags extends TagToken {

    public TableBodyTags(Token token) {
        super(token);
    }

    @Override
    public Configuration inTable(Configuration configuration) {
        return configuration;
    }
}
