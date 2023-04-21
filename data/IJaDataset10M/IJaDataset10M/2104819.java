package ru.megadevelopers.xboard.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationCredentialsNotFoundException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import ru.megadevelopers.xboard.core.User;
import ru.megadevelopers.xboard.util.UserUtils;

public class UserUtilsTest {

    @Test
    public void testGetCurrentUser() {
        User user = new User();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertEquals(user, UserUtils.getCurrentUser());
        assertEquals(user, UserUtils.getRequiredCurrentUser());
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void testGetRequiredCurrentUser() {
        SecurityContextHolder.clearContext();
        UserUtils.getRequiredCurrentUser();
    }
}
