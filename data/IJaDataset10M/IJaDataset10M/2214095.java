package nzdis.simplesl.fipakif;

import nzdis.simplesl.*;

/**
 * Pretty printer for dANSI KIF.
 * 
 *<br><br>
 * PrettyPrinter.java<br>
 * Created: Thu Jun  7 15:39:25 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.2 $ $Date: 2001/06/21 07:56:37 $
 */
public class PrettyPrinter extends VisitorAdapter {

    private int indent = 0;

    private StringBuffer buf = new StringBuffer();

    public String getString() {
        return buf.toString();
    }

    boolean needSpace = false;

    void checkSpace() {
        if (needSpace) {
            buf.append(" ");
            needSpace = false;
        }
    }

    void indent() {
        String s = new String("");
        for (int i = 0; i < indent; i++) s += " ";
        buf.append(s);
    }

    public void defaultOut() {
        buf.append(")");
        needSpace = false;
    }

    public void inActionAlternative(ActionAlternative node) {
        checkSpace();
        buf.append("( | ");
        needSpace = false;
    }

    public void inActionExpression(ActionExpression node) {
        checkSpace();
        buf.append("(action ");
        needSpace = false;
    }

    public void inActionSequence(ActionSequence node) {
        checkSpace();
        buf.append("( ;  ");
        needSpace = false;
    }

    public void inAllReferentialExpression(AllReferentialExpression node) {
        checkSpace();
        buf.append("(forall  ");
        needSpace = false;
    }

    public void inAnyReferentialExpression(AnyReferentialExpression node) {
        checkSpace();
        buf.append("(any  ");
        needSpace = false;
    }

    public void inAtomicProposition(AtomicProposition node) {
        checkSpace();
        buf.append("(forall  ");
        needSpace = false;
    }

    public void inConjunction(Conjunction node) {
        checkSpace();
        buf.append("(and ");
        needSpace = false;
    }

    public void inContent(Content node) {
    }

    public void outContent(Content node) {
    }

    public void inDisjunction(Disjunction node) {
        checkSpace();
        buf.append("(or ");
        needSpace = false;
    }

    public void inEquivalence(Equivalence node) {
        checkSpace();
        buf.append("(<=> ");
        needSpace = false;
    }

    public void inExistential(Existential node) {
        checkSpace();
        buf.append("(exists ");
        needSpace = false;
    }

    public void outFalse(False node) {
        checkSpace();
        buf.append("false");
        needSpace = true;
    }

    public void outFloatLiteral(FloatLiteral node) {
        checkSpace();
        buf.append(node.toString());
        needSpace = true;
    }

    public void inFunctionalTerm(FunctionalTerm node) {
        checkSpace();
        buf.append("(");
        needSpace = false;
    }

    public void inImplication(Implication node) {
        checkSpace();
        buf.append("(=> ");
        needSpace = false;
    }

    public void outIntegerLiteral(IntegerLiteral node) {
        checkSpace();
        buf.append(node.toString());
        needSpace = true;
    }

    public void inIotaReferentialExpression(IotaReferentialExpression node) {
        checkSpace();
        buf.append("(iota ");
        needSpace = false;
    }

    public void inNegation(Negation node) {
        checkSpace();
        buf.append("(not ");
        needSpace = false;
    }

    public void inParameter(Parameter node) {
        checkSpace();
        buf.append("%");
        needSpace = false;
    }

    public void outParameter(Parameter node) {
    }

    public void inPredicate(Predicate node) {
        checkSpace();
        buf.append("(");
        needSpace = false;
    }

    public void inSequence(Sequence node) {
        checkSpace();
        buf.append("(listof ");
        needSpace = false;
    }

    public void inSet(Set node) {
        checkSpace();
        buf.append("(setof ");
        needSpace = false;
    }

    public void outStringLiteral(StringLiteral node) {
        checkSpace();
        buf.append(node.toString());
        needSpace = true;
    }

    public void outSymbol(Symbol node) {
        checkSpace();
        if (node.getValue().startsWith(":")) buf.append(" ");
        buf.append(node.toString());
        needSpace = true;
    }

    public void outTrue(True node) {
        checkSpace();
        buf.append("true");
        needSpace = true;
    }

    public void inUniversal(Universal node) {
        checkSpace();
        buf.append("(all ");
        needSpace = false;
    }

    public void inVariable(Variable node) {
        buf.append(" ");
    }

    public void outVariable(Variable node) {
        buf.append(node.getValue());
        needSpace = true;
    }

    public void outWord(Word node) {
        checkSpace();
        buf.append(node.toString());
        needSpace = true;
    }

    public void outZtring(Ztring node) {
        checkSpace();
        buf.append(node.toString());
        needSpace = true;
    }
}
