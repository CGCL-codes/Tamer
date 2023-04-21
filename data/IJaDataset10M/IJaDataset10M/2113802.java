package de.bioutils.symbol.io.impl;

import java.util.ArrayList;
import java.util.Collection;
import de.bioutils.symbol.Alphabet;
import de.bioutils.symbol.Symbol;
import de.bioutils.symbol.io.SymbolParser;

/**
 *
 * TODO description
 * 
 * <p>
 * <b>Example:</b>
 * <pre>
 * TODO example
 * </pre>
 * </p>
 *
 * @autor <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version Oct 6, 2010
 *
 */
public abstract class AbstractSymbolParser implements SymbolParser {

    private final Alphabet alpha;

    /**
	 * 
	 * TODO description
	 *
	 */
    public AbstractSymbolParser(Alphabet alpha) {
        this.alpha = alpha;
    }

    public Symbol parseSymbol(String s) {
        return parseSymbol(s.charAt(0));
    }

    public Alphabet getAlphabet() {
        return alpha;
    }

    public Collection<? extends Symbol> parseSymbols(char[] chars) {
        final ArrayList<Symbol> result = new ArrayList<Symbol>();
        for (char c : chars) {
            result.add(parseSymbol(c));
        }
        return result;
    }

    public Collection<? extends Symbol> parseSymbols(String string) {
        return parseSymbols(string.toCharArray());
    }

    public boolean isValidSymbol(String s) {
        return isValidSymbol(s.charAt(0));
    }
}
