package org.phramer.v1.decoder.token;

import info.olteanu.interfaces.*;
import java.util.*;

public class TokenBuilderWordOnly implements TokenBuilder {

    private final StringFilter filter;

    public TokenBuilderWordOnly(StringFilter filter) {
        if (filter == null) filter = StringFilter.VOID;
        this.filter = filter;
    }

    public EToken build(FToken f) {
        assert f instanceof TokenWordOnly;
        if (filter == StringFilter.VOID) return (TokenWordOnly) f;
        return buildEToken(filter.filter(f.getWord()));
    }

    public FToken build(EToken f) {
        assert f instanceof TokenWordOnly;
        return (TokenWordOnly) f;
    }

    public EToken buildEToken(String word) {
        return buildToken(word);
    }

    public FToken buildFToken(String word) {
        return buildToken(word);
    }

    private HashMap<String, TokenWordOnly> myMap = new HashMap<String, TokenWordOnly>();

    private TokenWordOnly buildToken(String word) {
        TokenWordOnly k = myMap.get(word);
        if (k == null) {
            k = new TokenWordOnly(word);
            myMap.put(word, k);
        }
        return k;
    }

    public TokenBuilder getCloneForConcurrency() {
        return new TokenBuilderWordOnly(filter);
    }
}
