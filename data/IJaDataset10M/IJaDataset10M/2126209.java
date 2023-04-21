package jacg.model.activity;

import jacg.model.AbstractModelElement;

public class Guard extends AbstractModelElement {

    public String getExpression() {
        String expression = super.getContent();
        expression = expression.replaceAll("\n", "");
        return expression.trim();
    }
}
