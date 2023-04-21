package uk.ac.city.soi.everest.core;

import java.io.Serializable;

/**
 * This class models time expressions. it is assumed here that only the following expression are permitted T1 + number
 * or T1 - number. Although in the schema expressions like T1 + T2 or T1 - T2 + number is permitted. But it is not
 * implemented This class has the following fields :
 * 
 * timeVariable - Time variable involved in the espression operator - operator of the expression (plus or minus) number -
 * a numerical value involved in the expression
 * 
 * @author Khaled Mahbub
 */
public class TimeExpression implements Serializable {

    private TimeVar timeVariable;

    private String operator;

    private long number;

    /**
     * Creates a new instance of TimeExpression.
     */
    public TimeExpression() {
        timeVariable = new TimeVar("");
        operator = Constants.PLUS;
        number = 0;
    }

    /**
     * Creates a new instance of TimeExpression.
     */
    public TimeExpression(String tName, String operator, long number) {
        timeVariable = new TimeVar(tName);
        this.operator = new String(operator.getBytes());
        this.number = number;
    }

    /**
     * Creates a new Time Expression with a given time variable name. Operator is plus and number is 0.
     * 
     * @param tName
     */
    public TimeExpression(String tName) {
        timeVariable = new TimeVar(tName);
        operator = Constants.PLUS;
        number = 0;
    }

    /**
     * Creates a new Time Expression with a given time variable. Operator is plus and number is 0.
     * 
     * @param var
     */
    public TimeExpression(TimeVar var) {
        timeVariable = new TimeVar(var);
        operator = Constants.PLUS;
        number = 0;
    }

    /**
     * Copy constructor.
     * 
     * @param tex
     */
    public TimeExpression(TimeExpression tex) {
        timeVariable = new TimeVar(tex.getTimeVar());
        operator = new String(tex.getOperator());
        number = tex.getNumber();
    }

    /**
     * Returns the time variable of this expression.
     * 
     * @return
     */
    public TimeVar getTimeVar() {
        return timeVariable;
    }

    /**
     * Returns the operator of this expression.
     * 
     * @return
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the number of this expression.
     * 
     * @return
     */
    public long getNumber() {
        return number;
    }

    /**
     * Sets the number of this expression.
     * 
     * @param num
     */
    public void setNumber(long num) {
        number = num;
    }

    /**
     * Sets the operator of this expression.
     * 
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = new String(operator);
    }

    /**
     * Sets the name of the time variable of this expression.
     * 
     * @param name
     */
    public void setTimeVarName(String name) {
        timeVariable.setName(name);
    }

    /**
     * Sets the value of the time variable of this expression.
     * 
     * @param val
     */
    public void setTimeValue(long val) {
        timeVariable.setValue(val);
    }

    /**
     * Returns the value of the time variable of this expression.
     * 
     * @return
     */
    public long getTimeValue() {
        return timeVariable.getValue();
    }

    /**
     * Returns the name of the time variable of this expression.
     * 
     * @return
     */
    public String getTimeVarName() {
        return timeVariable.getName();
    }

    /**
     * Returns the value of this time expression, i.e. expression is evaluated and returned.
     * 
     * @return
     */
    public long getValue() {
        long val = timeVariable.getValue();
        if (val != Constants.TIME_UD && val != Constants.RANGE_UB) {
            if (operator.equals(Constants.PLUS)) val += number; else if (operator.equals(Constants.MINUS)) val -= number;
        }
        return val;
    }

    /**
     * Returns a string representation of this time expression.
     */
    @Override
    public String toString() {
        String val = "";
        if (timeVariable.getName().equals("")) {
            val += number;
        } else {
            if (number == 0) {
                val += timeVariable.getName() + "(" + timeVariable.getValue() + ")";
            } else {
                val += timeVariable.getName() + "(" + timeVariable.getValue() + ")" + operator + Math.abs(number);
            }
        }
        return val;
    }
}
