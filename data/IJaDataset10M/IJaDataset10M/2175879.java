package kpython.backend.ir.canon;

import kpython.backend.ir.tree.IRTreeVisitor;
import kpython.backend.ir.tree.Stmt;

public class MinusFunction extends Stmt {

    public StringBuilder code = new StringBuilder();

    public MinusFunction() {
        code.append("goto minus_skip;\n");
        code.append("minus:\n");
        code.append("if(sp->locals[0].type != sp->locals[1].type)\n");
        code.append("goto exit;\n");
        code.append("if(sp->locals[0].type == v_string)\n");
        code.append("goto exit;\n");
        code.append("if(sp->locals[0].type == v_boolean)\n");
        code.append("goto exit;\n");
        code.append("if(sp->locals[0].type == v_integer)\n");
        code.append("\t_rv->value.integer = sp->locals[0].value.integer - sp->locals[1].value.integer; \n");
        code.append("if(sp->locals[0].type == v_float)\n");
        code.append("\t_rv->value.real = sp->locals[0].value.real - sp->locals[1].value.real; \n");
        code.append("_rv->type = sp->locals[0].type;\n");
        code.append("goto redirector;\n");
        code.append("minus_skip:\n");
    }

    @Override
    public Object accept(IRTreeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void printTree(String tab) {
        System.out.println(tab + this.getClass().getSimpleName());
        tab = tab + "\t";
        System.out.println(tab + "Label minus");
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
