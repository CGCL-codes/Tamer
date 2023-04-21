package sisc.data;

import java.io.IOException;
import java.util.Set;
import sisc.io.ValueWriter;
import sisc.ser.Serializer;
import sisc.ser.Deserializer;
import sisc.util.ExpressionVisitor;

public class ExpressionValue extends Value {

    public Expression e;

    public ExpressionValue(Expression e) {
        this.e = e;
    }

    public Value setAnnotation(Symbol key, Value v) {
        return e.setAnnotation(key, v);
    }

    public Set getAnnotationKeys() {
        return e.getAnnotationKeys();
    }

    public Pair getAnnotations() {
        return e.getAnnotations();
    }

    public Value getAnnotation(Symbol key) {
        return e.getAnnotation(key);
    }

    public void display(ValueWriter w) throws IOException {
        w.append("#<").append(liMessage(SISCB, "expression")).append(' ').append(e.express()).append('>');
    }

    public int valueHashCode() {
        return e.hashCode();
    }

    public boolean valueEqual(Value v) {
        if (v == this) return true;
        if (!(v instanceof ExpressionValue)) return false;
        return e.equals(((ExpressionValue) v).e);
    }

    public void serialize(Serializer s) throws IOException {
        s.writeExpression(e);
    }

    public void deserialize(Deserializer s) throws IOException {
        e = s.readExpression();
    }

    public boolean visit(ExpressionVisitor v) {
        return v.visit(e);
    }
}
