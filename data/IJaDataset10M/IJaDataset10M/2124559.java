package org.likken.util.parser;

/**
 * @author Stephane Boisson <s.boisson@focal-net.com> 
 * @version $Revision: 1.1.1.1 $ $Date: 2000/12/07 00:12:40 $
 */
public class StartToken implements Token {

    public Object getValue() {
        return "(";
    }

    public void accept(TokenVisitor aVisitor) {
        aVisitor.visitStartToken(this);
    }

    public String toString() {
        return "<START>";
    }
}
