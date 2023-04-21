package icamodel.bits;

import icamodel.framework.BusinessLine;
import icamodel.framework.ModelBit;
import icamodel.framework.ResultName;
import icamodel.utils.Doer;
import icamodel.utils.ModelParameter;

/**
 * Represents the liabilies of the enterprise we are modelling.
 * @author Louise
 */
public class Liabilities extends ModelBit {

    private static Liabilities theLiabilities;

    /** Creates a new instance of Liabilities */
    private Liabilities() {
        super("Liabilities");
    }

    /**
     * Get the only permitted instance of Liabilities
     * @return the only permitted instance of  <code>Liabilities</code>
     */
    public static synchronized Liabilities getInstance() {
        if (theLiabilities == null) {
            theLiabilities = new Liabilities();
        }
        return theLiabilities;
    }

    /**
     * Do the right things at the start of a realisation,
     * before the children are reset
     * @param rnum The realisation number
     */
    protected void startReset(int rnum) {
    }

    /**
     * Do the right things at the start of a realisation, after the children
     * have been reset
     * @param rnum the realisation number
     */
    protected void endReset(int rnum) {
    }

    /**
     * do the right things for a time period, before the children
     * are updated
     * @param rnum realisation number
     * @param tnum time period
     */
    protected void startUpdate(int rnum, int tnum) {
    }

    /**
     * do the right things for a time period after the children have been updated.
     * @param rnum realisation
     * @param tnum time period
     */
    protected void endUpdate(int rnum, int tnum) {
        Doer filter = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof BusinessLine;
            }
        };
        double total;
        total = totalFromChildren(filter, ResultName.RISK_EXPOSED, tnum);
        setResult(ResultName.RISK_EXPOSED, tnum, total);
        total = totalFromChildren(filter, ResultName.PREM_RECEIVED, tnum);
        setResult(ResultName.PREM_RECEIVED, tnum, total);
        total = totalFromChildren(filter, ResultName.EXPENSES, tnum);
        setResult(ResultName.EXPENSES, tnum, total);
        total = totalFromChildren(filter, ResultName.LOSS_INCURRED, tnum);
        setResult(ResultName.LOSS_INCURRED, tnum, total);
        total = totalFromChildren(filter, ResultName.CLAIMS_PAID, tnum);
        setResult(ResultName.CLAIMS_PAID, tnum, total);
        total = totalFromChildren(filter, ResultName.LOSS_RESERVE, tnum);
        setResult(ResultName.LOSS_RESERVE, tnum, total);
        total = totalFromChildren(filter, ResultName.EXPENSES, tnum);
        setResult(ResultName.EXPENSES, tnum, total);
        total = totalFromChildren(filter, ResultName.NET_CASH_FLOW, tnum);
        setResult(ResultName.NET_CASH_FLOW, tnum, total);
    }

    protected String checkParameterValue(ModelParameter param) {
        return null;
    }

    public void clearSpecial() {
    }
}
