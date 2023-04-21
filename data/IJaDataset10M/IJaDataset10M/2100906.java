package jhomenet.responsive;

import java.util.*;
import org.apache.log4j.Logger;
import jhomenet.hw.*;
import jhomenet.hw.management.*;
import jhomenet.hw.sensor.*;
import jhomenet.hw.states.OnState;
import jhomenet.hw.states.OffState;

/**
 * Compiles an expression string into an expression object.
 * <br>
 * Id: $Id: ExpressionCompiler.java 992 2005-11-21 23:00:03Z dhirwinjr $
 * 
 * @author David Irwin
 */
public class ExpressionCompiler {

    /**
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(ExpressionCompiler.class.getName());

    /**
     * A reference to the expression compiler.
     */
    private static ExpressionCompiler eCompiler;

    /**
     * Define the maximum expression string size. 
     */
    private final int eSizeLimit = 1000;

    /**
     * Reference to the hardware registry
     */
    private JHomenetRegistry registry;

    /**
     * Define compiler operators.
     */
    public static enum CompilerOperator {

        CSTART("["), CSTOP("]");

        private String option1;

        /**
         * Constructor.
         *
         * @param option1
         */
        private CompilerOperator(String option1) {
            this.option1 = option1;
        }

        public String toString() {
            return option1;
        }

        public boolean equals(String s) {
            if (s.equalsIgnoreCase(option1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Known compiler descriptors.
     */
    private enum Descriptors {

        /**
         * List of known descriptors.
         */
        CONDITION("C"), ID("ID"), DESCRIPTION("D"), HARDWARE("HW"), DESIREDSTATE("DS"), TESTSTATE("TS"), TESTVALUE("TV"), TESTOPERATOR("TO");

        /**
         * Keep a list of the descriptor options
         */
        private String option;

        /**
         * Constructor.
         *
         * @param s
         */
        private Descriptors(String s) {
            this.option = s;
        }

        /**
         * Check whether an input string equals the descriptor options.
         *
         * @param input
         * @return Whether the two inputs are equal
         */
        public boolean equals(String input) {
            if (input.equalsIgnoreCase(option)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return option;
        }
    }

    /**
     * Known expression inputs.
     */
    private enum Inputs {

        STATE("s"), VALUE("v"), BOOLEAN("b"), TRUE("true"), FALSE("false"), ON("on"), OFF("off"), GREATERTHAN(">"), LESSTHAN("<"), EQUALS("=");

        String input;

        private Inputs(String input) {
            this.input = input;
        }

        public String toString() {
            return input;
        }

        public boolean equals(String s) {
            if (input.equalsIgnoreCase(s)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Constructor, however it's never instantiated.
     */
    private ExpressionCompiler() {
        registry = HardwareManager.instance().getRegistry();
    }

    /**
     * Get an instance of the expression compiler.
     * 
     * @return
     */
    public static ExpressionCompiler instance() {
        if (eCompiler == null) {
            eCompiler = new ExpressionCompiler();
        }
        return eCompiler;
    }

    /**
     * Compile an expression into an expression object.
     *
     * @param eString Expression string
     * @return An expression
     * @throws ResponsiveException
     */
    public Expression compileExpressionString(String eString) throws ResponsiveException {
        logger.debug("Compiling expression string: " + eString);
        Expression expression = new Expression();
        for (int position = 0; position < eString.length() && position != -1; ) {
            String s = Character.toString(eString.charAt(position));
            if (Expression.OPERATORS.OPEN_P.equals(s)) {
                expression._OPEN_PARENTHESIS();
                position++;
            } else if (Expression.OPERATORS.CLOSE_P.equals(s)) {
                expression._CLOSE_PARENTHESIS();
                position++;
            } else if (s.equals("&")) {
                position++;
                if (Character.toString(eString.charAt(position)).equals("&")) {
                    expression._AND();
                    position++;
                } else {
                    throw new ResponsiveException("Binary operator & not allowed -> use && instead");
                }
            } else if (s.equals("|")) {
                position++;
                if (Character.toString(eString.charAt(position)).equals("|")) {
                    expression._OR();
                    position++;
                } else {
                    throw new ResponsiveException("Binary operator | not allowed -> use || instead");
                }
            } else if (Expression.OPERATORS.NOT.equals(s)) {
                expression._NOT();
                position++;
            } else if (CompilerOperator.CSTART.equals(s)) {
                int nextPosition = eString.indexOf(CompilerOperator.CSTOP.toString(), position);
                String conditionString = eString.substring(++position, nextPosition);
                expression.addCondition(compileConditionString(conditionString));
                position = nextPosition;
            } else if (CompilerOperator.CSTOP.equals(s)) {
                position++;
            } else if (s.equalsIgnoreCase(" ")) {
                position++;
            } else {
                logger.error("Unknown expression string element: " + s);
                throw new ResponsiveException("Unknown expression string element: " + s);
            }
            if (position > eSizeLimit) {
                throw new ResponsiveException("Expression size exceeds limit!");
            }
        }
        return expression;
    }

    /**
     * Create a condition from a condition string.
     *
     * @param cString Condition string
     * @return A condition
     * @throws ResponsiveException
     */
    private Condition compileConditionString(String cString) throws ResponsiveException {
        logger.debug("Compiling condition string: " + cString);
        HashMap<String, String> conditionMap = buildConditionMap(cString);
        String cType = conditionMap.get(Descriptors.CONDITION.toString());
        if (cType == null) {
            throw new ResponsiveException("Condition type must be set!");
        } else if (Inputs.STATE.equals(cType)) {
            logger.debug("Parsing state condition...");
            StateCondition<StateSensor> condition = new StateCondition<StateSensor>();
            setHardwareID(condition, conditionMap.get(Descriptors.ID.toString()));
            setDescription(condition, conditionMap.get(Descriptors.DESCRIPTION.toString()));
            String hardwareID = getHardwareID(conditionMap.get(Descriptors.HARDWARE.toString()));
            HomenetHardware hw = registry.getRegisteredHardware(hardwareID);
            if (hw == null) {
                throw new ResponsiveException("Hardware doesn't existing in registry: " + conditionMap.get(Descriptors.HARDWARE.toString()));
            }
            if (!(hw instanceof StateSensor)) {
                throw new ResponsiveException("Condition and sensor types don't match!");
            }
            condition.setSensor((StateSensor) hw);
            String testState = conditionMap.get(Descriptors.TESTSTATE.toString());
            if (Inputs.ON.equals(testState)) {
                condition.setTestState(new OnState());
            } else if (Inputs.OFF.equals(testState)) {
                condition.setTestState(new OffState());
            } else {
                throw new ResponsiveException("Unknown test state: " + testState);
            }
            logger.debug("Returning state condition");
            return condition;
        } else if (Inputs.VALUE.equals(cType)) {
            logger.debug("Parsing value condition...");
            ValueCondition<ValueSensor> condition = new ValueCondition<ValueSensor>();
            setHardwareID(condition, conditionMap.get(Descriptors.ID.toString()));
            setDescription(condition, conditionMap.get(Descriptors.DESCRIPTION.toString()));
            String hardwareID = getHardwareID(conditionMap.get(Descriptors.HARDWARE.toString()));
            HomenetHardware hw = registry.getRegisteredHardware(hardwareID);
            if (hw == null) {
                throw new ResponsiveException("Hardware doesn't existing in registry: " + conditionMap.get(Descriptors.HARDWARE.toString()));
            }
            if (!(hw instanceof ValueSensor)) {
                throw new ResponsiveException("CONDITION and sensor types don't match!");
            }
            condition.setSensor((ValueSensor) hw);
            condition.setTestValue(Double.valueOf(conditionMap.get(Descriptors.TESTVALUE.toString())));
            String testOperator = conditionMap.get(Descriptors.TESTOPERATOR.toString());
            if (Inputs.GREATERTHAN.equals(testOperator)) {
                condition.setOperator(ValueCondition.ValueOperator._GREATER_THAN);
            } else if (Inputs.LESSTHAN.equals(testOperator)) {
                condition.setOperator(ValueCondition.ValueOperator._LESS_THAN);
            } else if (Inputs.EQUALS.equals(testOperator)) {
                condition.setOperator(ValueCondition.ValueOperator._EQUAL);
            } else {
                throw new ResponsiveException("Unknown test operator: " + testOperator);
            }
            logger.debug("Returning value condition");
            return condition;
        } else if (Inputs.BOOLEAN.equals(cType)) {
            logger.debug("Parsing boolean condition...");
            BooleanCondition bCondition = new BooleanCondition();
            bCondition.setDescription(conditionMap.get(Descriptors.DESCRIPTION.toString()));
            String desiredState = conditionMap.get(Descriptors.DESIREDSTATE.toString());
            if (Inputs.TRUE.equals(desiredState)) {
                bCondition.setDesiredState(true);
            } else if (Inputs.FALSE.equals(desiredState)) {
                bCondition.setDesiredState(false);
            } else {
                throw new ResponsiveException("Unknown boolean condition desired state");
            }
            return bCondition;
        } else {
            logger.error("Unknown condition type: " + cType);
            throw new ResponsiveException("Unknown condition type: " + cType);
        }
    }

    private static void setHardwareID(Condition condition, String cID) throws ResponsiveException {
        if (cID == null) {
            throw new ResponsiveException("Condition ID must be set!");
        } else {
            condition.setID(cID);
        }
    }

    /**
     * Set a condition's description.
     *
     * @param condition
     * @param desc
     */
    private static void setDescription(Condition condition, String desc) {
        if (desc != null) {
            condition.setDescription(desc);
        }
    }

    /**
     * Get the hardawre ID given a hardware input string as defined in a
     * a condition string.
     *
     * @param hwInputString
     * @return The hardware ID
     */
    private static String getHardwareID(String hwInputString) {
        if (hwInputString.startsWith("(") && hwInputString.endsWith(")")) {
            int nextPosition = hwInputString.indexOf(")", 0);
            String pair = hwInputString.substring(1, nextPosition);
            String pairs[] = pair.split(",");
            pairs[0].trim();
            pairs[1].trim();
            return pairs[0];
        } else {
            return hwInputString;
        }
    }

    /**
     * Build a condition map.
     *
     * @param cString Condition string
     * @return Map with descriptor/input pairs
     */
    private static HashMap<String, String> buildConditionMap(String cString) {
        HashMap<String, String> map = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(cString, ";");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            String[] descriptorInputPair = s.split(":");
            map.put(descriptorInputPair[0], descriptorInputPair[1]);
        }
        return map;
    }
}
