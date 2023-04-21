package dice.expression;

import dice.evaluation.*;
import dice.value.*;
import dice.app.JDiceApp;

public final class SingleDie extends Expression {

    private final int precedence = 110;

    Expression type;

    String str = null;

    public SingleDie(Expression type) {
        this.type = type;
        this.str = toString();
    }

    public SingleDie() {
        type = null;
        this.str = toString();
    }

    public Object evaluate(Namespace ns) throws IllegalValue {
        Object type;
        if (this.type == null) type = ns.global.defaultDice.getValue(); else type = this.type.evaluate(ns);
        Object value = _evaluate(ns, type);
        if (JDiceApp.theApp.logDice.getBoolean()) JDiceApp.theApp.diceLog(null, type, value);
        return value;
    }

    private Object _evaluate(Namespace ns, Object type) throws IllegalValue {
        if (type instanceof Function) {
            JDiceApp.theApp.diceArgs.set("N", 0);
            return ((Function) type).call(ns, JDiceApp.theApp.diceArgs);
        }
        if (type instanceof ValueList) {
            ValueList choices = (ValueList) type;
            int pick = JDiceApp.theApp.diceSeed.random.nextInt(choices.size());
            return choices.get(pick);
        }
        return new Integer(1 + JDiceApp.theApp.diceSeed.random.nextInt(Values.getInt(type)));
    }

    void buildString(StringBuilder sb) {
        if (str != null) sb.append(str); else {
            sb.append('d');
            if (type != null) type.buildString(sb, precedence);
        }
    }
}
