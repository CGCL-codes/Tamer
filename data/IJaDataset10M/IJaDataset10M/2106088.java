package issrg.pba.rbac;

/**
 * This interface defines behaviour of a Validity Period used by 
 * ExpirableCredentials.
 * If notBefore Date is later than notAfter, then the credential is invalidated.
 *
 * @author A.Otenko
 *
 * @see ExpirableCredentials
 */
public interface ValidityPeriod extends issrg.pba.Credentials {

    /**
   * This method returns the Date corresponding to the start of the validity 
   * period.
   * null means infinity.
   *
   * @return the Date corresponding to the start of the validity period, or 
   *    null, if
   *    infinitely valid in the past
   */
    public java.util.Date getNotBefore();

    /**
   * This method returns the Date corresponding to the end of the validity 
   * period.
   * null means eternity.
   *
   * @return the Date corresponding to the end of the validity period, or null, 
   *    if
   *    infinitely valid in the future
   */
    public java.util.Date getNotAfter();
}
