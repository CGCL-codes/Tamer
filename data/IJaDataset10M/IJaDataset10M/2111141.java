package org.jato.expression;

import org.jato.JatoException;
import org.jato.tags.*;
import org.jato.*;
import org.jdom.*;

/**
 * @deprecated InFunction
 */
public class SelfFunction extends ReferenceFunction {

    public SelfFunction() {
        super("self.");
    }

    protected Object getThisObject(Interpreter jato, ScriptTag tag, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        return xmlIn;
    }
}
