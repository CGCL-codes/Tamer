package matuya.sjm.examples.mechanics;

import matuya.sjm.parse.*;
import matuya.sjm.parse.tokens.*;

/**
 * Show the use of new subclasses of <code>Terminal</code>.
 * 
 * @author Steven J. Metsker
 *
 * @version 1.0 
 */
public class ShowNewTerminals {

    /**
 * Show the use of new subclasses of <code>Terminal</code>.
 */
    public static void main(String[] args) {
        Parser variable = new UppercaseWord();
        Parser known = new LowercaseWord();
        Parser term = new Alternation().add(variable).add(known);
        variable.setAssembler(new Assembler() {

            public void workOn(Assembly a) {
                Object o = a.pop();
                a.push("VAR(" + o + ")");
            }
        });
        known.setAssembler(new Assembler() {

            public void workOn(Assembly a) {
                Object o = a.pop();
                a.push("KNOWN(" + o + ")");
            }
        });
        System.out.println(new Repetition(term).bestMatch(new TokenAssembly("member X republican democrat")));
    }
}
