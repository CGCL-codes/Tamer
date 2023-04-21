package org.jmlspecs.jml6.boogie.ast;

import org.jmlspecs.jml6.boogie.BoogieSource;

public abstract class BoogieExpression extends BoogieNode implements BoogieAttributeArgument {

    public BoogieExpression(BoogieNode parent) {
        super(parent);
    }

    public static BoogieExpression newEmptyExpression(BoogieNode parent) {
        return new EmptyExpression(parent);
    }
}

class EmptyExpression extends BoogieExpression {

    public EmptyExpression(BoogieNode parent) {
        super(parent);
    }

    @Override
    public void toBuffer(BoogieSource source) {
        source.append("");
    }
}

;
