package net.sf.jguard.core.authentication.filters;

import javax.inject.Inject;
import net.sf.jguard.core.lifecycle.MockRequest;
import net.sf.jguard.core.lifecycle.MockResponse;

public class MockAuthenticationFiltersProvider extends AuthenticationFiltersProvider<MockRequest, MockResponse> {

    @Inject
    public MockAuthenticationFiltersProvider(AuthenticationChallengeFilter<MockRequest, MockResponse> mockRequestMockResponseAuthenticationChallengeFilter) {
        super(mockRequestMockResponseAuthenticationChallengeFilter);
    }
}
