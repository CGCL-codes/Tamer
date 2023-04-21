package issrg.pba.rbac;

/**
 * This is a collection of periods that are applied together. This is an 
 * advanced implementation of intersection. Normally the 
 * IntersectionValidityPeriod produces the validity period that is just a set of
 * the latest notBefore and the earliest notAfter, which is good for 
 * constraining. However, Age and Min cannot be implemented this way, because
 * if the ValidityPeriod exceeds one of the margins, it is fully invalid, and
 * not just constrained.
 *
 * @author A.Otenko
 */
public class AdjustedPeriodCollection extends AdjustedValidityPeriod {

    private ValidityPeriod vp1;

    private ValidityPeriod vp2;

    protected AdjustedPeriodCollection() {
        super();
    }

    /**
   * This constructor builds a AdjustedPeriodCollection of two ValidityPeriods.
   * More ValidityPeriods can be added to the collection by chaining multiple
   * AdjustedPeriodCollection as a binary tree. Both ValidityPeriods are treated
   * equally.
   *
   * @param vp1 - one ValidityPeriod
   * @param vp2 - another ValidityPeriod
   */
    public AdjustedPeriodCollection(ValidityPeriod vp1, ValidityPeriod vp2) {
        super();
        this.vp1 = vp1;
        this.vp2 = vp2;
    }

    /**
   * This method returns an adjusted ValidityPeriod, where the collection of
   * ValidityPeriods provided at construction time is adjusted against the 
   * given ValidityPeriod.
   *
   * @param vp - the ValidityPeriod to adjust against
   */
    public ValidityPeriod adjust(ValidityPeriod vp) {
        if (vp1 != null) {
            if (vp1 instanceof AdjustedValidityPeriod) {
                vp = ((AdjustedValidityPeriod) vp1).adjust(vp);
            } else {
                vp = new IntersectionValidityPeriod(vp1, vp);
            }
        }
        if (vp2 != null) {
            if (vp2 instanceof AdjustedValidityPeriod) {
                vp = ((AdjustedValidityPeriod) vp2).adjust(vp);
            } else {
                vp = new IntersectionValidityPeriod(vp2, vp);
            }
        }
        return vp;
    }

    public String toString() {
        return "{rule " + vp1 + " && " + vp2 + "}";
    }
}
