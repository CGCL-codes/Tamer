package org.xteam.cs.grm.build;

import java.util.HashSet;
import java.util.Set;
import org.xteam.cs.grm.model.NonTerminal;
import org.xteam.cs.grm.model.Rule;
import org.xteam.cs.grm.model.Symbol;

public class LRkItem extends LookaheadItem {

    private GrammarAnalysisModel model;

    public LRkItem(Rule p, Set<Word> lookahead, GrammarAnalysisModel model) {
        super(p);
        this.lookahead.addAll(lookahead);
        this.model = model;
    }

    protected LR0Item createItem(Rule p) {
        return new LRkItem(p, lookahead, model);
    }

    public boolean isSame(LR0Item i) {
        if (!super.isSame(i)) return false;
        if (!(i instanceof LRkItem)) return false;
        LRkItem other = (LRkItem) i;
        return other.lookahead.equals(lookahead);
    }

    public boolean computeLookahead(Set<Word> result) {
        if (atEnd()) throw new RuntimeException("cannot be at end");
        for (int pos = position + 1; pos < rhs.size(); ++pos) {
            Symbol sym = rhs.get(pos);
            if (sym.isTerminal()) {
                Set<Word> res = model.getFirstSet().at(getFrom(production, pos));
                result.addAll(removeEmpty(res));
                return false;
            } else {
                NonTerminal nt = (NonTerminal) sym;
                result.addAll(removeEmpty(model.getFirstSet().at(nt)));
                if (!model.getNullables().has(nt)) return false;
            }
        }
        result.addAll(lookahead);
        return true;
    }

    private Symbol getFrom(Rule production, int pos) {
        return production.getRhs().get(pos);
    }

    private Set<Word> removeEmpty(Set<Word> set) {
        Set<Word> res = new HashSet<Word>();
        for (Word word : set) {
            if (word.size() != 0) {
                res.add(word);
            }
        }
        return res;
    }
}
