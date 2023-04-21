package serl.equalschecker.alloydom;

public class FactNegate extends FactUnary {

    public FactNegate(int type, LogicElement element) {
        super(type, element);
    }

    @Override
    public Fact clone() {
        return this;
    }

    @Override
    public String getDefinition() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(SymbolTable.FUN_NEGATE);
        buffer.append(SymbolTable.BRACE_SQUARE_OPEN);
        buffer.append(element.getDefinition());
        buffer.append(SymbolTable.BRACE_SQUARE_CLOSE);
        return buffer.toString();
    }

    @Override
    public AlloyType getType() {
        return AlloyPrimType.INT;
    }
}
