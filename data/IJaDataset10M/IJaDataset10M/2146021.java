package hu.schmidtsoft.parser.tokenizer.impl;

import hu.schmidtsoft.parser.tokenizer.IToken;
import hu.schmidtsoft.parser.tokenizer.ITokenFilterDef;
import java.util.ArrayList;
import java.util.List;

public class TokenFilter {

    ITokenFilterDef tokenFilterDef;

    public TokenFilter(ITokenFilterDef tokenFilterDef) {
        this.tokenFilterDef = tokenFilterDef;
    }

    public List<IToken> filter(List<IToken> toks) {
        List<IToken> ret = new ArrayList<IToken>();
        for (IToken t : toks) {
            if (!tokenFilterDef.getToFilter().contains(t.getTokenType().getName())) {
                ret.add(t);
            }
        }
        return ret;
    }
}
