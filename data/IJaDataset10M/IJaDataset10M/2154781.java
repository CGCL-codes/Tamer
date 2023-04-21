package jaga.testing.external;

import jaga.external.bucanon.*;

/**
 *
 * @author  mmg20
 */
public class Bucanon {

    HTMLDisplay htmlD = new HTMLDisplay();

    /** Creates a new instance of Bucanon */
    public Bucanon() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoFormulaException {
        Bucanon t = new Bucanon();
        t.qm(FormulaFactory.getAtomForm("a"));
        TheoForm a = FormulaFactory.getAtomForm("a");
        TheoForm b = FormulaFactory.getAtomForm("b");
        TheoForm notB = FormulaFactory.getNegation(b);
        TheoForm aAndB = FormulaFactory.getConjunction(a, b);
        TheoForm aAndNotB = FormulaFactory.getConjunction(a, notB);
        TheoForm f = FormulaFactory.getDisjunction(aAndB, aAndNotB);
        t.qm(f);
        f = (TheoForm) Parser.parse("[ [ a,b ] ; [ b,c ] ]");
        t.qm(f);
    }

    void qm(TheoForm f) {
        QuineMcCluskey q = new QuineMcCluskey(f, QuineMcCluskey.DNF, QuineMcCluskey.HORIZONTAL);
        System.out.println("Simplified is ");
        System.out.println("" + htmlD.line(q.pnf));
    }

    void qm(String str) throws NoFormulaException {
        Formula f = Parser.parse(str);
        QuineMcCluskey q = new QuineMcCluskey((TheoForm) f, QuineMcCluskey.DNF, QuineMcCluskey.HORIZONTAL);
        System.out.println("Simplified is ");
        System.out.println("" + htmlD.line(q.pnf));
    }

    void display(String str) throws NoFormulaException {
        Formula f = Parser.parse(str);
        System.out.println("After parsing: " + htmlD.line(f));
        Formula fopt = LitFormAsFormula.asFormula(PDNFCanonizer.convertForm(f));
        System.out.println("After opt " + PDNFCanonizer.convertForm(f));
        System.out.println("After optimizing to DNF: " + htmlD.line(fopt));
    }

    void parse(String str) {
        try {
            Formula f = Parser.parse(str);
            System.out.println("Is theo form = " + f.isTheoForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
