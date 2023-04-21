package monkey.parser;

import java.io.PrintWriter;

public class ShiftReduceConflictException extends ParserDataException {

    public ShiftReduceConflictException(int state, ItemSet itemSet, Item item, Grammar.Symbol terminal) {
        super("shift-reduce conflict", state, itemSet, item, terminal);
    }

    public void dump(PrintWriter out) {
        out.println();
        out.print("Shift-Reduce Conflict: ");
        out.println("For any item [A->x.Ty, Z] in ItemSet with T a terminal, there is no item in ItemSet of the from [B->x., T]");
        out.println("Terminal: " + getTerminal().getName());
        out.println("Item: " + getItem());
        out.println("ItemSet is:");
        getItemSet().dump(out);
    }
}
