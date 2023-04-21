package nzdis.simplesl.standard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nzdis.simplesl.*;

/**
 * Represents an assignment.
 * 
 *<br><br>
 * Assignment.java<br>
 * Created: Thu Jun 21 18:42:45 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.2 $ $Date: 2001/06/22 04:16:42 $
 */
public class Assignment extends FunctionalTerm implements OntologyConcept {

    public Assignment() {
        super(new Symbol("unknown"), new Term[0]);
    }

    public Assignment(Symbol s, Term[] terms) {
        super(s, terms);
    }

    public OntologyConcept evaluate(Context ctx) throws OntologyException {
        OntologyConcept v = ctx.getValue(this);
        if (v != null) return v;
        final Term[] terms = getTerms();
        final List vars = new ArrayList();
        OntologyConcept concreteValue = null;
        for (int i = 0; i < terms.length; i++) {
            OntologyConcept temp = ((OntologyConcept) terms[i]).evaluate(ctx);
            if (temp instanceof Word) {
                vars.add(terms[i]);
            } else if (temp instanceof Constant) {
                if (concreteValue != null && !concreteValue.equals(temp)) {
                    throw new ContradictionException("Contradiction in assignment: " + concreteValue + " " + temp);
                }
                concreteValue = temp;
            }
        }
        if (concreteValue == null) return this;
        final Iterator iter = vars.iterator();
        while (iter.hasNext()) {
            Word w = (Word) iter.next();
            OntologyConcept w_res = ctx.resolve(w);
            if (w_res == null) {
                ctx.put(w.getValue(), concreteValue);
                ctx.setChanged();
            } else if (!w_res.equals(concreteValue)) {
                throw new ContradictionException("Contradiction in assignment: " + concreteValue + " " + w_res);
            }
        }
        ctx.setValue(this, concreteValue);
        return concreteValue;
    }

    public OntologyConcept create(Object o) {
        return create((FunctionalTerm) o);
    }

    public OntologyConcept create(FunctionalTerm src) {
        final Term[] terms = src.getTerms();
        if (terms == null) {
            System.out.println("ERROR: assignment must have a list of term arguments");
            return null;
        }
        return new Assignment(src.getSymbol(), terms);
    }

    public boolean isMatch(Object o) {
        return isMatch((FunctionalTerm) o);
    }

    public boolean isMatch(FunctionalTerm src) {
        return src.getSymbol().getValue().equals("=");
    }

    public boolean equals(OntologyConcept concept) {
        return (this == concept);
    }
}
