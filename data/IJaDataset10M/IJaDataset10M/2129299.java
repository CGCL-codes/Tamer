package IC.lir.operands;

import IC.SymbolTable.Symbol;
import IC.Types.TypesUtils;

public class Memory extends Operand {

    private final Symbol symbol;

    public Memory(Symbol symbol) {
        this.symbol = symbol;
    }

    public Memory() {
        this(null);
    }

    public String toString() {
        return (symbol == null) ? TypesUtils.THIS : "s" + symbol.getScopeUniqueID() + symbol.getId();
    }
}
