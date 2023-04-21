package no.uio.ifi.kjetilos.javatraits.parser;

import java.util.ArrayList;
import java.util.List;

public class OperatorExpression extends SimpleNode implements Visitable {

    List<String> operatorList = new ArrayList<String>();

    public OperatorExpression(int i) {
        super(i);
    }

    public OperatorExpression(JavaTParser p, int i) {
        super(p, i);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void pushOperator(String operator) {
        operatorList.add(operator);
    }

    @Override
    public String toString() {
        return super.toString() + " operators " + operatorList;
    }

    public String getOperator(int i) {
        return operatorList.get(i);
    }
}
