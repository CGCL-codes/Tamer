package org.ldaptive.provider.jndi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.ContextNotEmptyException;
import javax.naming.InvalidNameException;
import javax.naming.LimitExceededException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.naming.PartialResultException;
import javax.naming.ReferralException;
import javax.naming.ServiceUnavailableException;
import javax.naming.SizeLimitExceededException;
import javax.naming.TimeLimitExceededException;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.InvalidSearchFilterException;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SchemaViolationException;
import org.ldaptive.ResultCode;

/**
 * Utility class that provides a bridge between JNDI naming exceptions and ldap
 * result codes. See
 * http://docs.oracle.com/javase/tutorial/jndi/ldap/exceptions.html
 *
 * @author  Middleware Services
 * @version  $Revision: 2349 $ $Date: 2012-04-10 01:13:16 -0400 (Tue, 10 Apr 2012) $
 */
public final class NamingExceptionUtils {

    /** Map of naming exceptions to ldap result codes. */
    private static final Map<Class<? extends NamingException>, ResultCode[]> EXCEPTIONS_TO_RESULT_CODES;

    /** Map of ldap result codes to naming exceptions. */
    private static final Map<ResultCode, Class<? extends NamingException>> RESULT_CODES_TO_EXCEPTION;

    /**
   * initialize map of exceptions to result codes.
   */
    static {
        EXCEPTIONS_TO_RESULT_CODES = new HashMap<Class<? extends NamingException>, ResultCode[]>();
        EXCEPTIONS_TO_RESULT_CODES.put(NamingException.class, new ResultCode[] { ResultCode.OPERATIONS_ERROR, ResultCode.ALIAS_PROBLEM, ResultCode.ALIAS_DEREFERENCING_PROBLEM, ResultCode.LOOP_DETECT, ResultCode.AFFECTS_MULTIPLE_DSAS, ResultCode.OTHER });
        EXCEPTIONS_TO_RESULT_CODES.put(CommunicationException.class, new ResultCode[] { ResultCode.PROTOCOL_ERROR });
        EXCEPTIONS_TO_RESULT_CODES.put(TimeLimitExceededException.class, new ResultCode[] { ResultCode.TIME_LIMIT_EXCEEDED });
        EXCEPTIONS_TO_RESULT_CODES.put(SizeLimitExceededException.class, new ResultCode[] { ResultCode.SIZE_LIMIT_EXCEEDED });
        EXCEPTIONS_TO_RESULT_CODES.put(AuthenticationNotSupportedException.class, new ResultCode[] { ResultCode.AUTH_METHOD_NOT_SUPPORTED, ResultCode.STRONG_AUTH_REQUIRED, ResultCode.CONFIDENTIALITY_REQUIRED, ResultCode.INAPPROPRIATE_AUTHENTICATION });
        EXCEPTIONS_TO_RESULT_CODES.put(PartialResultException.class, new ResultCode[] { ResultCode.PARTIAL_RESULTS });
        EXCEPTIONS_TO_RESULT_CODES.put(ReferralException.class, new ResultCode[] { ResultCode.REFERRAL });
        EXCEPTIONS_TO_RESULT_CODES.put(LimitExceededException.class, new ResultCode[] { ResultCode.REFERRAL, ResultCode.ADMIN_LIMIT_EXCEEDED });
        EXCEPTIONS_TO_RESULT_CODES.put(OperationNotSupportedException.class, new ResultCode[] { ResultCode.UNAVAILABLE_CRITICAL_EXTENSION, ResultCode.UNWILLING_TO_PERFORM });
        EXCEPTIONS_TO_RESULT_CODES.put(NoSuchAttributeException.class, new ResultCode[] { ResultCode.NO_SUCH_ATTRIBUTE });
        EXCEPTIONS_TO_RESULT_CODES.put(InvalidAttributeIdentifierException.class, new ResultCode[] { ResultCode.UNDEFINED_ATTRIBUTE_TYPE });
        EXCEPTIONS_TO_RESULT_CODES.put(InvalidSearchFilterException.class, new ResultCode[] { ResultCode.INAPPROPRIATE_MATCHING });
        EXCEPTIONS_TO_RESULT_CODES.put(InvalidAttributeValueException.class, new ResultCode[] { ResultCode.CONSTRAINT_VIOLATION, ResultCode.INVALID_ATTRIBUTE_SYNTAX });
        EXCEPTIONS_TO_RESULT_CODES.put(AttributeInUseException.class, new ResultCode[] { ResultCode.ATTRIBUTE_OR_VALUE_EXISTS });
        EXCEPTIONS_TO_RESULT_CODES.put(NameNotFoundException.class, new ResultCode[] { ResultCode.NO_SUCH_OBJECT });
        EXCEPTIONS_TO_RESULT_CODES.put(InvalidNameException.class, new ResultCode[] { ResultCode.INVALID_DN_SYNTAX, ResultCode.NAMING_VIOLATION });
        EXCEPTIONS_TO_RESULT_CODES.put(AuthenticationException.class, new ResultCode[] { ResultCode.INVALID_CREDENTIALS });
        EXCEPTIONS_TO_RESULT_CODES.put(NoPermissionException.class, new ResultCode[] { ResultCode.INSUFFICIENT_ACCESS_RIGHTS });
        EXCEPTIONS_TO_RESULT_CODES.put(ServiceUnavailableException.class, new ResultCode[] { ResultCode.BUSY, ResultCode.UNAVAILABLE });
        EXCEPTIONS_TO_RESULT_CODES.put(SchemaViolationException.class, new ResultCode[] { ResultCode.OBJECT_CLASS_VIOLATION, ResultCode.NOT_ALLOWED_ON_RDN, ResultCode.OBJECT_CLASS_MODS_PROHIBITED });
        EXCEPTIONS_TO_RESULT_CODES.put(ContextNotEmptyException.class, new ResultCode[] { ResultCode.NOT_ALLOWED_ON_NONLEAF });
        EXCEPTIONS_TO_RESULT_CODES.put(NameAlreadyBoundException.class, new ResultCode[] { ResultCode.ENTRY_ALREADY_EXISTS });
    }

    /**
   * initialize map of result codes to exceptions.
   */
    static {
        RESULT_CODES_TO_EXCEPTION = new HashMap<ResultCode, Class<? extends NamingException>>();
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.SUCCESS, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.OPERATIONS_ERROR, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.PROTOCOL_ERROR, CommunicationException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.TIME_LIMIT_EXCEEDED, TimeLimitExceededException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.SIZE_LIMIT_EXCEEDED, SizeLimitExceededException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.COMPARE_FALSE, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.COMPARE_TRUE, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.AUTH_METHOD_NOT_SUPPORTED, AuthenticationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.STRONG_AUTH_REQUIRED, AuthenticationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.PARTIAL_RESULTS, PartialResultException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.REFERRAL, ReferralException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ADMIN_LIMIT_EXCEEDED, LimitExceededException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.UNAVAILABLE_CRITICAL_EXTENSION, OperationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.CONFIDENTIALITY_REQUIRED, AuthenticationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.SASL_BIND_IN_PROGRESS, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NO_SUCH_ATTRIBUTE, NoSuchAttributeException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.UNDEFINED_ATTRIBUTE_TYPE, InvalidAttributeIdentifierException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INAPPROPRIATE_MATCHING, InvalidSearchFilterException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.CONSTRAINT_VIOLATION, InvalidAttributeValueException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ATTRIBUTE_OR_VALUE_EXISTS, AttributeInUseException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INVALID_ATTRIBUTE_SYNTAX, InvalidAttributeValueException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NO_SUCH_OBJECT, NameNotFoundException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ALIAS_PROBLEM, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INVALID_DN_SYNTAX, InvalidNameException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.IS_LEAF, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ALIAS_DEREFERENCING_PROBLEM, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INAPPROPRIATE_AUTHENTICATION, AuthenticationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INVALID_CREDENTIALS, AuthenticationException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INSUFFICIENT_ACCESS_RIGHTS, NoPermissionException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.BUSY, ServiceUnavailableException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.UNAVAILABLE, ServiceUnavailableException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.UNWILLING_TO_PERFORM, OperationNotSupportedException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.LOOP_DETECT, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NAMING_VIOLATION, InvalidNameException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.OBJECT_CLASS_VIOLATION, SchemaViolationException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NOT_ALLOWED_ON_NONLEAF, ContextNotEmptyException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NOT_ALLOWED_ON_RDN, SchemaViolationException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ENTRY_ALREADY_EXISTS, NameAlreadyBoundException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.OBJECT_CLASS_MODS_PROHIBITED, SchemaViolationException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.AFFECTS_MULTIPLE_DSAS, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.OTHER, NamingException.class);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.SERVER_DOWN, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.LOCAL_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.ENCODING_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.DECODING_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.LDAP_TIMEOUT, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.AUTH_UNKNOWN, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.FILTER_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.USER_CANCELLED, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.PARAM_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NO_MEMORY, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.CONNECT_ERROR, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.LDAP_NOT_SUPPORTED, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.CONTROL_NOT_FOUND, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.NO_RESULTS_RETURNED, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.MORE_RESULTS_TO_RETURN, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.CLIENT_LOOP, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.REFERRAL_LIMIT_EXCEEDED, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.INVALID_RESPONSE, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.AMBIGUOUS_RESPONSE, null);
        RESULT_CODES_TO_EXCEPTION.put(ResultCode.TLS_NOT_SUPPORTED, null);
    }

    /** Default constructor. */
    private NamingExceptionUtils() {
    }

    /**
   * Returns the result codes that map to the supplied naming exception.
   *
   * @param  clazz  naming exception
   *
   * @return  ldap result codes
   */
    public static ResultCode[] getResultCodes(final Class<? extends NamingException> clazz) {
        Class<?> c = clazz;
        do {
            final ResultCode[] codes = EXCEPTIONS_TO_RESULT_CODES.get(c);
            if (codes != null) {
                return codes;
            }
            c = c.getSuperclass();
        } while (!c.equals(NamingException.class));
        return null;
    }

    /**
   * Returns the result code that map to the supplied naming exception. If the
   * exception maps to multiple result codes, the first one is returned.
   *
   * @param  clazz  naming exception
   *
   * @return  ldap result code
   */
    public static ResultCode getResultCode(final Class<? extends NamingException> clazz) {
        final ResultCode[] codes = getResultCodes(clazz);
        if (codes != null && codes.length > 0) {
            return codes[0];
        } else {
            return null;
        }
    }

    /**
   * Returns whether the supplied naming exception maps to the supplied result
   * code.
   *
   * @param  clazz  naming exception
   * @param  code  ldap result code
   *
   * @return  whether the naming exception matches the result code
   */
    public static boolean matches(final Class<? extends NamingException> clazz, final ResultCode code) {
        boolean match = false;
        final ResultCode[] matchingCodes = getResultCodes(clazz);
        if (matchingCodes != null) {
            for (ResultCode rc : matchingCodes) {
                if (rc == code) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

    /**
   * Returns the naming exception that maps to the supplied result code. If the
   * result code does not map to an exception, null is returned
   *
   * @param  code  ldap result code
   *
   * @return  array of naming exception classes
   */
    public static Class<? extends NamingException> getNamingException(final ResultCode code) {
        return RESULT_CODES_TO_EXCEPTION.get(code);
    }

    /**
   * Returns the naming exception that maps to the supplied result code. If the
   * result code does not map to an exception, null is returned
   *
   * @param  codes  ldap result codes
   *
   * @return  array of naming exception classes
   */
    public static Class<?>[] getNamingExceptions(final ResultCode[] codes) {
        final List<Class<?>> l = new ArrayList<Class<?>>();
        if (codes != null) {
            for (ResultCode rc : codes) {
                final Class<?> c = getNamingException(rc);
                if (c != null) {
                    l.add(c);
                }
            }
        }
        return l.toArray(new Class<?>[l.size()]);
    }
}
