package com.agentfactory.eis;

import java.util.LinkedList;
import com.agentfactory.clf.interpreter.CoreUtilities;
import com.agentfactory.clf.lang.ITerm;
import com.agentfactory.clf.lang.IntegerTerm;
import com.agentfactory.clf.lang.RealTerm;
import com.agentfactory.clf.lang.StringTerm;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class Utilities {

    public static Parameter toParameter(ITerm term) {
        if (RealTerm.class.isInstance(term)) {
            return new Numeral(((RealTerm) term).value());
        } else if (IntegerTerm.class.isInstance(term)) {
            return new Numeral(((IntegerTerm) term).value());
        } else if (StringTerm.class.isInstance(term)) {
            return new Identifier(((StringTerm) term).value());
        }
        return new Identifier(CoreUtilities.presenter.toString(term));
    }
}
