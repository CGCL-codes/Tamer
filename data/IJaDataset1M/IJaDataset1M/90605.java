package query.parser;

/**
 *
 * @author dzb
 */
public class SetOperation {

    public enum SetType {

        None, Union, UnionAll, Intersect, Except
    }

    private SelectStatement st;

    private SetType type;

    public SelectStatement getSelectStatement() {
        return st;
    }

    public void setSelectStatement(SelectStatement st) {
        this.st = st;
    }

    public SetType getSetType() {
        return type;
    }

    public void setSetType(SetType type) {
        this.type = type;
    }
}
