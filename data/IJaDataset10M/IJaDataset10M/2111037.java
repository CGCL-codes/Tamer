package test.straightline2;

import parser.DynTok;
import test.straightline.BinOps;
import test.straightline.terminaltokens.*;
import environment.simple.*;
import static test.straightline.NonTerminals.*;
import static test.straightline.Terminals.*;

/**
 * @author Daniel Kristensen
 *
 */
public class DynInterpreter {

    public Table interpret(DynTok stm, Table t) {
        DoubleAndTable ret;
        Statements type = (Statements) stm.getKind();
        switch(type) {
            case PRINT:
                DynTok current = stm.get(ExpList);
                while (current.getKind() == ExpLists.NON_LAST) {
                    ret = evaluate((DynTok) current.get(Exp), t);
                    System.out.print(ret.getValue() + ", ");
                    t = ret.getEnvironment();
                    current = current.get(ExpList);
                }
                ret = evaluate((DynTok) current.get(Exp), t);
                System.out.print(ret.getValue());
                return ret.getEnvironment();
            case ASSIGN:
            case DEF:
                ret = evaluate((DynTok) stm.get(Exp), t);
                String varName = getId(stm);
                checkAlreadyDeclared(varName, ret.getEnvironment(), type == Statements.ASSIGN);
                return new Table(varName, ret.getValue(), ret.getEnvironment());
            case COMPOUND:
                t = interpret((DynTok) stm.get(Stm, 0), t);
                return interpret((DynTok) stm.get(Stm, 1), t);
            default:
                throw new RuntimeException("Unknown statement kind: " + type != null ? type.toString() : "null");
        }
    }

    private void checkAlreadyDeclared(String varName, Table t, boolean shouldBeDeclared) {
        boolean alreadyDeclared = t.isDefined(varName);
        if (!shouldBeDeclared && alreadyDeclared) throw new RuntimeException("The variable " + varName + " is already declared!");
        if (shouldBeDeclared && !alreadyDeclared) throw new RuntimeException("The variable " + varName + " is not declared!");
    }

    private DoubleAndTable evaluate(DynTok exp, Table env) {
        Expressions type = (Expressions) exp.getKind();
        switch(type) {
            case ID:
                return new DoubleAndTable(env.lookup(getId(exp)), env);
            case NUM:
                return new DoubleAndTable(((Num) exp.get(num)).getNum(), env);
            case BINOP:
                DoubleAndTable left = evaluate((DynTok) exp.get(Exp, 0), env);
                DoubleAndTable right = evaluate((DynTok) exp.get(Exp, 1), left.getEnvironment());
                env = right.getEnvironment();
                BinOps b = (BinOps) ((DynTok) exp.get(Binop)).getKind();
                switch(b) {
                    case plus:
                        return new DoubleAndTable(left.getValue() + right.getValue(), env);
                    case minus:
                        return new DoubleAndTable(left.getValue() - right.getValue(), env);
                    case times:
                        return new DoubleAndTable(left.getValue() * right.getValue(), env);
                    case div:
                        return new DoubleAndTable(left.getValue() / right.getValue(), env);
                    default:
                        throw new RuntimeException("Unhandled binary operator " + b);
                }
            case ESEQ:
                env = interpret((DynTok) exp.get(Stm), env);
                return evaluate((DynTok) exp.get(Exp), env);
            default:
                throw new RuntimeException("Unknown expression class: " + exp != null ? exp.getClass().getName() : "null");
        }
    }

    private String getId(DynTok exp) {
        return ((ID) exp.get(id)).getName();
    }
}
