package nsl.instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import nsl.*;
import nsl.expression.*;

/**
 * @author Stuart
 */
public class DirVarInstruction extends AssembleExpression {

    public static final String name = "DirVar";

    private final Expression value;

    /**
   * Class constructor.
   * @param returns the number of values to return
   */
    public DirVarInstruction(int returns) {
        if (!PageExInfo.in()) throw new NslContextException(EnumSet.of(NslContext.PageEx), name);
        if (returns > 0) throw new NslReturnValueException(name);
        ArrayList<Expression> paramsList = Expression.matchList();
        if (paramsList.size() != 1) throw new NslArgumentException(name, 1);
        this.value = paramsList.get(0);
        if (!ExpressionType.isRegister(this.value)) throw new NslArgumentException(name, 1, ExpressionType.Register);
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
