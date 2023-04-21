package xcordion.api;

public class ItemAndExpression<T> {

    private final T item;

    private final String expression;

    public ItemAndExpression(T item, String expression) {
        this.item = item;
        this.expression = expression;
    }

    public T getItem() {
        return item;
    }

    public String getExpression() {
        return expression;
    }
}
