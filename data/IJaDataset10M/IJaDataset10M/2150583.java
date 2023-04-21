package net.sf.ofx4j.domain.data.investment.transactions;

import net.sf.ofx4j.meta.Aggregate;
import net.sf.ofx4j.meta.ChildAggregate;
import net.sf.ofx4j.meta.Element;

/**
 * Transaction for buying debt (i.e. bonds, CDs, etc.,).
 * @see "Section 13.9.2.4.4, OFX Spec"
 *
 * @author Jon Perlow
 */
@Aggregate("BUYDEBT")
public class BuyDebtTransaction extends BaseBuyInvestmentTransaction {

    private Double accruedInterest;

    public BuyDebtTransaction() {
        super(TransactionType.BUY_DEBT);
    }

    /**
   * Gets the amount of accrued interest on the debt. This is an optional field according to the
   * OFX spec.
   * @see "Section 13.9.2.4.4, OFX Spec"
   *
   * @return the amount of accrued interest
   */
    @Element(name = "ACCRDINT", order = 20)
    public Double getAccruedInterest() {
        return accruedInterest;
    }

    /**
   * Sets the amount of accrued interest on the debt. This is an optional field according to the
   * OFX spec.
   * @see "Section 13.9.2.4.4, OFX Spec"
   *
   * @param accruedInterest the amount of accrued interest
   */
    public void setAccruedInterest(Double accruedInterest) {
        this.accruedInterest = accruedInterest;
    }
}
