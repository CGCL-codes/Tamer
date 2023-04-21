package org.fernwood.jbasic.opcodes;

import java.math.BigDecimal;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Expression;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.value.Value;

/**
 * @author cole
 * 
 */
public class OpDIV extends AbstractOpcode {

    /**
	 * Divide top two stack items, push result back on stack.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        final Value sourceValue = env.pop();
        Value targetValue = env.popForUpdate();
        int bestType = Expression.bestType(targetValue, sourceValue);
        targetValue.coerce(bestType);
        switch(bestType) {
            case Value.DECIMAL:
                BigDecimal d1 = targetValue.getDecimal();
                BigDecimal d2 = sourceValue.getDecimal();
                BigDecimal d3 = d1.divide(d2, BigDecimal.ROUND_HALF_UP);
                targetValue.setDecimal(d3);
                break;
            case Value.DOUBLE:
                targetValue.setDouble(targetValue.getDouble() / sourceValue.getDouble());
                break;
            case Value.INTEGER:
                targetValue.setInteger(targetValue.getInteger() / sourceValue.getInteger());
                break;
            default:
                throw new JBasicException(Status.TYPEMISMATCH);
        }
        env.push(targetValue);
        return;
    }
}
