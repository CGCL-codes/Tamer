package org.jmlspecs.jml6.boogie.translation;

import org.jmlspecs.jml6.boogie.ast.BoogieMapTypeReference;
import org.jmlspecs.jml6.boogie.ast.BoogieTypeDeclaration;
import org.jmlspecs.jml6.boogie.ast.BoogieTypeReference;
import org.jmlspecs.jml6.boogie.ast.BoogieVisitor;

/**
 * Makes sure all reference types have been declared
 */
public class BoogieTypeDeclaratorVisitor extends BoogieVisitor {

    public boolean visit(BoogieMapTypeReference term) {
        if (term.getScope().lookupType(term.getTypeName()) == null) {
            declareType(term);
        }
        return true;
    }

    public boolean visit(BoogieTypeReference term) {
        if (term.getTypeName().equals("Ref")) return false;
        if (term.getScope().lookupType(term.getTypeName()) == null) declareType(term);
        return true;
    }

    private void declareType(BoogieTypeReference type) {
        type.getScope().addType(new BoogieTypeDeclaration(type, null, type.getScope()));
    }
}
