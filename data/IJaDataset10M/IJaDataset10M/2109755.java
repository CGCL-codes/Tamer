package Alge;

import Absyn.ColName;
import Absyn.SelectExpr;

public class Project extends Relation {

    public Absyn.SelectExpr select_expr;

    public Relation sub;

    public ColName group_col_name;

    public Condition group_condition;

    public Project(SelectExpr select_expr, Relation sub, ColName group_col_name, Condition group_condition) {
        this.select_expr = select_expr;
        this.sub = sub;
        this.group_col_name = group_col_name;
        this.group_condition = group_condition;
    }
}
