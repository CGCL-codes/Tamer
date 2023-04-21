package org.openXpertya.util;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.logging.Level;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class Evaluator {

    /** Descripción de Campos */
    private static CLogger s_log = CLogger.getCLogger(Evaluator.class);

    /**
     * Descripción de Método
     *
     *
     * @param source
     * @param logic
     *
     * @return
     */
    public static boolean isAllVariablesDefined(Evaluatee source, String logic) {
        if ((logic == null) || (logic.length() == 0)) {
            return true;
        }
        int pos = 0;
        while (pos < logic.length()) {
            int first = logic.indexOf('@', pos);
            if (first == -1) {
                return true;
            }
            int second = logic.indexOf('@', first + 1);
            if (second == -1) {
                s_log.severe("No second @ in Logic: " + logic);
                return false;
            }
            String variable = logic.substring(first + 1, second - 1);
            String eval = source.get_ValueAsString(variable);
            s_log.finest(variable + "=" + eval);
            if ((eval == null) || (eval.length() == 0)) {
                return false;
            }
            pos = second + 1;
        }
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param source
     * @param logic
     *
     * @return
     */
    public static boolean evaluateLogic(Evaluatee source, String logic) {
        StringTokenizer st = new StringTokenizer(logic.trim(), "&|", true);
        int it = st.countTokens();
        if (((it / 2) - ((it + 1) / 2)) == 0) {
            s_log.severe("Logic does not comply with format " + "'<expression> [<logic> <expression>]' => " + logic);
            return false;
        }
        boolean retValue = evaluateLogicTuple(source, st.nextToken());
        while (st.hasMoreTokens()) {
            String logOp = st.nextToken().trim();
            boolean temp = evaluateLogicTuple(source, st.nextToken());
            if (logOp.equals("&")) {
                retValue = retValue & temp;
            } else if (logOp.equals("|")) {
                retValue = retValue | temp;
            } else {
                s_log.log(Level.SEVERE, "Logic operant '|' or '&' expected => " + logic);
                return false;
            }
        }
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param source
     * @param logic
     *
     * @return
     */
    private static boolean evaluateLogicTuple(Evaluatee source, String logic) {
        StringTokenizer st = new StringTokenizer(logic.trim(), "!=^><", true);
        if (st.countTokens() != 3) {
            s_log.log(Level.SEVERE, "Logic touple does not comply with format " + "'@context@=value' or '@context@!value' => " + logic);
            return false;
        }
        String first = st.nextToken().trim();
        String firstEval = first.trim();
        if (first.indexOf('@') != -1) {
            first = first.replace('@', ' ').trim();
            firstEval = source.get_ValueAsString(first);
        }
        firstEval = firstEval.replace('\'', ' ').replace('"', ' ').trim();
        String operand = st.nextToken();
        String second = st.nextToken();
        String secondEval = second.trim();
        if (second.indexOf('@') != -1) {
            second = second.replace('@', ' ').trim();
            secondEval = source.get_ValueAsString(second);
        }
        secondEval = secondEval.replace('\'', ' ').replace('"', ' ').trim();
        if ((first.indexOf("_ID") != -1) && (firstEval.length() == 0)) {
            firstEval = "0";
        }
        if ((second.indexOf("_ID") != -1) && (secondEval.length() == 0)) {
            secondEval = "0";
        }
        boolean result = evaluateLogicTouple(firstEval, operand, secondEval);
        if (CLogMgt.isLevelFinest()) {
            s_log.finest(logic + " => \"" + firstEval + "\" " + operand + " \"" + secondEval + "\" => " + result);
        }
        return result;
    }

    /**
     * Descripción de Método
     *
     *
     * @param value1
     * @param operand
     * @param value2
     *
     * @return
     */
    private static boolean evaluateLogicTouple(String value1, String operand, String value2) {
        if ((value1 == null) || (operand == null) || (value2 == null)) {
            return false;
        }
        BigDecimal value1bd = null;
        BigDecimal value2bd = null;
        try {
            if (!value1.startsWith("'")) {
                value1bd = new BigDecimal(value1);
            }
            if (!value2.startsWith("'")) {
                value2bd = new BigDecimal(value2);
            }
        } catch (Exception e) {
            value1bd = null;
            value2bd = null;
        }
        if (operand.equals("=")) {
            if ((value1bd != null) && (value2bd != null)) {
                return value1bd.compareTo(value2bd) == 0;
            }
            return value1.compareTo(value2) == 0;
        } else if (operand.equals("<")) {
            if ((value1bd != null) && (value2bd != null)) {
                return value1bd.compareTo(value2bd) < 0;
            }
            return value1.compareTo(value2) < 0;
        } else if (operand.equals(">")) {
            if ((value1bd != null) && (value2bd != null)) {
                return value1bd.compareTo(value2bd) > 0;
            }
            return value1.compareTo(value2) > 0;
        } else {
            if ((value1bd != null) && (value2bd != null)) {
                return value1bd.compareTo(value2bd) != 0;
            }
            return value1.compareTo(value2) != 0;
        }
    }
}
