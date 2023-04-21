package gatech.mmpm.sensor.composite;

import gatech.mmpm.Context;
import gatech.mmpm.GameState;
import gatech.mmpm.sensor.Sensor;

/**
 * Sensor that applies a OR logical operator over
 * a set of expressions.
 * 
 * Keep in mind that Make Me Play Me uses <em>fuzzy logic</em>
 * so OR works in a non-crispy way. Specifically, the evaluation
 * of a chain of ORs is maximum returned value by the children. 
 * Short-circuit evaluation is <em>not</em> used in any case. 
 * 
 * @note If you decide to change the name of this class, you
 * should also change the Builtin2Java class.
 * 
 * @author Pedro Pablo Gomez Martin
 * @date August, 2009
 */
public class OrCondition extends LogicalOperator {

    public OrCondition() {
        super();
    }

    public OrCondition(Sensor... children) {
        super(children);
    }

    public OrCondition(OrCondition rhs) {
        super(rhs);
    }

    public Object clone() {
        return new OrCondition(this);
    }

    public Object evaluate(int cycle, GameState gameState, String player, Context parameters) {
        float retValue = 0.0f;
        for (Sensor s : _children) {
            Object result;
            result = s.evaluate(cycle, gameState, player, parameters);
            if (retValue < (Float) result) retValue = (Float) result;
        }
        return retValue;
    }
}
