package ru.whale.lang.structure;

import ru.util.WhaleUtil;
import ru.whale.lang.structure.expression.Expression;
import ru.whale.lang.structure.expression.Namespace;
import ru.whale.lang.types.Type;
import java.util.ArrayList;

public class MethodCallOperation extends Expression {

    private String myName;

    private ArrayList<Expression> myArgument;

    private Expression myOperand;

    private ArrayList<Expression> myPredicates = new ArrayList<Expression>();

    public MethodCallOperation(Expression arg, String val, ArrayList<Expression> e) {
        myOperand = arg;
        myName = val;
        myArgument = e;
    }

    public String toString() {
        return myOperand + "." + myName + "(" + WhaleUtil.toString(myArgument) + ")";
    }

    public Type calculateType(Namespace namespace) {
        myPredicates.addAll(namespace.getPredicates());
        Type objectType = myOperand.calculateType(namespace);
        Type[] argTypes = new Type[myArgument.size()];
        for (int i = 0; i < myArgument.size(); i++) {
            argTypes[i] = myArgument.get(i).calculateType(namespace);
        }
        return objectType.getOperationType(myName, argTypes);
    }

    public ArrayList<Expression> getPredicates() {
        return myPredicates;
    }
}
