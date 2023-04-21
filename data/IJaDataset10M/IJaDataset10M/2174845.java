package bd.com.escenic.flexilunch.model;

/**
 * $Id: PaymentImpl.java 14 2009-06-03 09:39:19Z shihab.uddin@gmail.com $.
 *
 * @author <a href="mailto:shihab.uddin@gmail.com">Shihab Uddin</a>
 * @version $Revision: 14 $
 */
public class PaymentImpl extends AbstractEntity implements Payment {

    private User mUser;

    private double mAmount;

    /**
   * {@inheritDoc}
   */
    @Override
    public final Type getType() {
        return Type.PAYMENT;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public User getUser() {
        return mUser;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setUser(final User pUser) {
        mUser = pUser;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public double getAmount() {
        return mAmount;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setAmount(final double pAmount) {
        mAmount = pAmount;
    }
}
