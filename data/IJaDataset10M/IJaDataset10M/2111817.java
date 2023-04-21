package net.sf.jguard.core.enforcement;

import javax.inject.Inject;
import net.sf.jguard.core.authentication.Guest;
import net.sf.jguard.core.authentication.filters.AuthenticationFilter;
import net.sf.jguard.core.authorization.filters.AuthorizationFilter;
import net.sf.jguard.core.lifecycle.MockRequest;
import net.sf.jguard.core.lifecycle.MockResponse;
import java.util.List;

/**
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
public class MockGuestPolicyEnforcementPointFilter extends GuestPolicyEnforcementPointFilter<MockRequest, MockResponse> {

    @Inject
    public MockGuestPolicyEnforcementPointFilter(@Guest List<AuthenticationFilter<MockRequest, MockResponse>> guestAuthenticationFilters, @Guest List<AuthorizationFilter<MockRequest, MockResponse>> guestAuthorizationFilters, List<AuthenticationFilter<MockRequest, MockResponse>> restfulAuthenticationFilters, List<AuthorizationFilter<MockRequest, MockResponse>> restfulAuthorizationFilters) {
        super(guestAuthenticationFilters, guestAuthorizationFilters, restfulAuthenticationFilters, restfulAuthorizationFilters);
    }
}
