package de.bioutils.symbol.io.impl;

import de.bioutils.symbol.Alphabet;
import de.bioutils.symbol.DefaultSymbolToByteMapper;
import de.bioutils.symbol.Symbol;
import de.bioutils.symbol.SymbolToByteMapper;

/**
 * 
 * TODO description
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * TODO example
 * </pre>
 * 
 * </p>
 * 
 * @autor <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version Oct 6, 2010
 * 
 */
public class DefaultSymbolParser extends AbstractSymbolParser {

    public DefaultSymbolParser(Alphabet alpha) {
        super(alpha);
    }

    public Symbol parseSymbol(char c) {
        for (Symbol s : getAlphabet().getSymbols()) {
            if (s.toChar() == c) {
                return s;
            }
        }
        throw new NumberFormatException("Character \"" + c + "\" not known");
    }

    public boolean isValidSymbol(char c) {
        for (Symbol s : getAlphabet().getSymbols()) {
            if (s.toChar() == c) {
                return true;
            }
        }
        return false;
    }

    public SymbolToByteMapper getMapper() {
        return DefaultSymbolToByteMapper.getInstance();
    }
}
