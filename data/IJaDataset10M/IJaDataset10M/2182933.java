package de.tum.in.botl.transformer.implementation;

import java.util.Iterator;
import java.util.Vector;
import de.tum.in.botl.metamodel.implementation.ObjectVariable;
import de.tum.in.botl.metamodel.implementation.Term;

/**
 * @author marschal
 * 28.08.2003
 */
public class IncalculableValueException extends TransformationException {

    private Term term;

    private ObjectVariable ov;

    private Vector results;

    private String s;

    /**
	 * 
	 */
    public IncalculableValueException() {
        super();
        s = "";
    }

    /**
	 * @param s
	 */
    public IncalculableValueException(String s) {
        super(s);
        this.s = s;
    }

    public IncalculableValueException(Term t, ObjectVariable v, Vector r) {
        super();
        this.term = t;
        this.ov = v;
        this.results = r;
        s = new String("Rule " + ov.getModelVariable().getRule().getName() + " contains " + "a term " + term.getValue() + " in the object variable " + ov.getId() + " of the type " + ov.getTheClass()) + " that could not be evaluated. ";
        if (results.size() == 0) s += "There is no possible value for this term."; else if (results.size() > 1) {
            s += "Possible values for this term are: ";
            for (Iterator it = results.iterator(); it.hasNext(); ) s += "\"" + ((String) it.next()) + "\", ";
        }
    }

    public Term getTerm() {
        return term;
    }

    public ObjectVariable getObjectVariable() {
        return ov;
    }

    public Vector getResults() {
        return results;
    }

    public String getMessage() {
        return s;
    }
}
