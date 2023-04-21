package visolate.simulator;

import java.util.*;

public abstract class MacroExpression {

    private static final String cvsid = "$Id: MacroExpression.java,v 1.1.1.1 2004/06/24 05:46:01 vona Exp $";

    public abstract double getValue(final List<Double> actuals);

    public abstract String toString();
}
