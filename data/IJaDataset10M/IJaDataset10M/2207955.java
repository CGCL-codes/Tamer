package org.eoti.math;

import org.eoti.util.StringUtil;
import java.util.*;
import java.util.regex.*;

public class Expression {

    public enum Operator {

        Divide("/"), Multiply("*"), Add("+"), Subtract("-");

        Operator(String representation) {
            this.representation = representation;
        }

        protected String representation;

        public String getRepresentation() {
            return representation;
        }
    }

    public static double evaluate(double val1, Operator op, double val2) {
        switch(op) {
            case Multiply:
                return val1 * val2;
            case Divide:
                return val1 / val2;
            case Add:
                return val1 + val2;
            case Subtract:
                return val1 - val2;
        }
        return 0.0;
    }

    protected static HashMap<String, Operator> opMap;

    protected static Pattern pattern;

    protected Expression() {
        if (opMap == null) {
            opMap = new HashMap<String, Operator>();
            for (Operator op : Operator.values()) opMap.put(op.getRepresentation(), op);
            pattern = Pattern.compile("\\([^\\(\\)]+\\)");
        }
    }

    public Pattern getPattern() {
        return pattern;
    }

    protected Operator getOp(String op) {
        return opMap.get(op);
    }

    protected double value;

    protected Expression val1, val2;

    protected Operator op;

    protected HashMap<String, Double> variables;

    protected Expression(double value) {
        this();
        this.value = value;
        this.val1 = null;
        this.val2 = null;
    }

    protected Expression(Expression val1, Operator op, Expression val2, HashMap<String, Double> variables) {
        this();
        this.val1 = val1;
        this.val2 = val2;
        this.op = op;
        this.variables = variables;
    }

    protected double evaluate() {
        if (val1 == null) return value;
        if (val2 == null) return val1.evaluate();
        double v1 = val1.evaluate();
        double v2 = val2.evaluate();
        return evaluate(v1, op, v2);
    }

    public String toString() {
        if (val1 == null) return Double.toString(value);
        if (val2 == null) return val1.toString();
        return String.format("(%s %s %s)", val1.toString(), op.getRepresentation(), val2.toString());
    }

    public static double evaluate(Expression val1, Operator op, Expression val2, HashMap<String, Double> variables) {
        return (new Expression(val1, op, val2, variables)).evaluate();
    }

    public static double evaluate(String expression, HashMap<String, Double> variables) {
        Expression tmp = new Expression();
        expression = expression.replaceAll("\\)\\(", ") * (");
        return evaluate(tmp.getPattern(), expression.trim(), variables);
    }

    protected static double evaluate(Pattern pattern, String expression, HashMap<String, Double> variables) {
        Matcher matcher = pattern.matcher(expression);
        String match = null;
        boolean found = false;
        while (matcher.find()) {
            match = matcher.group();
            match = match.substring(1, match.length() - 1);
            double val = evaluate(match.split("\\s"), variables);
            expression = expression.replace(matcher.quoteReplacement(matcher.group()), Double.toString(val));
            found = true;
        }
        if (found) return evaluate(pattern, expression, variables);
        return evaluate(expression.split("\\s"), variables);
    }

    protected static double evaluate(String[] expression, HashMap<String, Double> variables) {
        if (expression.length > 1) {
            for (Operator op : Operator.values()) {
                int index = locate(op, expression);
                if (index != -1) {
                    Expression preOp = new Expression(decodeVariable(expression[index - 1], variables));
                    Expression postOp = new Expression(decodeVariable(expression[index + 1], variables));
                    Expression exp = new Expression(preOp, op, postOp, variables);
                    double value = exp.evaluate();
                    return evaluate(expression, index, value, variables);
                }
            }
        }
        return decodeVariable(expression[0], variables);
    }

    protected static double evaluate(String[] expression, int opIndex, double previousResult, HashMap<String, Double> variables) {
        int preNdx = opIndex - 1;
        int postNdx = opIndex + 1;
        StringBuilder builder = new StringBuilder();
        if (preNdx > 0) builder.append(StringUtil.join(StringUtil.subset(expression, 0, preNdx - 1), " "));
        builder.append(" " + previousResult + " ");
        if ((postNdx + 1) <= expression.length) builder.append(StringUtil.join(StringUtil.subset(expression, postNdx + 1, expression.length - 1), " "));
        return evaluate(builder.toString(), variables);
    }

    protected static boolean isNumeric(char c) {
        if (c == '.') return true;
        if (c == '-') return true;
        return Character.isDigit(c);
    }

    protected static double decodeVariable(String variable, HashMap<String, Double> variables) {
        if (!isNumeric(variable.charAt(0))) return decodeNonNumericVariable(variable, variables);
        for (int pos = 0; pos < variable.length(); pos++) {
            if (!isNumeric(variable.charAt(pos))) {
                String part1 = variable.substring(0, pos);
                String part2 = variable.substring(pos);
                String expression = String.format("(%s * %s)", part1, part2);
                return evaluate(expression, variables);
            }
        }
        return Double.parseDouble(variable);
    }

    protected static double decodeNonNumericVariable(String variable, HashMap<String, Double> variables) {
        if (variables.containsKey(variable)) return variables.get(variable);
        for (int i = 0; i < variable.length(); i++) {
            String part1 = variable.substring(0, i);
            if (variables.containsKey(part1)) {
                String part2 = variable.substring(i);
                String expression = String.format("(%s * %s)", part1, part2);
                return evaluate(expression, variables);
            }
        }
        return 0.0;
    }

    protected static int locate(Operator op, String[] parts) {
        String rep = op.getRepresentation();
        for (int i = 0; i < parts.length; i++) if (rep.equals(parts[i])) return i;
        return -1;
    }

    public static class Test {

        public static void main(String[] args) {
            if (args.length == 0) {
                System.out.println("USAGE: java org.eoti.math.Expression \"expression\" [[variables]]");
                System.out.println("Example: java org.eoti.math.Expression \"(x + (3 * y))\" x=2 y=4");
                System.exit(0);
            }
            String expression = args[0];
            System.out.println("EXPRESSION: " + expression);
            HashMap<String, Double> variables = new HashMap<String, Double>();
            for (int i = 1; i < args.length; i++) {
                String s = args[i];
                int index = s.indexOf("=");
                String name = s.substring(0, index);
                String val = s.substring(index + 1);
                variables.put(name, Double.parseDouble(val));
            }
            System.out.println("VARIABLES:");
            for (String s : variables.keySet()) System.out.println("\t" + s + " = " + variables.get(s));
            double val = Expression.evaluate(expression, variables);
            System.out.println("\nRESULT: " + val);
        }
    }
}
