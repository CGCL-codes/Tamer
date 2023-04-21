package exec.functions;

import exec.ExecutionException;
import exec.Item;
import exec.types.BuiltIn;
import exec.types.Cons;
import exec.types.Decimal;
import exec.types.ErrSym;
import exec.types.Text;
import com.tce.math.TBigDecimal;

public final class Substring extends BuiltIn {

    public Item execute(Item caller, Item list) {
        argCount(list, 3);
        try {
            return new Text(((Text) ((Cons) list).getCar()).getData().substring(((Decimal) ((Cons) ((Cons) list).getCdr()).getCar()).getData().intValue(), ((Decimal) ((Cons) ((Cons) ((Cons) list).getCdr()).getCdr()).getCar()).getData().intValue()));
        } catch (IndexOutOfBoundsException e) {
            throw new ExecutionException(ErrSym.BAD_RANGE);
        }
    }
}
