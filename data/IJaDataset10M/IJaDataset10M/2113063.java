package jaque.expressions;

public final class ParameterExpression extends Expression {

    private final int _index;

    ParameterExpression(Class<?> resultType, int index) {
        super(ExpressionType.Parameter, resultType);
        if (index < 0) throw new IndexOutOfBoundsException("index");
        _index = index;
    }

    public int getIndex() {
        return _index;
    }

    @Override
    protected <T> T visit(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + _index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof ParameterExpression)) return false;
        final ParameterExpression other = (ParameterExpression) obj;
        if (_index != other._index) return false;
        return true;
    }

    @Override
    public String toString() {
        return "P" + getIndex();
    }
}
