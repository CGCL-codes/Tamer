package gdg.dataGeneration.language;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author Silvio Donnini
 */
public class Random extends Function {

    /** Creates a new instance of Random */
    public Random(List<Expression> arguments) {
        super(arguments);
    }

    public String evaluate(BigInteger index) {
        String result = null;
        BigInteger indexArg1;
        BigInteger indexArg2;
        Expression arg1;
        Expression arg2;
        BigInteger cardArg1;
        arg1 = arguments.get(0);
        arg2 = arguments.get(1);
        cardArg1 = arg1.getCardinality();
        indexArg1 = index;
        indexArg2 = index.subtract(cardArg1);
        result = (index.compareTo(cardArg1) == -1) ? arg1.evaluate(indexArg1) : arg2.evaluate(indexArg2);
        return result;
    }

    public BigInteger getCardinality() {
        BigInteger result = BigInteger.ZERO;
        for (Expression arg : arguments) {
            result = result.add(arg.getCardinality());
        }
        return result;
    }

    public void print(String indent) {
        Logger.getLogger("application").info("random(");
        arguments.get(0).print("");
        Logger.getLogger("application").info(",");
        arguments.get(1).print("");
        Logger.getLogger("application").info(")");
    }
}
