package galronnlp.cfg.lexicon;

import galronnlp.util.Symbol;

/**
 * A rule for productions with a non-terminal LHS and a terminal RHS.
 *
 * @author Daniel A. Galron
 */
public interface LexicalEntry extends galronnlp.cfg.grammar.Rule {

    public Symbol category();

    public Symbol entry();
}
