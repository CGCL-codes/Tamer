package net.sf.saxon.query;

import net.sf.saxon.expr.BindingReference;
import net.sf.saxon.expr.VariableDeclaration;
import net.sf.saxon.instruct.GlobalVariable;
import net.sf.saxon.instruct.Executable;
import net.sf.saxon.trans.XPathException;
import java.util.Collections;
import java.util.Iterator;

/**
 *  An UndeclaredVariable object is created when a reference is encountered to a variable
 *  that has not yet been declared. This can happen as a result of recursive module imports.
 *  These references are resolved at the end of query parsing.
 */
public class UndeclaredVariable extends GlobalVariableDefinition {

    public UndeclaredVariable() {
    }

    public void transferReferences(VariableDeclaration var) {
        Iterator iter = references.iterator();
        while (iter.hasNext()) {
            BindingReference ref = (BindingReference) iter.next();
            var.registerReference(ref);
        }
        references = Collections.EMPTY_LIST;
    }

    public GlobalVariable compile(Executable exec, int slot) throws XPathException {
        throw new UnsupportedOperationException("Attempt to compile a place-holder for an undeclared variable");
    }
}
