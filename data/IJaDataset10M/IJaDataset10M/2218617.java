package edu.vub.at.objects.natives;

import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.objects.ATClosure;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.crime_at.bridge.AmbientTalk2CRIME;

public class OBJCrime extends NATFactSpace {

    public OBJCrime() throws InterpreterException {
        super(null, AmbientTalk2CRIME.getEngine());
    }

    public ATClosure meta_doesNotUnderstand(ATSymbol selector) throws InterpreterException {
        meta_defineField(selector, new NATFactSpace(selector, itsEngine));
        return impl_lookupAccessor(selector);
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<crime engine:" + itsEngine.hashCode() + ">");
    }
}
