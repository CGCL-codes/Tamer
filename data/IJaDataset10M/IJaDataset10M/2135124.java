package org.jmlspecs.openjml.provers;

import java.util.LinkedList;
import java.util.List;
import org.jmlspecs.openjml.JmlToken;
import org.jmlspecs.openjml.JmlTreeScanner;
import org.jmlspecs.openjml.JmlTree.JmlBBArrayAccess;
import org.jmlspecs.openjml.JmlTree.JmlBBArrayAssignment;
import org.jmlspecs.openjml.JmlTree.JmlBBArrayHavoc;
import org.jmlspecs.openjml.JmlTree.JmlBBFieldAccess;
import org.jmlspecs.openjml.JmlTree.JmlBBFieldAssignment;
import org.jmlspecs.openjml.JmlTree.JmlBinary;
import org.jmlspecs.openjml.JmlTree.JmlMethodInvocation;
import org.jmlspecs.openjml.JmlTree.JmlQuantifiedExpr;
import org.jmlspecs.openjml.esc.BasicBlocker;
import org.jmlspecs.openjml.proverinterface.ProverException;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;

/** This class converts OpenJDK ASTs (i.e., JCTree) into Strings appropriate
 * to send to CVC3; in the process, requests to define various variables may
 * be sent back to the invoking CVC3Prover.  It is implemented as a tree 
 * walker.
 * @author David Cok
 */
public class CVC3Expr extends JmlTreeScanner {

    protected CVC3Prover p;

    /** Does the translation.  
     * 
     * @param t the tree to translate
     * @param p the prover invoking this translation
     * @return the translated string
     */
    public String toCVC3(JCTree t) throws ProverException {
        try {
            result.setLength(0);
            t.accept(this);
            return result.toString();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ProverException) {
                throw (ProverException) e.getCause();
            } else {
                ProverException ee = new ProverException(e.toString());
                ee.setStackTrace(e.getStackTrace());
                throw ee;
            }
        }
    }

    /** The constructor of a new translator, connected to the given prover.
     * 
     * @param p the prover to connect with
     */
    protected CVC3Expr(CVC3Prover p) {
        this.p = p;
    }

    private StringBuilder result = new StringBuilder();

    protected void send(String s) {
        try {
            p.send(s);
            p.eatPrompt();
        } catch (ProverException e) {
            throw new RuntimeException(e);
        }
    }

    int distinctCount = 100;

    protected boolean define(String name, String type) {
        try {
            if (CVC3Prover.TYPE.equals(type)) {
                boolean n = p.rawdefine(name, type);
                if (n) return n;
                String s = "( " + "distinct$(" + name + ") = " + (++distinctCount) + " )";
                p.rawassume(s);
                return false;
            } else {
                return p.rawdefine(name, type);
            }
        } catch (ProverException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitIdent(JCIdent that) {
        try {
            String id = that.toString();
            id = id.replace('$', '_');
            id = id.replace('.', '_');
            p.rawdefine(id, convertIdentType(that));
            result.append(id);
        } catch (ProverException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertIdentType(JCIdent that) {
        Type t = that.type;
        String s;
        if (t.isPrimitive()) {
            s = convertExprType(t);
        } else if (t.tag == TypeTags.ARRAY) {
            defineArrayTypesIfNeeded(t);
            s = BasicBlocker.encodeType(t);
        } else {
            s = "REF";
        }
        if (that.sym != null && that.sym.owner instanceof Symbol.ClassSymbol && !that.sym.isStatic()) {
            s = " REF -> " + s;
        }
        return s;
    }

    public static String convertExprType(Type t) {
        String s;
        if (!t.isPrimitive()) {
            if (t instanceof ArrayType) {
                t = ((ArrayType) t).getComponentType();
                s = "refA$" + convertExprType(t);
            } else {
                s = "REF";
            }
        } else if (t.tag == TypeTags.BOOLEAN) {
            s = "BOOLEAN";
        } else if (t.tag == TypeTags.INT) {
            s = "INT";
        } else if (t.tag == TypeTags.CHAR) {
            s = "INT";
        } else {
            s = "INT";
        }
        return s;
    }

    @Override
    public void visitParens(JCParens that) {
        that.expr.accept(this);
    }

    @Override
    public void visitLiteral(JCLiteral that) {
        switch(that.typetag) {
            case TypeTags.CLASS:
                if (that.value instanceof Type) {
                    String s = "T$" + ((Type) that.value).toString();
                    s = s.replace("[]", "$$A");
                    s = s.replace("<", "$_");
                    s = s.replace(",", "__");
                    s = s.replace(">", "_$");
                    s = s.replace(".", "_");
                    define(s, CVC3Prover.TYPE);
                    result.append(s);
                } else {
                    result.append(that.toString());
                }
                break;
            case TypeTags.BOT:
                result.append("NULL");
                break;
            case TypeTags.CHAR:
                result.append(that.value.toString());
                break;
            case TypeTags.BOOLEAN:
                result.append(that.value.equals(1) ? "TRUE" : "FALSE");
                break;
            default:
                result.append(that.toString());
        }
    }

    @Override
    public void visitJmlMethodInvocation(JmlMethodInvocation that) {
        switch(that.token) {
            case BSTYPEOF:
                result.append(CVC3Prover.TYPEOF);
                result.append("(");
                that.args.get(0).accept(this);
                result.append(")");
                break;
            default:
                System.out.println("Unknown token in YicsJCExpr.visitJmlMethodInvocation: " + that.token.internedName());
                break;
        }
    }

    @Override
    public void visitApply(JCMethodInvocation that) {
        if (that.meth != null) {
            if (!(that.meth instanceof JCIdent)) that.args.get(0).accept(this); else {
                String nm = that.meth.toString();
                if (!p.isDefined(nm)) {
                    String s = "(define " + nm + "::(->";
                    for (JCExpression e : that.args) {
                        s = s + " " + p.defineType(e.type);
                    }
                    String t = p.defineType(that.type);
                    p.checkAndDefine(nm, t);
                    s = s + " " + t + "))\n";
                    try {
                        p.send(s);
                        p.eatPrompt();
                    } catch (ProverException e) {
                        throw new RuntimeException(e);
                    }
                }
                that.meth.accept(this);
                result.append("(");
                boolean first = true;
                for (JCExpression e : that.args) {
                    if (!first) {
                        result.append(",");
                        first = true;
                    }
                    e.accept(this);
                }
                result.append(")");
            }
        } else if (that instanceof JmlBBArrayAssignment) {
            JCIdent newarrs = (JCIdent) that.args.get(0);
            JCIdent oldarrs = (JCIdent) that.args.get(1);
            JCExpression arr = that.args.get(2);
            JCExpression index = that.args.get(3);
            JCExpression rhs = that.args.get(4);
            Type t = ((ArrayType) arr.type).elemtype;
            defineArrayTypesIfNeeded(t, oldarrs.toString(), newarrs.toString());
            {
                result.append("(= " + newarrs);
                result.append(" (update ");
                result.append(oldarrs);
                result.append(" (");
                arr.accept(this);
                result.append(") (update (");
                result.append(oldarrs);
                result.append(" ");
                arr.accept(this);
                result.append(") (");
                index.accept(this);
                result.append(") ");
                rhs.accept(this);
                result.append(")))");
            }
        } else if (that instanceof JmlBBFieldAssignment) {
            JCIdent newfield = (JCIdent) that.args.get(0);
            JCIdent oldfield = (JCIdent) that.args.get(1);
            JCExpression selected = that.args.get(2);
            JCExpression rhs = that.args.get(3);
            Type t = rhs.type;
            String s = BasicBlocker.encodeType(t);
            try {
                String type = " REF -> " + s;
                if (!p.checkAndDefine(newfield.toString(), type)) {
                    p.send(newfield + " : " + type + ";\n");
                    p.eatPrompt();
                }
                if (!p.checkAndDefine(oldfield.toString(), type)) {
                    p.send(oldfield + " : REF -> " + s + ";\n");
                    p.eatPrompt();
                }
                result.append("( " + newfield);
                result.append(" = update( ");
                result.append(oldfield);
                result.append(" (");
                selected.accept(this);
                result.append(") ");
                rhs.accept(this);
                result.append("))");
            } catch (ProverException e) {
                throw new RuntimeException(e);
            }
        } else if (that instanceof JmlBBArrayHavoc) {
            JCIdent newarrs = (JCIdent) that.args.get(0);
            JCIdent oldarrs = (JCIdent) that.args.get(1);
            JCExpression arr = that.args.get(2);
            JCExpression indexlo = that.args.get(3);
            JCExpression indexhi = that.args.get(4);
            JCExpression precondition = that.args.get(5);
            boolean above = ((JmlBBArrayHavoc) that).above;
            Type t = ((ArrayType) arr.type).elemtype;
            defineArrayTypesIfNeeded(t, oldarrs.toString(), newarrs.toString());
            {
                result.append("(and (forall (b::");
                result.append(p.defineType(arr.type));
                result.append(") (=> (/= b ");
                arr.accept(this);
                result.append(") (= (");
                result.append(newarrs);
                result.append(" b) (");
                result.append(oldarrs);
                result.append(" b))))");
                result.append("(/= (");
                result.append(newarrs);
                result.append(" ");
                arr.accept(this);
                result.append(") (");
                result.append(oldarrs);
                result.append(" ");
                arr.accept(this);
                result.append("))");
                result.append("(forall (i::int) (=> (not (and ");
                precondition.accept(this);
                result.append(" (<= ");
                indexlo.accept(this);
                result.append(" i) (");
                result.append(above ? "<" : "<=");
                result.append(" i ");
                indexhi.accept(this);
                result.append("))) (= ((");
                result.append(newarrs);
                result.append(" ");
                arr.accept(this);
                result.append(") i) ((");
                result.append(oldarrs);
                result.append(" ");
                arr.accept(this);
                result.append(") i)))))");
            }
        } else {
            System.out.println("UNEXPECTED");
        }
    }

    @Override
    public void visitUnary(JCUnary that) {
        switch(that.getTag()) {
            case JCTree.NOT:
                result.append("(NOT ");
                break;
            case JCTree.NEG:
                result.append("(- ");
                break;
            case JCTree.POS:
                that.arg.accept(this);
                return;
            case JCTree.COMPL:
            default:
                throw new RuntimeException(new ProverException("Unary operator not implemented for CVC3: " + that.getTag()));
        }
        that.arg.accept(this);
        result.append(")");
    }

    @Override
    public void visitBinary(JCBinary that) {
        result.append("(");
        that.lhs.accept(this);
        switch(that.getTag()) {
            case JCTree.EQ:
                if (that.lhs.type.tag == TypeTags.BOOLEAN) {
                    result.append(" <=> ");
                } else {
                    result.append(" = ");
                }
                break;
            case JCTree.AND:
                result.append(" AND ");
                break;
            case JCTree.OR:
                result.append(" OR ");
                break;
            case JCTree.PLUS:
                result.append(" + ");
                break;
            case JCTree.MINUS:
                result.append(" - ");
                break;
            case JCTree.MUL:
                result.append(" * ");
                break;
            case JCTree.DIV:
                result.append(" / ");
                break;
            case JCTree.MOD:
                result.append(" / ");
                break;
            case JCTree.NE:
                result.append(" /= ");
                break;
            case JCTree.LE:
                result.append(" <= ");
                break;
            case JCTree.LT:
                result.append(" < ");
                break;
            case JCTree.GE:
                result.append(" >= ");
                break;
            case JCTree.GT:
                result.append(" > ");
                break;
            case JCTree.BITAND:
            case JCTree.BITXOR:
            case JCTree.BITOR:
            case JCTree.SL:
            case JCTree.SR:
            case JCTree.USR:
            default:
                throw new RuntimeException(new ProverException("Binary operator not implemented for CVC3: " + that.getTag()));
        }
        that.rhs.accept(this);
        result.append(")");
    }

    @Override
    public void visitJmlBinary(JmlBinary that) {
        if (that.op == JmlToken.SUBTYPE_OF) {
            result.append(CVC3Prover.SUBTYPE);
            result.append("(");
            that.lhs.accept(this);
            result.append(",");
            that.rhs.accept(this);
            result.append(")");
            return;
        }
        if (that.op == JmlToken.JSUBTYPE_OF) {
            System.out.println("NOT IMPLEMENTED");
            return;
        }
        result.append("(");
        that.lhs.accept(this);
        if (that.op == JmlToken.IMPLIES) {
            result.append(" => ");
        } else if (that.op == JmlToken.EQUIVALENCE) {
            result.append(" <=> ");
        } else if (that.op == JmlToken.INEQUIVALENCE) {
            result.append(" XOR ");
        } else if (that.op == JmlToken.REVERSE_IMPLIES) {
            result.append(" | ");
            result.append("(! ");
            that.rhs.accept(this);
            result.append("))");
            return;
        } else {
            throw new RuntimeException(new ProverException("Binary operator not implemented for CVC3: " + that.getTag()));
        }
        result.append(" ");
        that.rhs.accept(this);
        result.append(")");
    }

    @Override
    public void visitConditional(JCConditional that) {
        result.append("(IF ");
        that.cond.accept(this);
        result.append(" THEN ");
        that.truepart.accept(this);
        result.append(" ELSE ");
        that.falsepart.accept(this);
        result.append("ENDIF)");
    }

    @Override
    public void visitIndexed(JCArrayAccess that) {
        if (!(that instanceof JmlBBArrayAccess)) {
            throw new RuntimeException(new ProverException("A BasicBlock AST should have JMLBBArrayAccess nodes for array access: " + that.getClass()));
        }
        JCIdent arraysId = ((JmlBBArrayAccess) that).arraysId;
        String arr = arraysId.toString();
        defineArrayTypesIfNeeded(that.type, arr);
        result.append("((");
        result.append(arr);
        result.append(" ");
        that.indexed.accept(this);
        result.append(") ");
        that.index.accept(this);
        result.append(")");
    }

    protected void defineArrayTypesIfNeeded(Type componenttype, String... ids) {
        if (componenttype instanceof ArrayType) defineArrayTypesIfNeeded(((ArrayType) componenttype).elemtype);
        try {
            String comptype = BasicBlocker.encodeType(componenttype);
            String ty = "refA$" + comptype;
            p.rawdefinetype(ty, "(subtype (a::ARRAYorNULL) (or (= a NULL) (subtype$ (typeof$ a) T$java.lang.Object$$A)))", CVC3Prover.ARRAY);
            for (String arr : ids) {
                if (!p.isDefined(arr)) {
                    String arrty = "(-> " + ty + " (-> int " + comptype + "))";
                    p.rawdefine(arr, arrty);
                }
            }
        } catch (ProverException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitSelect(JCFieldAccess that) {
        if (!(that instanceof JmlBBFieldAccess)) {
            throw new RuntimeException(new ProverException("A BasicBlock AST should have JmlBBFieldAccess nodes for field access: " + that.getClass()));
        }
        JCIdent fieldId = ((JmlBBFieldAccess) that).fieldId;
        Type t = that.type;
        try {
            String s = p.defineType(t);
            String nm = fieldId.toString();
            p.rawdefine(nm, " REF -> " + s);
        } catch (ProverException e) {
            throw new RuntimeException(e);
        }
        result.append(fieldId);
        result.append("(");
        that.selected.accept(this);
        result.append(")");
    }

    @Override
    public void visitJmlQuantifiedExpr(JmlQuantifiedExpr that) {
        result.append("(forall (");
        List<String> oldTypes = new LinkedList<String>();
        do {
            for (JCVariableDecl decl : that.decls) {
                String ytype = p.defineType(decl.type);
                String id = decl.name.toString();
                result.append(id);
                result.append("::");
                result.append(ytype);
                result.append(" ");
                String oldType = p.getTypeString(id);
                oldTypes.add(oldType);
                p.declare(id, ytype);
            }
            if (that.value instanceof JmlQuantifiedExpr) {
                that = (JmlQuantifiedExpr) that.value;
            } else break;
        } while (true);
        result.append(") ");
        if (that.range == null) {
            that.value.accept(this);
        } else {
            result.append("(=> ");
            that.range.accept(this);
            result.append(" ");
            that.value.accept(this);
            result.append(")");
        }
        result.append(")");
        for (JCVariableDecl decl : that.decls) {
            String id = decl.name.toString();
            String ot = oldTypes.remove(0);
            if (ot == null) {
                p.removeDeclaration(id);
            } else {
                p.declare(id, ot);
            }
        }
    }

    @Override
    public void visitTypeCast(JCTypeCast that) {
        result.append("(");
        result.append(CVC3Prover.CAST);
        result.append(" ");
        that.expr.accept(this);
        result.append(" ");
        that.clazz.accept(this);
        result.append(")");
    }

    @Override
    public void visitTree(JCTree tree) {
        Exception e = new ProverException("Did not expect to call a visit method in CVC3Expr: " + tree.getClass());
        throw new RuntimeException(e);
    }
}
