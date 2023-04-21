package sf2.service.strategy;

public class StrategyException extends Exception {

    private static final long serialVersionUID = -8553887027813453474L;

    public StrategyException(String s) {
        super(s);
    }

    public StrategyException(String s, Throwable t) {
        super(s, t);
    }

    public StrategyException(Throwable t) {
        super(t);
    }
}
