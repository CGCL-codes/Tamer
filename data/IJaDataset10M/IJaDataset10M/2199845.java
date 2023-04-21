package org.bsplus.util.security;

import junit.framework.TestCase;
import org.bsplus.domain.Role;
import org.bsplus.domain.User;
import org.bsplus.service.UserService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * TestCase for UserDetailsServiceImpl
 */
public class UserDetailsServiceImplTest extends TestCase {

    private Mockery context = new Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    public void testLoadUserByUsernameExists() {
        final UserService userService = context.mock(UserService.class);
        UserDetailsServiceImpl userDetailsServiceImpl = new UserDetailsServiceImpl();
        userDetailsServiceImpl.setUserService(userService);
        final User user = new User();
        user.setUsername("fred");
        user.setPassword("sercet");
        user.setRole(Role.USER);
        context.checking(new Expectations() {

            {
                oneOf(userService).getUser(user.getUsername());
                will(returnValue(user));
            }
        });
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUsername());
        context.assertIsSatisfied();
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(user.getRole().getDescription(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    public void testLoadUserByUsernameDoesntExist() {
        final UserService userService = context.mock(UserService.class);
        UserDetailsServiceImpl userDetailsServiceImpl = new UserDetailsServiceImpl();
        userDetailsServiceImpl.setUserService(userService);
        final String username = "fred";
        context.checking(new Expectations() {

            {
                oneOf(userService).getUser(username);
                will(returnValue(null));
            }
        });
        try {
            userDetailsServiceImpl.loadUserByUsername(username);
            fail();
        } catch (UsernameNotFoundException e) {
            assertNotNull(e.getMessage());
            context.assertIsSatisfied();
        }
    }
}
