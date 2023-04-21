package net.sf.buildbox.parser.builder;

import net.sf.buildbox.parser.model.Symbol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Petr Kozelka
 */
public class Sequence extends Symbol {

    private final List<SymbolRef> symbolRefs = new ArrayList<SymbolRef>();

    public void add(SymbolRef... symbolRefs) {
        Collections.addAll(this.symbolRefs, symbolRefs);
    }

    public List<SymbolRef> getSymbolRefs() {
        return symbolRefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sequence sequence = (Sequence) o;
        if (!symbolRefs.equals(sequence.symbolRefs)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + symbolRefs.hashCode();
        return result;
    }
}
