package au.edu.diasb.emmet.test;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import au.edu.diasb.emmet.EmmetController;
import au.edu.diasb.emmet.model.EmmetAuthority;

class MockEmmetController extends EmmetController {

    private Authentication authentication;

    MockEmmetController(final String userName, final EmmetAuthority role) {
        authentication = makeAuthentication(userName, role);
    }

    @SuppressWarnings("serial")
    private Authentication makeAuthentication(final String userName, final EmmetAuthority role) {
        return userName == null ? null : new Authentication() {

            public String getName() {
                return userName;
            }

            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            public boolean isAuthenticated() {
                return role != null;
            }

            public Object getPrincipal() {
                return userName;
            }

            public Object getDetails() {
                return null;
            }

            public Object getCredentials() {
                return "N/A";
            }

            public Collection<GrantedAuthority> getAuthorities() {
                ArrayList<GrantedAuthority> res = new ArrayList<GrantedAuthority>();
                res.add(role);
                return res;
            }
        };
    }

    @Override
    protected Authentication getAuthentication() {
        return authentication;
    }

    public void updateAuthentication(String userName, EmmetAuthority role) {
        this.authentication = makeAuthentication(userName, role);
    }
}
