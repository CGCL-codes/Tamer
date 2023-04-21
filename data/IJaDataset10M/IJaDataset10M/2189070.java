package org.jmlspecs.jml6.boogie.ast;

import org.jmlspecs.jml6.boogie.BoogieSource;

/**
 * free? ensures BoogieExpression;
 * @author Alexandre Tristan St-Cyr
 *
 */
public class BoogieEnsures extends BoogieSpecification {

    boolean isFree;

    BoogieExpression expression = BoogieExpression.newEmptyExpression(this);

    public BoogieEnsures(boolean isFree, BoogieNode parent) {
        super(parent);
        this.isFree = isFree;
    }

    public BoogieExpression getExpression() {
        return expression;
    }

    public void setExpression(BoogieExpression expression) {
        this.expression = expression;
    }

    @Override
    public void toBuffer(BoogieSource source) {
        if (isFree) source.append("free ");
        source.append("ensures ");
        expression.toBuffer(source);
        source.append(";");
    }
}
