package luaparser;

import java.util.Vector;
import luaparser.LUA_PT.*;
import luaparser.AST.*;

/**
 * @author Frippe
 */
public class MakeASTVisitor extends SimpleVisitor {

    public Object visit(SimpleNode node, Object data) {
        throw new Error("Unimplemented method in MakeASTVisitor: \n" + "  visit(" + node.getClass().getName() + ", Object)");
    }

    public Object visit(chunk node, Object data) {
        Start start = new Start();
        try {
            start.set((Chunk) node.jjtGetChild(0).jjtAccept(this, null));
        } catch (Error e) {
            e.printStackTrace();
        }
        return start;
    }

    public Object visit(block node, Object data) {
        Chunk chunk = new Chunk();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            chunk.add((Statement) node.jjtGetChild(i).jjtAccept(this, data));
        }
        return chunk;
    }

    public Object visit(statement node, Object data) {
        if (node.jjtGetNumChildren() == 0) return new ErrorStmt();
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    public Object visit(doEnd node, Object data) {
        Chunk chunk = (Chunk) node.jjtGetChild(0).jjtAccept(this, null);
        return new DoStmt(chunk);
    }

    public Object visit(whileDoEnd node, Object data) {
        Expr cond = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Chunk chunk = (Chunk) node.jjtGetChild(1).jjtAccept(this, null);
        return new WhileStmt(cond, chunk);
    }

    public Object visit(repeatUntil node, Object data) {
        Chunk chunk = (Chunk) node.jjtGetChild(0).jjtAccept(this, null);
        Expr cond = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new RepeatStmt(chunk, cond);
    }

    public Object visit(ifThenElse node, Object data) {
        Expr cond = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Chunk chunk = (Chunk) node.jjtGetChild(1).jjtAccept(this, null);
        ElsePart elsepart = (ElsePart) node.jjtGetChild(2).jjtAccept(this, null);
        return new IfStmt(cond, chunk, elsepart);
    }

    public Object visit(elseIfEnd node, Object data) {
        if (node.jjtGetNumChildren() == 0) return null;
        if (node.jjtGetNumChildren() == 1) {
            Chunk chunk = (Chunk) node.jjtGetChild(0).jjtAccept(this, null);
            return new ElsePart(null, chunk, null);
        }
        Expr cond = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Chunk chunk = (Chunk) node.jjtGetChild(1).jjtAccept(this, null);
        ElsePart elsepart = (ElsePart) node.jjtGetChild(2).jjtAccept(this, null);
        return new ElsePart(cond, chunk, elsepart);
    }

    public Object visit(ret node, Object data) {
        Vector exprs = new Vector();
        if (node.jjtGetNumChildren() > 0) exprs = (Vector) node.jjtGetChild(0).jjtAccept(this, null);
        return new ReturnStmt(exprs);
    }

    public Object visit(brk node, Object data) {
        return new BreakStmt();
    }

    public Object visit(forNumbers node, Object data) {
        Variable var = (Variable) node.jjtGetChild(0).jjtAccept(this, null);
        Expr start = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        Expr end = (Expr) node.jjtGetChild(2).jjtAccept(this, null);
        Expr step = null;
        if (node.jjtGetNumChildren() > 4) step = (Expr) node.jjtGetChild(3).jjtAccept(this, null);
        Chunk chunk = (Chunk) node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, null);
        return new ForNumStmt(var, start, end, step, chunk);
    }

    public Object visit(forTable node, Object data) {
        Vector vars = new Vector();
        Variable var = (Variable) node.jjtGetChild(0).jjtAccept(this, null);
        vars.add(var);
        for (int i = 1; i < node.jjtGetNumChildren() - 2; i++) {
            var = (Variable) node.jjtGetChild(i).jjtAccept(this, null);
            vars.add(var);
        }
        Vector exprs = (Vector) node.jjtGetChild(node.jjtGetNumChildren() - 2).jjtAccept(this, null);
        Chunk chunk = (Chunk) node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, null);
        return new ForTableStmt(vars, exprs, chunk);
    }

    public Object visit(functionStatement node, Object data) {
        Vector names = new Vector();
        Variable name = (Variable) node.jjtGetChild(0).jjtAccept(this, null);
        names.add(name);
        Node child = node.jjtGetChild(1);
        int i = 2;
        while (child instanceof name) {
            name = (Variable) child.jjtAccept(this, null);
            names.add(name);
            child = node.jjtGetChild(i++);
        }
        Variable colon = (Variable) child.jjtAccept(this, null);
        Vector args = new Vector();
        child = node.jjtGetChild(i++);
        if (child instanceof argList) {
            args = (Vector) child.jjtAccept(this, null);
            child = node.jjtGetChild(i++);
        }
        Chunk chunk = (Chunk) child.jjtAccept(this, null);
        return new FunctionStmt(names, colon, args, chunk);
    }

    public Object visit(local node, Object data) {
        Vector vars = new Vector();
        Vector exprs = new Vector();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            if (child instanceof expressionList) {
                exprs = (Vector) child.jjtAccept(this, null);
            } else {
                Variable var = (Variable) child.jjtAccept(this, null);
                vars.add(var);
            }
        }
        return new LocalStmt(vars, exprs);
    }

    public Object visit(localFunction node, Object data) {
        Variable name = (Variable) node.jjtGetChild(0).jjtAccept(this, null);
        Vector args = new Vector();
        if (node.jjtGetNumChildren() == 3) {
            args = (Vector) node.jjtGetChild(1).jjtAccept(this, null);
        }
        Chunk chunk = (Chunk) node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, null);
        return new LocalFunctionStmt(name, args, chunk);
    }

    public Object visit(assignmentOrCall node, Object data) {
        Object prefix = node.jjtGetChild(0).jjtAccept(new ExtractPrefixVisitor(this), null);
        if (node.jjtGetNumChildren() == 1) {
            CallExpr e = (CallExpr) prefix;
            return new CallStmt(e);
        } else {
            return node.jjtGetChild(1).jjtAccept(this, prefix);
        }
    }

    public Object visit(colon node, Object data) {
        if (node.jjtGetNumChildren() != 1) return null;
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    public Object visit(arguments node, Object data) {
        Vector v = new Vector();
        if (node.jjtGetNumChildren() == 0) {
            return v;
        }
        Node o = node.jjtGetChild(0);
        if (o instanceof expressionList) {
            return o.jjtAccept(this, null);
        } else {
            Expr e = (Expr) o.jjtAccept(this, null);
            v.add(e);
            return v;
        }
    }

    public Object visit(assignment node, Object data) {
        Object var1 = data;
        Vector varN = (Vector) node.jjtGetChild(0).jjtAccept(this, var1);
        Vector ExprN = (Vector) node.jjtGetChild(1).jjtAccept(this, data);
        return new AssignStmt(varN, ExprN);
    }

    public Object visit(varList node, Object data) {
        Vector varlist = new Vector();
        varlist.add((Expr) data);
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Expr var = (Expr) node.jjtGetChild(i).jjtAccept(this, null);
            varlist.add(var);
        }
        return varlist;
    }

    public Object visit(expressionList node, Object data) {
        Vector exprlist = new Vector();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Expr e = (Expr) node.jjtGetChild(i).jjtAccept(this, data);
            exprlist.add(e);
        }
        return exprlist;
    }

    public Object visit(simple node, Object data) {
        return node.jjtAccept(new ExtractPrefixVisitor(this), data);
    }

    public Object visit(name node, Object data) {
        return new Variable(node.text);
    }

    public Object visit(number node, Object data) {
        return new Value();
    }

    public Object visit(term node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr("+", a, b);
    }

    public Object visit(factor node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr("*", a, b);
    }

    public Object visit(exponentiation node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr("^", a, b);
    }

    public Object visit(negate node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        return new UnaryExpr("-", a);
    }

    public Object visit(concat node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr("..", a, b);
    }

    public Object visit(relation node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr(">=", a, b);
    }

    public Object visit(expression node, Object data) {
        Expr a = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr b = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new BinaryExpr("AND", a, b);
    }

    public Object visit(nilTrueFalse node, Object data) {
        return new Value();
    }

    public Object visit(symbol node, Object data) {
        return new Value();
    }

    public Object visit(function node, Object data) {
        Vector arglist;
        Chunk chunk;
        if (node.jjtGetNumChildren() == 2) {
            arglist = (Vector) node.jjtGetChild(0).jjtAccept(this, null);
            chunk = (Chunk) node.jjtGetChild(1).jjtAccept(this, null);
        } else {
            arglist = new Vector();
            chunk = (Chunk) node.jjtGetChild(0).jjtAccept(this, null);
        }
        return new Function(arglist, chunk);
    }

    public Object visit(argList node, Object data) {
        Vector arglist = new Vector();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Variable v = (Variable) node.jjtGetChild(i).jjtAccept(this, null);
            arglist.add(v);
        }
        return arglist;
    }

    public Object visit(elipses node, Object data) {
        return new Variable("...");
    }

    public Object visit(tableConstructor node, Object data) {
        Vector fields = new Vector();
        if (node.jjtGetNumChildren() > 0) fields = (Vector) node.jjtGetChild(0).jjtAccept(this, null);
        return new TableValue(fields);
    }

    public Object visit(fieldList node, Object data) {
        Vector fields = new Vector();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Object field = node.jjtGetChild(i).jjtAccept(this, null);
            fields.add(field);
        }
        return fields;
    }

    public Object visit(simpleField node, Object data) {
        return new Value();
    }

    public Object visit(mapField node, Object data) {
        Variable key = (Variable) node.jjtGetChild(0).jjtAccept(this, null);
        Expr value = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new MapField(key, value);
    }

    public Object visit(exprField node, Object data) {
        Expr key = (Expr) node.jjtGetChild(0).jjtAccept(this, null);
        Expr value = (Expr) node.jjtGetChild(1).jjtAccept(this, null);
        return new ExprField(key, value);
    }
}
