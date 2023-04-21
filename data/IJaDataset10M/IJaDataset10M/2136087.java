package org.jmlspecs.jml6.boogie.ast;

import org.jmlspecs.annotation.Nullable;
import org.jmlspecs.jml6.boogie.BoogieSource;

/**
 * BoogieIdentifier: BoogieType
 * @author Alexandre Tristan St-Cyr
 *
 */
public class BoogieFunctionArgument extends BoogieNode {

    BoogieIdentifier identifier = null;

    BoogieType type = BoogieType.UNKNOWN_TYPE;

    BoogieFunctionArgument(BoogieNode parent) {
        super(parent);
    }

    @Override
    public void toBuffer(BoogieSource source) {
        if (identifier != null) {
            identifier.toBuffer(source);
            source.append(": ");
        }
        type.toBuffer(source);
    }
}
