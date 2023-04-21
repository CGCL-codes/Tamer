package org.openuss.security;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.openuss.security.acegi.UserDetailsServiceAdapter;
import org.openuss.security.acegi.UserInfoDetailsAdapter;

/**
 * @author Ingo Dueppe
 */
public class UserDetailsServiceAdapterTest extends TestCase {

    /**
	 * Test wether or not the load by username wraps the user object correctly.
	 */
    public void testLoadUserByUsername() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("username");
        userInfo.setId(4711L);
        userInfo.setEmail("user@openuss.de");
        UserInfoDetailsAdapter userInfoDetailsAdapter = new UserInfoDetailsAdapter(userInfo, null);
        SecurityService service = createMock(SecurityService.class);
        UserDetailsServiceAdapter adapter = new UserDetailsServiceAdapter();
        adapter.setSecurityService(service);
        expect(service.getUserByName("username")).andReturn(userInfo);
        expect(service.getGrantedAuthorities(userInfo)).andReturn(null);
        expect(service.getUserByName("user@openuss.de")).andReturn(null);
        expect(service.getUserByEmail("user@openuss.de")).andReturn(userInfo);
        expect(service.getGrantedAuthorities(userInfo)).andReturn(null);
        expect(service.getUserByName("usernotfound")).andReturn(null);
        expect(service.getUserByEmail("usernotfound")).andReturn(null);
        replay(service);
        UserDetails userDetails = (UserDetails) adapter.loadUserByUsername("username");
        assertEquals("username", userDetails.getUsername());
        assertEquals(userInfoDetailsAdapter, userDetails);
        UserDetails userDetails2 = (UserDetails) adapter.loadUserByUsername("user@openuss.de");
        assertEquals("username", userDetails2.getUsername());
        assertEquals(userInfoDetailsAdapter, userDetails2);
        try {
            adapter.loadUserByUsername("usernotfound");
            fail();
        } catch (UsernameNotFoundException ex) {
        }
        verify(service);
    }

    public void testSecurityServiceState() {
        try {
            UserDetailsServiceAdapter adapter = new UserDetailsServiceAdapter();
            adapter.loadUserByUsername("username");
        } catch (IllegalStateException ex) {
        }
    }

    /**
	 * Tests wether or not the asccessors of SecurityService work correctly.
	 */
    public void testSecurityServiceSetterAndGetter() {
        SecurityService service = createMock(SecurityService.class);
        UserDetailsServiceAdapter adapter = new UserDetailsServiceAdapter();
        adapter.setSecurityService(service);
        assertEquals(service, adapter.getSecurityService());
    }
}
