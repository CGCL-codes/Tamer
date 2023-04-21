package grammatical_competence;

import java.util.ArrayList;
import parser.*;

public class SInterface {

    public static String linearize(Constituent XP) {
        WorkingMemory.PF = "";
        return Parser.comp.SendToPF(XP);
    }

    /** Derive the different trees from a list of constituents
	 * Basically a call for the ReferenceSetDerivation.
	 */
    public static ArrayList<Constituent> derive(ArrayList<Constituent> constituents) {
        WorkingMemory.clear();
        WorkingMemory._counter = 0;
        ArrayList<Constituent> der = new ArrayList<Constituent>();
        System.out.print("RSD with constituents: ");
        WorkingMemory.StoredConstituents.add("RSD with constituents: ");
        String s = "";
        for (Constituent c : constituents) {
            System.out.print(c.Identifier + ", ");
            s += c.Identifier;
            s += ", ";
        }
        s = s.substring(0, s.length() - 3);
        WorkingMemory.StoredConstituents.add(s + "\n");
        System.out.println();
        ArrayList<Constituent> reversed = new ArrayList<Constituent>();
        for (Constituent C : constituents) reversed.add(0, C);
        Parser.comp.counter = 0;
        boolean suppressed = Competence.reporter.suppress;
        if (!suppressed) Competence.reporter.suppress = true;
        try {
            WorkingMemory.testedLAs = new ArrayList<ArrayList<Constituent>>();
            der = Parser.comp.ReferenceSetDerivation(constituents);
        } catch (InterruptedException e) {
        }
        if (!suppressed) Competence.reporter.suppress = false;
        System.out.println("***** Derivations total: ");
        System.out.println(Parser.comp.counter);
        System.out.println(der.size());
        return der;
    }
}
