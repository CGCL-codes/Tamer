package de.uni_muenster.cs.sev.lethal.hom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import de.uni_muenster.cs.sev.lethal.symbol.common.BiSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.Variable;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;

/**
 * Represents a homomorphism between two alphabets in a very complex way.<br>
 * 
 * @see Hom
 * @see HomOps
 * 
 * @param <F> type of function symbols in source alphabet of the homomorphism
 * @param <G> type of function symbols occurring in images of the homomorphism
 * @param <V> type of homomorphism variables occurring in images of the homomorphism
 * 
 * @author Dorothea, Irene, Martin
 */
public class AbstractHom<F extends RankedSymbol, G extends RankedSymbol, V extends Variable> implements Hom<F, G, V> {

    /**
	 * Represents the homomorphism between the alphabets. <br> 
	 * A symbol maps to a tree with at most as many variables as the symbol's arity. 
	 * This suffices to characterise the homomorphism.
	 */
    protected Map<F, Tree<? extends BiSymbol<G, V>>> hom;

    /**
	 * Creates an empty homomorphism.
	 */
    public AbstractHom() {
        hom = new HashMap<F, Tree<? extends BiSymbol<G, V>>>();
    }

    /**
	 * Creates a homomorphism out of a given map that specifies the homomorphism.
	 * 
	 * @param h map which specifies the homomorphism 
	 */
    public AbstractHom(Map<F, Tree<? extends BiSymbol<G, V>>> h) {
        hom = new HashMap<F, Tree<? extends BiSymbol<G, V>>>(h);
    }

    /**
	 * @see de.uni_muenster.cs.sev.lethal.hom.Hom#containsSrcSymbol(de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol)
	 */
    @Override
    public boolean containsSrcSymbol(RankedSymbol symbol) {
        return hom.containsKey(symbol);
    }

    /**
	 * @see de.uni_muenster.cs.sev.lethal.hom.Hom#getSrcAlphabet()
	 */
    @Override
    public Collection<F> getSrcAlphabet() {
        return hom.keySet();
    }

    /**
	 * @see de.uni_muenster.cs.sev.lethal.hom.Hom#imageOf(de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol)
	 */
    @Override
    public Tree<? extends BiSymbol<G, V>> imageOf(RankedSymbol f) {
        return hom.get(f);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        String res = "Homomorphismn: \n";
        for (F symbol : hom.keySet()) {
            res += symbol.toString() + " -> " + hom.get(symbol).toString() + "\n";
        }
        return res;
    }
}
