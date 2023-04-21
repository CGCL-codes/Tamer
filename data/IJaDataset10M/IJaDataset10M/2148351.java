package uk.ac.lkl.migen.system.expresser.ui.uievent;

/**
 * 
 * Represents the popping up of a menu for an expression. If kind = "Main" then
 * this is the popup menu for the expression. if kind = "Drop" then an
 * expression was dropped on an expression.
 * 
 * @author Ken Kahn
 * 
 */
public class ExpressionMenuPopupEvent extends MenuPopupEvent {

    protected String expressionAsString;

    protected String kind;

    public ExpressionMenuPopupEvent(String expressionPanelId, String expressionAsString, String kind) {
        super(expressionPanelId);
        this.expressionAsString = expressionAsString;
        this.kind = kind;
    }

    @Override
    public String logMessage() {
        return kind + " menu popped up (" + getExpressionPanelId() + " " + expressionAsString + ")";
    }

    public String getExpressionPanelId() {
        return getSource();
    }

    public String getKind() {
        return kind;
    }

    public String getExpressionAsString() {
        return expressionAsString;
    }
}
