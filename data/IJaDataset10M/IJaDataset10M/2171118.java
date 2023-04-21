package parser.valueObjects;

public class AssertionPart {

    public Severity severity;

    public Expression condition;

    public Expression report;

    public AssertionPart(Expression condition, Expression report, Severity severity) {
        this.condition = condition;
        this.report = report;
        this.severity = severity;
    }

    public String compose() {
        return "c.assert(" + (condition != null ? condition.compose() : "true") + ", " + report + ", " + severity.compose() + ");";
    }
}
