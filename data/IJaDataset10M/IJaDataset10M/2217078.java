package annone.filters;

public class GreaterOrEqual extends Filter {

    private Object left;

    private Object right;

    public GreaterOrEqual(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public Filter simplify() {
        return new Or(new Greater(left, right), new Equal(left, right));
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (left instanceof Filter) b.append('(').append(left).append(')'); else b.append(left);
        b.append(" >= ");
        if (right instanceof Filter) b.append('(').append(right).append(')'); else b.append(right);
        return b.toString();
    }
}
