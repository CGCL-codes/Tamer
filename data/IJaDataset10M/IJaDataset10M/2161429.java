package jolie.runtime;

public class NotCondition implements Condition {

    private Condition condition;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    public boolean evaluate() {
        return (!(condition.evaluate()));
    }
}
