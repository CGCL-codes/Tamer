package de.bioutils.symbol;

import java.util.Collection;
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
 * @version Oct 5, 2010
 *
 */
public abstract class AbstractSymbolSequenceModifiable extends AbstractSymbolSequence implements SymbolSequenceModifiable {

    private static final long serialVersionUID = 5007169830852464919L;

    public AbstractSymbolSequenceModifiable(char[] chars, SymbolParser parser, SymbolToByteMapper mapper) {
        super(chars, parser, mapper);
    }

    public AbstractSymbolSequenceModifiable(Collection<? extends Symbol> symbols, SymbolParser parser, SymbolToByteMapper mapper) {
        super(symbols, parser, mapper);
    }

    public AbstractSymbolSequenceModifiable(String string, SymbolParser parser, SymbolToByteMapper mapper) {
        super(string, parser, mapper);
    }

    public AbstractSymbolSequenceModifiable(SymbolParser parser, SymbolToByteMapper mapper) {
        super(parser, mapper);
    }

    public AbstractSymbolSequenceModifiable(SymbolParser parser) {
        super(parser);
    }
}
