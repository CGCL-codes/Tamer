package franklinmath.math;

import java.util.Vector;
import franklinmath.executor.*;
import franklinmath.expression.*;

/**
 *
 * @author Allen Jordan
 */
public class CosCommand extends Command {

    @Override
    public FMResult Execute(Vector<Equation> args, ExpressionToolset expressionToolset) throws CommandException {
        CheckArgsLength(args, 1);
        try {
            FMNumber number = GetNumberArgument(args, 0);
            double result = StrictMath.cos(number.doubleValue());
            return new FMResult(new Factor(new FMNumber(result)));
        } catch (Exception ex) {
            try {
                return new FMResult(new Factor(new SymbolicFunction(GetName(), args, isMathFunction)));
            } catch (ExpressionException ex2) {
                throw new CommandException(ex2.toString(), GetName());
            }
        }
    }
}
