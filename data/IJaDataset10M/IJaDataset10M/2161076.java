package net.sf.parcinj;

/**
 * Compound symbol which contains several {@link Symbol}s. The symbol which
 * matches will be processed.
 */
public class Alternative<P extends Processor> extends AbstractSymbolContainer<P> {

    /**
   * Creates an instance without any symbol.
   */
    public Alternative() {
    }

    /**
   * Creates an instance with one symbol.
   */
    public Alternative(Symbol<P> symbol) {
        addSymbol(symbol);
    }

    /**
   * Appends the specified symbol.
   * 
   * @return this.
   */
    public AbstractSymbol<P> or(Symbol<P> symbol) {
        addSymbol(symbol);
        return this;
    }

    /**
   * Returns {@link MatchingResult#OK} if at least one symbol matches the
   * specified token. If no symbol matches the list of failure reasons are
   * returned.
   */
    public MatchingResult matches(Token token) {
        StringBuilder builder = new StringBuilder("Possible reasons:");
        for (int i = 0; i < _symbols.size(); i++) {
            Symbol<P> symbol = _symbols.get(i);
            MatchingResult result = symbol.matches(token);
            if (result.isSuccessful()) {
                return MatchingResult.OK;
            }
            builder.append(" (").append(i + 1).append("): ").append(result.getFailureReason());
        }
        return MatchingResult.failure(builder.toString());
    }

    /**
   * Processes the current token of the specified iterator by the first matching
   * symbol.
   */
    public void doProcess(TokenIterator iterator, P processor) {
        Token currentToken = iterator.currentToken();
        for (Symbol<P> symbol : _symbols) {
            if (symbol.matches(currentToken).isSuccessful()) {
                symbol.process(iterator, processor);
                break;
            }
        }
    }
}
