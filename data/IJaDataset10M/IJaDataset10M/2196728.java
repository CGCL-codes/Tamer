package org.ldaptive.auth.ext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.AuthenticationResponseHandler;

/**
 * Attempts to parse the authentication response and set the account state using
 * data associated with eDirectory. The {@link
 * org.ldaptive.auth.Authenticator} should be configured to return
 * 'passwordExpirationTime' and 'loginGraceRemaining' attributes so they can be
 * consumed by this handler.
 *
 * @author  Middleware Services
 * @version  $Revision: 2198 $ $Date: 2012-01-04 16:02:09 -0500 (Wed, 04 Jan 2012) $
 */
public class EDirectoryAuthenticationResponseHandler implements AuthenticationResponseHandler {

    /** {@inheritDoc} */
    @Override
    public void process(final AuthenticationResponse response) {
        final LdapEntry entry = response.getLdapEntry();
        final LdapAttribute expTime = entry.getAttribute("passwordExpirationTime");
        final LdapAttribute loginRemaining = entry.getAttribute("loginGraceRemaining");
        Calendar exp = null;
        if (expTime != null) {
            exp = Calendar.getInstance();
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                exp.setTime(formatter.parse(expTime.getStringValue()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Expiration time format error", e);
            }
        }
        if (exp != null || loginRemaining != null) {
            response.setAccountState(new EDirectoryAccountState(exp, loginRemaining != null ? Integer.parseInt(loginRemaining.getStringValue()) : 0));
        }
        if (response.getAccountState() == null && response.getMessage() != null) {
            final EDirectoryAccountState.Error edError = EDirectoryAccountState.Error.parse(response.getMessage());
            if (edError != null) {
                response.setAccountState(new EDirectoryAccountState(edError));
            }
        }
    }
}
