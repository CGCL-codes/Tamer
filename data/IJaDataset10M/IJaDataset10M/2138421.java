package mix.asm;

public class SymbolNotFoundException extends MixAssemblerException {

    public SymbolNotFoundException(String symbol, java.io.StreamTokenizer st) {
        super("Cannot resolve symbol " + symbol, st);
    }
}
