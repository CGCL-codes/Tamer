package ru.whale.lang.structure;

import ru.whale.lang.structure.expression.Namespace;
import ru.whale.lang.structure.expression.Expression;
import ru.whale.lang.types.Type;
import ru.whale.runtime.WhaleState;
import ru.whale.runtime.WhaleObject;
import ru.whale.UnimplementedException;
import java.util.List;
import java.util.ArrayList;

public class FieldOperation extends Expression {

    private String myName;

    private Expression myArg;

    public FieldOperation(Expression arg, String val) {
        myArg = arg;
        myName = val;
    }

    public String getText(String tabs) {
        return myName;
    }

    @Override
    public String toString() {
        return myArg + "." + myName;
    }

    public List<SyntaxTreeElement> getChildren() {
        return null;
    }

    public Type calculateType(Namespace namespace) {
        return null;
    }

    public WhaleState createState() {
        return null;
    }

    public void check(String query) {
    }

    public ArrayList<Expression> getPredicates() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldOperation that = (FieldOperation) o;
        if (myArg != null ? !myArg.equals(that.myArg) : that.myArg != null) return false;
        if (myName != null ? !myName.equals(that.myName) : that.myName != null) return false;
        return true;
    }
}
