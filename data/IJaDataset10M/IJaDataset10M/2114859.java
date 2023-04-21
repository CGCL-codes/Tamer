package annone.engine.local.builder.nodes;

public class TextNode extends ExpressionNode {

    private String value;

    public TextNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
