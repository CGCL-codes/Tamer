package org.joogie.boogie.constants;

import java.util.HashMap;
import org.joogie.boogie.expressions.BoogieVariableFactory;
import org.joogie.boogie.expressions.Expression;
import org.joogie.boogie.types.BoogieTypeFactory;
import soot.PrimType;

/**
 * @author schaef
 * This is a very simple and imprecise implementation, but should be fine for now 
 * TODO improve
 */
public class RealConstantFactory {

    public static void resetFactory() {
        realConstants = new HashMap<Double, Expression>();
    }

    private static HashMap<Double, Expression> realConstants = new HashMap<Double, Expression>();

    public static Expression getRealConstant(Double c, PrimType t) {
        if (!realConstants.containsKey(c)) {
            realConstants.put(c, BoogieVariableFactory.getInstance().getFreshGlobalConstant(BoogieTypeFactory.lookupBoogieType(t)));
        }
        return realConstants.get(c);
    }
}
