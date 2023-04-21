package org.ldaptive.provider;

import org.ldaptive.LdapException;
import org.ldaptive.OperationException;
import org.ldaptive.ResultCode;
import org.ldaptive.control.ResponseControl;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 * @version  $Revision: 2362 $ $Date: 2012-04-26 13:33:29 -0400 (Thu, 26 Apr 2012) $
 */
public final class ProviderUtils {

    /** Default constructor. */
    private ProviderUtils() {
    }

    /**
   * Determines whether to throw operation exception or ldap exception. If
   * operation exception is thrown, the operation will be retried. Otherwise the
   * exception is propagated out.
   *
   * @param  operationRetryResultCodes  to compare result code against
   * @param  e  provider exception to wrap
   * @param  resultCode  provider result code
   * @param  matchedDn  response matched dn
   * @param  respControls  response controls
   * @param  referralUrls  response referral urls
   * @param  throwLdapException  throw an ldap exception if an operation
   * exception is not thrown
   *
   * @throws  OperationException  if the operation should be retried
   * @throws  LdapException  to propagate the exception out
   */
    public static void throwOperationException(final ResultCode[] operationRetryResultCodes, final Exception e, final int resultCode, final String matchedDn, final ResponseControl[] respControls, final String[] referralUrls, final boolean throwLdapException) throws LdapException {
        if (operationRetryResultCodes != null && operationRetryResultCodes.length > 0) {
            for (ResultCode rc : operationRetryResultCodes) {
                if (rc.value() == resultCode) {
                    throw new OperationException(e, rc, matchedDn, respControls, referralUrls);
                }
            }
        }
        if (throwLdapException) {
            throw new LdapException(e, ResultCode.valueOf(resultCode), matchedDn, respControls, referralUrls);
        }
    }

    /**
   * Determines whether to throw operation exception or ldap exception. If
   * operation exception is thrown, the operation will be retried. Otherwise the
   * exception is propagated out.
   *
   * @param  operationRetryResultCodes  to compare result code against
   * @param  msg  provider message
   * @param  resultCode  provider result code
   * @param  matchedDn  response matched dn
   * @param  respControls  response controls
   * @param  referralUrls  response referral urls
   * @param  throwLdapException  throw an ldap exception if an operation
   * exception is not thrown
   *
   * @throws  OperationException  if the operation should be retried
   * @throws  LdapException  to propagate the exception out
   */
    public static void throwOperationException(final ResultCode[] operationRetryResultCodes, final String msg, final int resultCode, final String matchedDn, final ResponseControl[] respControls, final String[] referralUrls, final boolean throwLdapException) throws LdapException {
        if (operationRetryResultCodes != null && operationRetryResultCodes.length > 0) {
            for (ResultCode rc : operationRetryResultCodes) {
                if (rc.value() == resultCode) {
                    throw new OperationException(msg, rc, matchedDn, respControls, referralUrls);
                }
            }
        }
        if (throwLdapException) {
            throw new LdapException(msg, ResultCode.valueOf(resultCode), matchedDn, respControls, referralUrls);
        }
    }
}
