package org.isisproject.ice.conditionparser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import org.isisproject.ice.db.Property;

public class ConditionParser implements ConditionParserTreeConstants, ConditionParserConstants {

    protected JJTConditionParserState jjtree = new JJTConditionParserState();

    private Hashtable<String, String> propDatatypes;

    private Hashtable<String, String> triggerPropertyValues;

    public boolean testCondition(Node condition, Set<Property> triggerProps) throws ParseException {
        propDatatypes = new Hashtable<String, String>(triggerProps.size());
        for (Property p : triggerProps) {
            propDatatypes.put(p.getName(), p.getDatatype());
        }
        return evaluateCondition(condition, true);
    }

    public boolean evaluateCondition(Node condition, Hashtable<String, String> triggerPropertyValues, Set<Property> triggerProperties) throws ParseException {
        this.triggerPropertyValues = triggerPropertyValues;
        propDatatypes = new Hashtable<String, String>(triggerProperties.size());
        for (Property p : triggerProperties) {
            propDatatypes.put(p.getName(), p.getDatatype());
        }
        return evaluateCondition(condition, false);
    }

    private boolean evaluateCondition(Node condition, boolean isTest) throws ParseException {
        boolean result = false;
        if (condition instanceof Condition) {
            result = evaluateCondition(condition.jjtGetChild(0), isTest);
        } else if (condition instanceof And) {
            result = true;
            for (int i = 0; i < condition.jjtGetNumChildren(); i++) {
                if (evaluateCondition(condition.jjtGetChild(i), isTest) == false) {
                    result = false;
                    break;
                }
            }
        } else if (condition instanceof Or) {
            result = false;
            for (int i = 0; i < condition.jjtGetNumChildren(); i++) {
                if (evaluateCondition(condition.jjtGetChild(i), isTest) == true) {
                    result = true;
                    break;
                }
            }
        } else if (condition instanceof Not) {
            if (isTest) {
                result = evaluateCondition(condition.jjtGetChild(0), isTest);
            } else {
                result = !evaluateCondition(condition.jjtGetChild(0), isTest);
            }
        } else if (condition instanceof TruePredicate) {
            result = true;
        } else if (condition instanceof FalsePredicate) {
            if (isTest) {
                result = true;
            } else {
                result = false;
            }
        } else if (condition instanceof Comparison) {
            if (isTest) {
                result = testComparison((Comparison) condition);
            } else {
                result = evaluateComparison((Comparison) condition);
            }
        } else if (condition instanceof WeekdayExpression) {
            if (isTest) result = true; else result = evaluateWeekdayExpression((WeekdayExpression) condition);
        } else if (condition instanceof DateExpression) {
            if (isTest) result = true; else result = evaluateDateExpression((DateExpression) condition);
        } else if (condition instanceof TimeExpression) {
            if (isTest) result = true; else result = evaluateTimeExpression((TimeExpression) condition);
        }
        return result;
    }

    private boolean evaluateTimeExpression(TimeExpression timeExp) {
        double providedTimeSec = timeExp.getHour() + (double) timeExp.getMinutes() / (double) 60;
        GregorianCalendar currentDate = new GregorianCalendar();
        double currentTimeSec = currentDate.get(Calendar.HOUR_OF_DAY) + (double) currentDate.get(Calendar.MINUTE) / (double) 60;
        switch(timeExp.getOperator()) {
            case ConditionParserConstants.EQUAL:
                return providedTimeSec == currentTimeSec;
            case ConditionParserConstants.NOTEQUAL:
                return providedTimeSec != currentTimeSec;
            case ConditionParserConstants.GEQT:
                return currentTimeSec >= providedTimeSec;
            case ConditionParserConstants.GT:
                return currentTimeSec > providedTimeSec;
            case ConditionParserConstants.LT:
                return currentTimeSec < providedTimeSec;
            case ConditionParserConstants.LEQT:
                return currentTimeSec <= providedTimeSec;
            default:
                return false;
        }
    }

    private boolean evaluateDateExpression(DateExpression dateExp) {
        GregorianCalendar providedDate = new GregorianCalendar(dateExp.getYear(), dateExp.getMonth() - 1, dateExp.getDay());
        long providedDateSec = providedDate.getTimeInMillis();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        long currentDateSec = currentDate.getTimeInMillis();
        switch(dateExp.getOperator()) {
            case ConditionParserConstants.EQUAL:
                return providedDateSec == currentDateSec;
            case ConditionParserConstants.NOTEQUAL:
                return providedDateSec != currentDateSec;
            case ConditionParserConstants.GEQT:
                return currentDateSec >= providedDateSec;
            case ConditionParserConstants.GT:
                return currentDateSec > providedDateSec;
            case ConditionParserConstants.LT:
                return currentDateSec < providedDateSec;
            case ConditionParserConstants.LEQT:
                return currentDateSec <= providedDateSec;
            default:
                return false;
        }
    }

    private boolean evaluateWeekdayExpression(WeekdayExpression weekdayExp) {
        int op = weekdayExp.getOperator();
        int providedDayOfWeek = weekdayExp.getValue();
        GregorianCalendar gcal = new GregorianCalendar();
        int currentDayOfWeek = gcal.get(Calendar.DAY_OF_WEEK);
        switch(op) {
            case ConditionParserConstants.EQUAL:
                return providedDayOfWeek == currentDayOfWeek;
            case ConditionParserConstants.NOTEQUAL:
                return providedDayOfWeek != currentDayOfWeek;
            case ConditionParserConstants.GEQT:
                return currentDayOfWeek >= providedDayOfWeek;
            case ConditionParserConstants.GT:
                return currentDayOfWeek > providedDayOfWeek;
            case ConditionParserConstants.LT:
                return currentDayOfWeek < providedDayOfWeek;
            case ConditionParserConstants.LEQT:
                return currentDayOfWeek <= providedDayOfWeek;
            default:
                return false;
        }
    }

    private boolean evaluateComparison(Comparison comparison) {
        int op = comparison.getOperator();
        String aux = ((Variable) comparison.jjtGetChild(0)).getName();
        String var1Name = aux.substring(1, aux.length() - 1);
        String datatype1 = propDatatypes.get(var1Name);
        String var1Value = triggerPropertyValues.get(var1Name);
        if (comparison.jjtGetChild(1) instanceof Variable) {
            aux = ((Variable) comparison.jjtGetChild(1)).getName();
            String var2Name = aux.substring(1, aux.length() - 1);
            String datatype2 = propDatatypes.get(var2Name);
            String var2Value = triggerPropertyValues.get(var2Name);
            if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING)) {
                return compareStringVariables(var1Value, var2Value, datatype2, op);
            } else if (datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING)) {
                return compareStringVariables(var2Value, var1Value, datatype1, op);
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT) || datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT)) {
                return compareIntAndFloatVariables(var1Value, var2Value, op);
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_BOOLEAN)) {
                if (op == ConditionParserConstants.EQUAL) return var1Value.equalsIgnoreCase(var2Value); else return !var1Value.equalsIgnoreCase(var2Value);
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_CHAR)) {
                if (op == ConditionParserConstants.EQUAL) return var1Value.equals(var2Value); else return !var1Value.equals(var2Value);
            }
        } else {
            String value = ((Value) comparison.jjtGetChild(1)).getValue();
            if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING) || datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_CHAR)) {
                if (op == ConditionParserConstants.EQUAL) return var1Value.equals(value); else return !var1Value.equals(value);
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_BOOLEAN)) {
                if (op == ConditionParserConstants.EQUAL) return var1Value.equalsIgnoreCase(value); else return !var1Value.equalsIgnoreCase(value);
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT) || datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT)) {
                return compareIntAndFloatVariables(var1Value, value, op);
            }
        }
        return false;
    }

    private boolean compareStringVariables(String var1, String var2, String datatype2, int op) {
        if (datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING) || datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT) || datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_CHAR)) {
            if (op == ConditionParserConstants.EQUAL) return var1.equals(var2); else return !var1.equals(var2);
        } else if (datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_BOOLEAN)) {
            if (op == ConditionParserConstants.EQUAL) return var1.equalsIgnoreCase(var2); else return !var1.equalsIgnoreCase(var2);
        } else if (datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT)) {
            try {
                if (op == ConditionParserConstants.EQUAL) return Float.parseFloat(var1) == Float.parseFloat(var2); else return Float.parseFloat(var1) != Float.parseFloat(var2);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean compareIntAndFloatVariables(String variable1, String variable2, int op) {
        try {
            switch(op) {
                case ConditionParserConstants.EQUAL:
                    return Float.parseFloat(variable1) == Float.parseFloat(variable2);
                case ConditionParserConstants.NOTEQUAL:
                    return Float.parseFloat(variable1) != Float.parseFloat(variable2);
                case ConditionParserConstants.GEQT:
                    return Float.parseFloat(variable1) >= Float.parseFloat(variable2);
                case ConditionParserConstants.GT:
                    return Float.parseFloat(variable1) > Float.parseFloat(variable2);
                case ConditionParserConstants.LT:
                    return Float.parseFloat(variable1) < Float.parseFloat(variable2);
                case ConditionParserConstants.LEQT:
                    return Float.parseFloat(variable1) <= Float.parseFloat(variable2);
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean testComparison(Comparison comparison) throws ParseException {
        int operator = comparison.getOperator();
        String aux = ((Variable) comparison.jjtGetChild(0)).getName();
        String variable1 = aux.substring(1, aux.length() - 1);
        String datatype1 = propDatatypes.get(variable1);
        if (datatype1 == null) throw new ParseException(variable1 + " is not a valid property name.");
        if (comparison.jjtGetChild(1) instanceof Variable) {
            aux = ((Variable) comparison.jjtGetChild(1)).getName();
            String variable2 = aux.substring(1, aux.length() - 1);
            String datatype2 = propDatatypes.get(variable2);
            if (datatype2 == null) throw new ParseException(variable1 + " is not a valid property name.");
            if (datatype1.equals(datatype2) || datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING) || datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING) || (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT) && datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT)) || (datatype2.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT) && datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT))) {
                if (!(datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT) || datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT) || operator == ConditionParserConstants.EQUAL || operator == ConditionParserConstants.NOTEQUAL)) {
                    throw new ParseException("Property " + variable1 + " is of type " + getDatatypeStr(datatype1) + ", and property " + variable2 + " is of type " + getDatatypeStr(datatype2) + ".\n Those two types cannot be compared with the given operator.");
                } else {
                    return true;
                }
            } else {
                throw new ParseException("Property " + variable1 + " is of type " + getDatatypeStr(datatype1) + ", and property " + variable2 + " is of type " + getDatatypeStr(datatype2) + ".\n Those two types cannot be compared.");
            }
        } else {
            String value = ((Value) comparison.jjtGetChild(1)).getValue();
            if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_STRING)) {
                if (operator == ConditionParserConstants.EQUAL || operator == ConditionParserConstants.NOTEQUAL) {
                    return true;
                }
                throw new ParseException("Property " + variable1 + " is of type String and cannot be compared with " + value + " using the given operator.");
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_BOOLEAN)) {
                if (operator == ConditionParserConstants.EQUAL || operator == ConditionParserConstants.NOTEQUAL) {
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        return true;
                    }
                    throw new ParseException("Property " + variable1 + " is of type Boolean and cannot be compared with " + value + ".");
                }
                throw new ParseException("Property " + variable1 + " is of type Boolean and cannot be compared with " + value + " using the given operator.");
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_CHAR)) {
                if (operator == ConditionParserConstants.EQUAL || operator == ConditionParserConstants.NOTEQUAL) {
                    if (value.length() == 1) {
                        return true;
                    }
                    throw new ParseException("Property " + variable1 + " is of type Char and cannot be compared with " + value + ".");
                }
                throw new ParseException("Property " + variable1 + " is of type Char and cannot be compared with " + value + " using the given operator.");
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_FLOAT)) {
                try {
                    Float.parseFloat(value);
                    return true;
                } catch (NumberFormatException e) {
                    throw new ParseException("Property " + variable1 + " is of type Float and cannot be compared with " + value + ".");
                }
            } else if (datatype1.equals(PropertyDatatypes.ICE_PROP_DATATYPE_INT)) {
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    throw new ParseException("Property " + variable1 + " is of type Integer and cannot be compared with " + value + ".");
                }
            }
        }
        return false;
    }

    private String getDatatypeStr(String datatype) {
        int i = Integer.parseInt(datatype);
        switch(i) {
            case 0:
                return "Unknown";
            case 1:
                return "String";
            case 2:
                return "Integer";
            case 3:
                return "Float";
            case 4:
                return "Boolean";
            case 5:
                return "Char";
        }
        return "";
    }

    public static void main(String args[]) throws ParseException {
        ConditionParser parser;
        while (true) {
            parser = new ConditionParser(System.in);
            try {
                Condition root = parser.Condition();
                root.dump("");
                boolean result = parser.evaluateCondition(root, new Hashtable<String, String>(0), new HashSet<Property>(0));
                System.out.println(result);
                System.out.println("Thank you.");
            } catch (Exception e) {
                System.out.println("Oops.");
                System.out.println(e.getMessage());
            }
        }
    }

    public final Condition Condition() throws ParseException {
        Condition jjtn000 = new Condition(JJTCONDITION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        try {
            Expression();
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            {
                if (true) return jjtn000;
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte000;
                }
            }
            if (jjte000 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte000;
                }
            }
            {
                if (true) throw (Error) jjte000;
            }
        } finally {
            if (jjtc000) {
                jjtree.closeNodeScope(jjtn000, true);
            }
        }
        throw new Error("Missing return statement in function");
    }

    public final void Expression() throws ParseException {
        OrExpression();
    }

    public final void OrExpression() throws ParseException {
        Or jjtn001 = new Or(JJTOR);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            AndExpression();
            label_1: while (true) {
                if (jj_2_1(2)) {
                    ;
                } else {
                    break label_1;
                }
                jj_consume_token(OR);
                AndExpression();
            }
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, jjtree.nodeArity() > 1);
            }
        }
    }

    public final void AndExpression() throws ParseException {
        And jjtn001 = new And(JJTAND);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case TRUE:
                case FALSE:
                case WEEKDAY:
                case DATE:
                case TIME:
                    ReservedExpression();
                    break;
                case VARIABLE:
                    ComparativeExpression();
                    break;
                case NOT:
                    NotExpression();
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            label_2: while (true) {
                if (jj_2_2(2)) {
                    ;
                } else {
                    break label_2;
                }
                jj_consume_token(AND);
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case TRUE:
                    case FALSE:
                    case WEEKDAY:
                    case DATE:
                    case TIME:
                        ReservedExpression();
                        break;
                    case VARIABLE:
                        ComparativeExpression();
                        break;
                    case NOT:
                        NotExpression();
                        break;
                    default:
                        jj_la1[1] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, jjtree.nodeArity() > 1);
            }
        }
    }

    public final void NotExpression() throws ParseException {
        Not jjtn001 = new Not(JJTNOT);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(NOT);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 29:
                    jj_consume_token(29);
                    Expression();
                    jj_consume_token(30);
                    break;
                case TRUE:
                case FALSE:
                case WEEKDAY:
                case DATE:
                case TIME:
                    ReservedExpression();
                    break;
                case VARIABLE:
                    ComparativeExpression();
                    break;
                case NOT:
                    NotExpression();
                    break;
                default:
                    jj_la1[2] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
    }

    public final void ComparativeExpression() throws ParseException {
        Comparison jjtn001 = new Comparison(JJTCOMPARISON);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            VariableExpression();
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case NOTEQUAL:
                    jj_consume_token(NOTEQUAL);
                    jjtn001.setOperator(ConditionParserConstants.NOTEQUAL);
                    break;
                case EQUAL:
                    jj_consume_token(EQUAL);
                    jjtn001.setOperator(ConditionParserConstants.EQUAL);
                    break;
                case GEQT:
                    jj_consume_token(GEQT);
                    jjtn001.setOperator(ConditionParserConstants.GEQT);
                    break;
                case GT:
                    jj_consume_token(GT);
                    jjtn001.setOperator(ConditionParserConstants.GT);
                    break;
                case LEQT:
                    jj_consume_token(LEQT);
                    jjtn001.setOperator(ConditionParserConstants.LEQT);
                    break;
                case LT:
                    jj_consume_token(LT);
                    jjtn001.setOperator(ConditionParserConstants.LT);
                    break;
                default:
                    jj_la1[3] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case VARIABLE:
                    VariableExpression();
                    break;
                case TRUE:
                case FALSE:
                case TWODIGITS:
                case YEAR:
                case STRING:
                    ValueExpression();
                    break;
                default:
                    jj_la1[4] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
    }

    public final void VariableExpression() throws ParseException {
        Token t;
        Variable jjtn001 = new Variable(JJTVARIABLE);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            t = jj_consume_token(VARIABLE);
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
        jjtn001.setName(t.toString());
    }

    public final void ValueExpression() throws ParseException {
        Token t;
        Value jjtn001 = new Value(JJTVALUE);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case TRUE:
                    t = jj_consume_token(TRUE);
                    break;
                case FALSE:
                    t = jj_consume_token(FALSE);
                    break;
                case TWODIGITS:
                    t = jj_consume_token(TWODIGITS);
                    break;
                case YEAR:
                    t = jj_consume_token(YEAR);
                    break;
                case STRING:
                    t = jj_consume_token(STRING);
                    break;
                default:
                    jj_la1[5] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
        jjtn001.setValue(t.toString());
    }

    public final void ReservedExpression() throws ParseException {
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case TRUE:
                TruePredicate();
                break;
            case FALSE:
                FalsePredicate();
                break;
            case WEEKDAY:
                WeekdayExpression();
                break;
            case DATE:
                DateExpression();
                break;
            case TIME:
                TimeExpression();
                break;
            default:
                jj_la1[6] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final void TruePredicate() throws ParseException {
        TruePredicate jjtn001 = new TruePredicate(JJTTRUEPREDICATE);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(TRUE);
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
    }

    public final void FalsePredicate() throws ParseException {
        FalsePredicate jjtn001 = new FalsePredicate(JJTFALSEPREDICATE);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(FALSE);
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
    }

    public final void WeekdayExpression() throws ParseException {
        WeekdayExpression jjtn001 = new WeekdayExpression(JJTWEEKDAYEXPRESSION);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(WEEKDAY);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case NOTEQUAL:
                    jj_consume_token(NOTEQUAL);
                    jjtn001.setOperator(ConditionParserConstants.NOTEQUAL);
                    break;
                case EQUAL:
                    jj_consume_token(EQUAL);
                    jjtn001.setOperator(ConditionParserConstants.EQUAL);
                    break;
                case GEQT:
                    jj_consume_token(GEQT);
                    jjtn001.setOperator(ConditionParserConstants.GEQT);
                    break;
                case GT:
                    jj_consume_token(GT);
                    jjtn001.setOperator(ConditionParserConstants.GT);
                    break;
                case LEQT:
                    jj_consume_token(LEQT);
                    jjtn001.setOperator(ConditionParserConstants.LEQT);
                    break;
                case LT:
                    jj_consume_token(LT);
                    jjtn001.setOperator(ConditionParserConstants.LT);
                    break;
                default:
                    jj_la1[7] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case MONDAY:
                    jj_consume_token(MONDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.MONDAY);
                    break;
                case TUESDAY:
                    jj_consume_token(TUESDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.TUESDAY);
                    break;
                case WEDNESDAY:
                    jj_consume_token(WEDNESDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.WEDNESDAY);
                    break;
                case THURSDAY:
                    jj_consume_token(THURSDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.THURSDAY);
                    break;
                case FRIDAY:
                    jj_consume_token(FRIDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.FRIDAY);
                    break;
                case SATURDAY:
                    jj_consume_token(SATURDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.SATURDAY);
                    break;
                case SUNDAY:
                    jj_consume_token(SUNDAY);
                    jjtree.closeNodeScope(jjtn001, true);
                    jjtc001 = false;
                    jjtn001.setValue(Calendar.SUNDAY);
                    break;
                default:
                    jj_la1[8] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
    }

    public final void DateExpression() throws ParseException {
        int year, month, day;
        DateExpression jjtn001 = new DateExpression(JJTDATEEXPRESSION);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(DATE);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case NOTEQUAL:
                    jj_consume_token(NOTEQUAL);
                    jjtn001.setOperator(ConditionParserConstants.NOTEQUAL);
                    break;
                case EQUAL:
                    jj_consume_token(EQUAL);
                    jjtn001.setOperator(ConditionParserConstants.EQUAL);
                    break;
                case GEQT:
                    jj_consume_token(GEQT);
                    jjtn001.setOperator(ConditionParserConstants.GEQT);
                    break;
                case GT:
                    jj_consume_token(GT);
                    jjtn001.setOperator(ConditionParserConstants.GT);
                    break;
                case LEQT:
                    jj_consume_token(LEQT);
                    jjtn001.setOperator(ConditionParserConstants.LEQT);
                    break;
                case LT:
                    jj_consume_token(LT);
                    jjtn001.setOperator(ConditionParserConstants.LT);
                    break;
                default:
                    jj_la1[9] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            year = TimeValueYear();
            jj_consume_token(31);
            month = TimeValueMonth();
            jj_consume_token(31);
            day = TimeValueDay();
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
        GregorianCalendar gcal = new GregorianCalendar();
        if (!((gcal.isLeapYear(year) && month == 2 && day <= 29) || (!gcal.isLeapYear(year) && month == 2 && day <= 28) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day <= 31) || ((month == 4 || month == 6 || month == 9 || month == 11) && day <= 30))) {
            if (true) throw new ParseException("Invalid Date");
        }
        jjtn001.setYear(year);
        jjtn001.setMonth(month);
        jjtn001.setDay(day);
    }

    public final int TimeValueDay() throws ParseException {
        Token t;
        t = jj_consume_token(TWODIGITS);
        {
            if (true) return (Integer.parseInt((String) t.image));
        }
        throw new Error("Missing return statement in function");
    }

    public final int TimeValueMonth() throws ParseException {
        Token t;
        t = jj_consume_token(TWODIGITS);
        {
            if (true) return (Integer.parseInt((String) t.image));
        }
        throw new Error("Missing return statement in function");
    }

    public final int TimeValueYear() throws ParseException {
        Token t;
        t = jj_consume_token(YEAR);
        {
            if (true) return (Integer.parseInt((String) t.image));
        }
        throw new Error("Missing return statement in function");
    }

    public final void TimeExpression() throws ParseException {
        Token th, tm;
        TimeExpression jjtn001 = new TimeExpression(JJTTIMEEXPRESSION);
        boolean jjtc001 = true;
        jjtree.openNodeScope(jjtn001);
        try {
            jj_consume_token(TIME);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case NOTEQUAL:
                    jj_consume_token(NOTEQUAL);
                    jjtn001.setOperator(ConditionParserConstants.NOTEQUAL);
                    break;
                case EQUAL:
                    jj_consume_token(EQUAL);
                    jjtn001.setOperator(ConditionParserConstants.EQUAL);
                    break;
                case GEQT:
                    jj_consume_token(GEQT);
                    jjtn001.setOperator(ConditionParserConstants.GEQT);
                    break;
                case GT:
                    jj_consume_token(GT);
                    jjtn001.setOperator(ConditionParserConstants.GT);
                    break;
                case LEQT:
                    jj_consume_token(LEQT);
                    jjtn001.setOperator(ConditionParserConstants.LEQT);
                    break;
                case LT:
                    jj_consume_token(LT);
                    jjtn001.setOperator(ConditionParserConstants.LT);
                    break;
                default:
                    jj_la1[10] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            th = TimeValueHour();
            jj_consume_token(32);
            tm = TimeValueMinutes();
        } catch (Throwable jjte001) {
            if (jjtc001) {
                jjtree.clearNodeScope(jjtn001);
                jjtc001 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte001 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte001;
                }
            }
            if (jjte001 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte001;
                }
            }
            {
                if (true) throw (Error) jjte001;
            }
        } finally {
            if (jjtc001) {
                jjtree.closeNodeScope(jjtn001, true);
            }
        }
        jjtn001.setHour(Integer.parseInt(th.toString()));
        jjtn001.setMinutes(Integer.parseInt(tm.toString()));
    }

    public final Token TimeValueHour() throws ParseException {
        Token t;
        t = jj_consume_token(TWODIGITS);
        if (Integer.parseInt(t.toString()) > 23) {
            if (true) throw new ParseException("Invalid value for the hour. It must be less than 24");
        }
        {
            if (true) return t;
        }
        throw new Error("Missing return statement in function");
    }

    public final Token TimeValueMinutes() throws ParseException {
        Token t;
        t = jj_consume_token(TWODIGITS);
        if (Integer.parseInt(t.toString()) > 59) {
            if (true) throw new ParseException("Invalid value for the minutes. It must be less than 60");
        }
        {
            if (true) return t;
        }
        throw new Error("Missing return statement in function");
    }

    private boolean jj_2_1(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_1();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(0, xla);
        }
    }

    private boolean jj_2_2(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_2();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(1, xla);
        }
    }

    private boolean jj_3R_23() {
        if (jj_scan_token(TIME)) return true;
        return false;
    }

    private boolean jj_3R_8() {
        if (jj_3R_11()) return true;
        return false;
    }

    private boolean jj_3R_13() {
        if (jj_3R_19()) return true;
        return false;
    }

    private boolean jj_3_2() {
        if (jj_scan_token(AND)) return true;
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3R_4()) {
            jj_scanpos = xsp;
            if (jj_3R_5()) {
                jj_scanpos = xsp;
                if (jj_3R_6()) return true;
            }
        }
        return false;
    }

    private boolean jj_3R_10() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3R_13()) {
            jj_scanpos = xsp;
            if (jj_3R_14()) {
                jj_scanpos = xsp;
                if (jj_3R_15()) {
                    jj_scanpos = xsp;
                    if (jj_3R_16()) {
                        jj_scanpos = xsp;
                        if (jj_3R_17()) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_5() {
        if (jj_3R_11()) return true;
        return false;
    }

    private boolean jj_3R_11() {
        if (jj_3R_18()) return true;
        return false;
    }

    private boolean jj_3R_15() {
        if (jj_3R_21()) return true;
        return false;
    }

    private boolean jj_3_1() {
        if (jj_scan_token(OR)) return true;
        if (jj_3R_3()) return true;
        return false;
    }

    private boolean jj_3R_12() {
        if (jj_scan_token(NOT)) return true;
        return false;
    }

    private boolean jj_3R_17() {
        if (jj_3R_23()) return true;
        return false;
    }

    private boolean jj_3R_9() {
        if (jj_3R_12()) return true;
        return false;
    }

    private boolean jj_3R_7() {
        if (jj_3R_10()) return true;
        return false;
    }

    private boolean jj_3R_21() {
        if (jj_scan_token(WEEKDAY)) return true;
        return false;
    }

    private boolean jj_3R_3() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3R_7()) {
            jj_scanpos = xsp;
            if (jj_3R_8()) {
                jj_scanpos = xsp;
                if (jj_3R_9()) return true;
            }
        }
        return false;
    }

    private boolean jj_3R_6() {
        if (jj_3R_12()) return true;
        return false;
    }

    private boolean jj_3R_22() {
        if (jj_scan_token(DATE)) return true;
        return false;
    }

    private boolean jj_3R_4() {
        if (jj_3R_10()) return true;
        return false;
    }

    private boolean jj_3R_18() {
        if (jj_scan_token(VARIABLE)) return true;
        return false;
    }

    private boolean jj_3R_14() {
        if (jj_3R_20()) return true;
        return false;
    }

    private boolean jj_3R_20() {
        if (jj_scan_token(FALSE)) return true;
        return false;
    }

    private boolean jj_3R_16() {
        if (jj_3R_22()) return true;
        return false;
    }

    private boolean jj_3R_19() {
        if (jj_scan_token(TRUE)) return true;
        return false;
    }

    /** Generated Token Manager. */
    public ConditionParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    /** Current token. */
    public Token token;

    /** Next token. */
    public Token jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    private int jj_gen;

    private final int[] jj_la1 = new int[11];

    private static int[] jj_la1_0;

    private static int[] jj_la1_1;

    static {
        jj_la1_init_0();
        jj_la1_init_1();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[] { 0x13f0, 0x13f0, 0x200013f0, 0x7e000, 0x1c000230, 0x1c000030, 0x1f0, 0x7e000, 0x3f80000, 0x7e000, 0x7e000 };
    }

    private static void jj_la1_init_1() {
        jj_la1_1 = new int[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[2];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    /** Constructor with InputStream. */
    public ConditionParser(java.io.InputStream stream) {
        this(stream, null);
    }

    /** Constructor with InputStream and supplied encoding */
    public ConditionParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new ConditionParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /** Reinitialise. */
    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor. */
    public ConditionParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new ConditionParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor with generated Token Manager. */
    public ConditionParser(ConditionParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(ConditionParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 11; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            if (++jj_gc > 100) {
                jj_gc = 0;
                for (int i = 0; i < jj_2_rtns.length; i++) {
                    JJCalls c = jj_2_rtns[i];
                    while (c != null) {
                        if (c.gen < jj_gen) c.first = null;
                        c = c.next;
                    }
                }
            }
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    private static final class LookaheadSuccess extends java.lang.Error {
    }

    private final LookaheadSuccess jj_ls = new LookaheadSuccess();

    private boolean jj_scan_token(int kind) {
        if (jj_scanpos == jj_lastpos) {
            jj_la--;
            if (jj_scanpos.next == null) {
                jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
            } else {
                jj_lastpos = jj_scanpos = jj_scanpos.next;
            }
        } else {
            jj_scanpos = jj_scanpos.next;
        }
        if (jj_rescan) {
            int i = 0;
            Token tok = token;
            while (tok != null && tok != jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) jj_add_error_token(kind, i);
        }
        if (jj_scanpos.kind != kind) return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
        return false;
    }

    /** Get the next Token. */
    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /** Get the specific Token. */
    public final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();

    private int[] jj_expentry;

    private int jj_kind = -1;

    private int[] jj_lasttokens = new int[100];

    private int jj_endpos;

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) return;
        if (pos == jj_endpos + 1) {
            jj_lasttokens[jj_endpos++] = kind;
        } else if (jj_endpos != 0) {
            jj_expentry = new int[jj_endpos];
            for (int i = 0; i < jj_endpos; i++) {
                jj_expentry[i] = jj_lasttokens[i];
            }
            jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext(); ) {
                int[] oldentry = (int[]) (it.next());
                if (oldentry.length == jj_expentry.length) {
                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            continue jj_entries_loop;
                        }
                    }
                    jj_expentries.add(jj_expentry);
                    break jj_entries_loop;
                }
            }
            if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
        }
    }

    /** Generate ParseException. */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[33];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 11; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                    if ((jj_la1_1[i] & (1 << j)) != 0) {
                        la1tokens[32 + j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 33; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /** Enable tracing. */
    public final void enable_tracing() {
    }

    /** Disable tracing. */
    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
        jj_rescan = true;
        for (int i = 0; i < 2; i++) {
            try {
                JJCalls p = jj_2_rtns[i];
                do {
                    if (p.gen > jj_gen) {
                        jj_la = p.arg;
                        jj_lastpos = jj_scanpos = p.first;
                        switch(i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess ls) {
            }
        }
        jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p = jj_2_rtns[index];
        while (p.gen > jj_gen) {
            if (p.next == null) {
                p = p.next = new JJCalls();
                break;
            }
            p = p.next;
        }
        p.gen = jj_gen + xla - jj_la;
        p.first = token;
        p.arg = xla;
    }

    static final class JJCalls {

        int gen;

        Token first;

        int arg;

        JJCalls next;
    }
}
