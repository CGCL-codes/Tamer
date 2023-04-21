package kpython.backend.ir.canon;

import kpython.backend.ir.tree.IRTreeVisitor;
import kpython.backend.ir.tree.Stmt;

public class NotFunction extends Stmt {

    public StringBuilder code = new StringBuilder();

    public NotFunction() {
        code.append("goto not_skip;\n");
        code.append("not:\n");
        code.append("\t_rv->value.boolean = !sp->locals[0].value.boolean; \n");
        code.append("_rv->type = v_boolean;\n");
        code.append("goto redirector;\n");
        code.append("not_skip:\n");
    }

    @Override
    public Object accept(IRTreeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void printTree(String tab) {
        System.out.println(tab + this.getClass().getSimpleName());
        tab = tab + "\t";
        System.out.println(tab + "Label not");
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
