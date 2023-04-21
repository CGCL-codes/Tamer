package nsl.instruction;

import java.io.IOException;
import java.util.ArrayList;
import nsl.*;
import nsl.expression.*;

/**
 * @author Stuart
 */
public class SetOverwriteInstruction extends AssembleExpression {

    public static final String name = "SetOverwrite";

    private final Expression value;

    /**
   * Class constructor.
   * @param returns the number of values to return
   */
    public SetOverwriteInstruction(int returns) {
        if (returns > 0) throw new NslReturnValueException(name);
        ArrayList<Expression> paramsList = Expression.matchList();
        if (paramsList.size() != 1) throw new NslArgumentException(name, 1);
        this.value = paramsList.get(0);
        if (!ExpressionType.isString(this.value)) throw new NslArgumentException(name, 1, ExpressionType.String);
    }

    /**
   * Assembles the source code.
   */
    @Override
    public void assemble() throws IOException {
        AssembleExpression.assembleIfRequired(this.value);
        ScriptParser.writeLine(name + " " + this.value);
    }

    /**
   * Assembles the source code.
   * @param var the variable to assign the value to
   */
    @Override
    public void assemble(Register var) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }
}
