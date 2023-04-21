package inertial.math.functions;

import inertial.math.functions.compiler.ContinuousFunctionCompiler;
import inertial.math.functions.compiler.FunctionClass;
import inertial.math.functions.established.EstablishedFunctionFactory;
import inertial.math.numbers.RealNumber;
import inertial.math.points.Point;
import inertial.utils.string.StringUtility;
import java.util.ArrayList;
import java.util.List;
import static inertial.math.functions.FunctionWords.*;

public class ContinuousFunction extends Function implements MathematicalExpressionManipulator {

    private String expressionBody;

    private String sign;

    private boolean subFunction = false;

    private boolean compound = false;

    private List<ContinuousFunction> subFunctions = new ArrayList<ContinuousFunction>();

    private FunctionClass compiledClass;

    public ContinuousFunction(String functionBody) {
        this(functionBody, null, false);
        compiledClass = ContinuousFunctionCompiler.getInstance().compileFunction(this);
    }

    protected ContinuousFunction(String functionBody, String sign) {
        this(functionBody, sign, true);
    }

    protected ContinuousFunction(String functionBody, String sign, boolean subFunction) {
        this.expressionBody = findFunctionSign(functionBody.replaceAll(" ", ""));
        this.sign = sign;
        this.subFunction = subFunction;
        createSubFunctions();
        if (subFunctions.size() > 0) {
            compound = true;
        }
    }

    public RealNumber evaluate(Point point) throws FunctionException {
        try {
            return compiledClass.evaluate(point);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FunctionException();
        }
    }

    private String findFunctionSign(String functionBody) {
        String newFunctionBody = functionBody;
        if (functionBody.startsWith(SUBTRACTION_SIGN)) {
            this.sign = SUBTRACTION_SIGN;
            newFunctionBody = newFunctionBody.substring(1);
        } else if (functionBody.startsWith(ADDITION_SIGN)) {
            this.sign = ADDITION_SIGN;
            newFunctionBody = newFunctionBody.substring(1);
        } else {
            this.sign = ADDITION_SIGN;
        }
        return newFunctionBody;
    }

    private void createSubFunctions() {
        String[] levelSimbol = MathematicalExpressionUtility.getExpressionLevelSimbols(expressionBody);
        if (levelSimbol.length > 0) {
            List<String> subParts = StringUtility.splitWithRestriction(expressionBody, true, BEGIN_GROUP, END_GROUP, levelSimbol);
            String lastSign = ADDITION_SIGN;
            for (int i = 0; i < subParts.size(); i++) {
                String subFunctionBody = subParts.get(i);
                if (StringUtility.containString(levelSimbol, subFunctionBody)) {
                    lastSign = subFunctionBody;
                } else {
                    i++;
                    subFunctions.add(new ContinuousFunction(subFunctionBody, lastSign));
                    if (i < subParts.size()) {
                        lastSign = subParts.get(i);
                    }
                }
            }
        } else {
            if (expressionBody.startsWith(BEGIN_GROUP)) {
                expressionBody = StringUtility.findInside(expressionBody, BEGIN_GROUP, END_GROUP).get(0);
                createSubFunctions();
            } else if (expressionBody.indexOf(BEGIN_GROUP) != -1) {
                String subFunctionName = expressionBody.substring(0, expressionBody.indexOf(BEGIN_GROUP));
                String subFunctionBody = StringUtility.findInside(expressionBody, BEGIN_GROUP, END_GROUP).get(0);
                subFunctions.add(EstablishedFunctionFactory.getInstance().getSubFunction(subFunctionName, subFunctionBody));
            }
        }
    }

    public boolean isSubFunction() {
        return subFunction;
    }

    public boolean isCompound() {
        return compound;
    }

    public String getFunctionBody() {
        return expressionBody;
    }

    public String getSign() {
        return sign;
    }

    public List<ContinuousFunction> getSubFunctions() {
        return subFunctions;
    }
}
