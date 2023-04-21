package exec.functions;

import exec.Item;
import exec.ExecutionException;
import exec.types.BuiltIn;
import exec.types.Cons;
import exec.types.ErrSym;

/**
 * This function performs a conditional branch. It consists of two overloaded forms, one
 * with two arguments and one with three. For the purposes of this explanation, the
 * two-argument form is equivalent to the three-argument form with a third argument of NIL.
 * If other than two or three arguments are passed, an ARG_COUNT error will occur. Otherwise,
 * the first argument (the conditional) is evaluated. If it evaluates to true (non-NIL),
 * the second argument (the 'then' clause) is evaluated and its value is returned. If the
 * conditional evaluated to false, the third argument (the 'else' clause) is evaluated and
 * its value is returned. Note that if an error occurs in the processing of the conditional,
 * neither the 'then' nor the 'else' clause will be evaluated.
 * <p>
 *
 * @author Adam Berger <addaon@users.sourceforge.net>
 */
public final class If extends BuiltIn {

    public Item execute(Item caller, Item list) throws ExecutionException, java.io.IOException {
        int args = argCount(list);
        if (!((args == 2) || (args == 3))) {
            throw new ExecutionException(ErrSym.ARG_COUNT);
        } else {
            if (first(list).execute().equals(nil)) {
                if (args == 3) {
                    return third(list).execute();
                } else if (args == 2) {
                    return nil;
                } else {
                    throw new ExecutionException(ErrSym.INT_ERROR);
                }
            } else {
                return second(list).execute();
            }
        }
    }
}
