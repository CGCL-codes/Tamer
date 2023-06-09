package edu.rice.cs.drjava.config;

import edu.rice.cs.plt.lambda.Lambda3;
import edu.rice.cs.plt.lambda.Lambda;

/** Class representing ternary operations that can be inserted as variables in external processes.
  * @version $Id: TernaryOpProperty.java 5175 2010-01-20 08:46:32Z mgricken $
  */
public class TernaryOpProperty<O, P, Q, R> extends EagerProperty {

    /** Operation to perform. */
    protected Lambda3<O, P, Q, R> _op;

    /** Operator 1 name */
    protected String _op1Name;

    /** Operator 1 default */
    protected String _op1Default;

    /** Operator 2 name */
    protected String _op2Name;

    /** Operator 2 default */
    protected String _op2Default;

    /** Operator 3 name */
    protected String _op3Name;

    /** Operator 3 default */
    protected String _op3Default;

    /** Lambda to turn a string into the first operand. */
    protected Lambda<String, O> _parse1;

    /** Lambda to turn a string into the second operand. */
    protected Lambda<String, P> _parse2;

    /** Lambda to turn a string into the third operand. */
    protected Lambda<String, Q> _parse3;

    /** Lambda to format the result. */
    protected Lambda<R, String> _format;

    /** Create an eager property. */
    public TernaryOpProperty(String name, String help, Lambda3<O, P, Q, R> op, String op1Name, String op1Default, Lambda<String, O> parse1, String op2Name, String op2Default, Lambda<String, P> parse2, String op3Name, String op3Default, Lambda<String, Q> parse3, Lambda<R, String> format) {
        super(name, help);
        _op = op;
        _op1Name = op1Name;
        _op1Default = op1Default;
        _parse1 = parse1;
        _op2Name = op2Name;
        _op2Default = op2Default;
        _parse2 = parse2;
        _op3Name = op3Name;
        _op3Default = op3Default;
        _parse3 = parse3;
        _format = format;
        resetAttributes();
    }

    /** Create an eager property. */
    public TernaryOpProperty(String name, String help, Lambda3<O, P, Q, R> op, Lambda<String, O> parse1, Lambda<String, P> parse2, Lambda<String, Q> parse3, Lambda<R, String> format) {
        this(name, help, op, "op1", null, parse1, "op2", null, parse2, "op3", null, parse3, format);
    }

    /** Update the property so the value is current.
    * @param pm PropertyMaps used for substitution when replacing variables */
    public void update(PropertyMaps pm) {
        O op1;
        if (_attributes.get(_op1Name) == null) {
            _value = "(" + _name + " Error...)";
            return;
        } else {
            try {
                op1 = _parse1.value(_attributes.get(_op1Name));
            } catch (Exception e) {
                _value = "(" + _name + " Error...)";
                return;
            }
        }
        P op2;
        if (_attributes.get(_op2Name) == null) {
            _value = "(" + _name + " Error...)";
            return;
        } else {
            try {
                op2 = _parse2.value(_attributes.get(_op2Name));
            } catch (Exception e) {
                _value = "(" + _name + " Error...)";
                return;
            }
        }
        Q op3;
        if (_attributes.get(_op3Name) == null) {
            _value = "(" + _name + " Error...)";
            return;
        } else {
            try {
                op3 = _parse3.value(_attributes.get(_op3Name));
            } catch (Exception ee) {
                _value = "(" + _name + " Error...)";
                return;
            }
        }
        _value = _format.value(_op.value(op1, op2, op3));
    }

    public void resetAttributes() {
        _attributes.clear();
        _attributes.put(_op1Name, _op1Default);
        _attributes.put(_op2Name, _op2Default);
        _attributes.put(_op3Name, _op3Default);
    }
}
