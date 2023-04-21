package serl.equalschecker.alloydom;

public abstract class FactUnary extends Fact {

    protected LogicElement element;

    public FactUnary(int type, LogicElement element) {
        super(type);
        this.element = element;
    }

    public LogicElement getLogicElement() {
        return this.element;
    }
}
