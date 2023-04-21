package de.fuberlin.wiwiss.r2r.functions;

import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.math.BigDecimal;
import de.fuberlin.wiwiss.r2r.Function;
import de.fuberlin.wiwiss.r2r.FunctionFactory;
import de.fuberlin.wiwiss.r2r.functions.HelperFunctions.DataType;

public class MultiplyFunctionFactory implements FunctionFactory {

    private MultiplyFunction function = null;

    public Function getInstance() {
        if (function == null) function = new MultiplyFunction();
        return function;
    }

    private static class MultiplyFunction implements Function {

        public String getURI() {
            return "multiply";
        }

        public List<String> execute(List<List<String>> arguments, String hint) {
            List<String> result = new ArrayList<String>();
            DataType type = HelperFunctions.getDataTypeInformationOfAllArguments(arguments);
            type = HelperFunctions.takeMostAppropriateDataTypeForAddLikeCalculations(type, hint);
            if (type == DataType.String) throw new IllegalArgumentException("Non-numerical argument for multiply-function"); else if (type == DataType.Integer) {
                BigInteger bigIntResult = BigInteger.ONE;
                for (List<String> argument : arguments) bigIntResult = bigIntResult.multiply(new BigInteger(argument.get(0)));
                result.add(bigIntResult.toString());
            } else if (type == DataType.Int) {
                Integer intResult = 1;
                for (List<String> argument : arguments) intResult *= Integer.parseInt(argument.get(0));
                result.add(intResult.toString());
            } else if (type == DataType.Long) {
                Long longResult = 1l;
                for (List<String> argument : arguments) longResult *= Long.parseLong(argument.get(0));
                result.add(longResult.toString());
            } else if (type == DataType.Decimal) {
                BigDecimal decimalResult = null;
                boolean first = true;
                for (List<String> argument : arguments) if (first) {
                    first = false;
                    decimalResult = new BigDecimal(argument.get(0));
                } else decimalResult = decimalResult.multiply(new BigDecimal(argument.get(0)));
                result.add(decimalResult.toString());
            } else if (type == DataType.Double) {
                Double doubleResult = 1.0;
                for (List<String> argument : arguments) doubleResult *= Double.parseDouble(argument.get(0));
                result.add(doubleResult.toString());
            }
            return result;
        }
    }
}
