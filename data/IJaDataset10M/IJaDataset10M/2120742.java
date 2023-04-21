package upm.fi.oeg.sparql.lqp.operators;

import java.util.List;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.BinaryOperator;

/**
 * SPARQL set operator abstract class: UNION
 * @author Carlos Buil Aranda
 * @email cbuil@fi.upm.es
 *
 */
public abstract class SparqlSetOperator extends BinaryOperator {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Universidad Politecnica de Madrid, 2011";

    /** Not a bag operator. */
    protected boolean mBagOperator = true;

    /**
     * Checks if operator is a bag operator.
     * 
     * @return <code>true</code> if operator is a bag operator,
     *         <code>false</code> if it is a set operator
     */
    public boolean isBagOperator() {
        return mBagOperator;
    }

    /**
     * {@inheritDoc}
     */
    public void update() throws LQPException {
        super.update();
        mOperatorHeading = new HeadingImpl(mLeftChildOperator.getHeading());
    }

    /**
     * {@inheritDoc}
     */
    public void validate() throws LQPException {
        super.validate();
        List<Attribute> lAttr = mLeftChildOperator.getHeading().getAttributes();
        List<Attribute> rAttr = mRightChildOperator.getHeading().getAttributes();
    }
}
