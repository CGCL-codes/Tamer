package nickyb.sqleonardo.querybuilder.syntax;

public class SubQuery extends QueryExpression implements QueryTokens._Expression {

    public String toString(boolean wrap) {
        return "( " + super.toString(wrap) + " )";
    }

    public String toString() {
        return toString(false);
    }
}
