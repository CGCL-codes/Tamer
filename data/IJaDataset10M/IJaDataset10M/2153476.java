package com.acv.dao.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.log4j.Logger;
import com.acv.dao.common.BaseDaoTestCase;
import com.acv.dao.profiles.model.UserProfile;

public class UserDetailsServiceDaoTest extends BaseDaoTestCase {

    private UserDetailsServiceDao userDetailsServiceDao = null;

    private static final Logger log = Logger.getLogger(UserDetailsServiceDaoTest.class);

    private static String primaryKeyUid = "tomcat@cmtek.com";

    /**
	 * a methode for populate ldap server with the same thing that the
	 * sampleData.sql
	 */
    public void testASetupLdap() throws Exception {
        log.debug("Begin to populate ldap user");
        String password = "tomcat";
        String uid = primaryKeyUid;
        GrantedAuthority grantedAuthorityUser = new GrantedAuthorityImpl("Logged_in");
        GrantedAuthority[] grantedAuthorities = { grantedAuthorityUser };
    }

    public void testLoadUserDetails() {
        UserDetails userDetails = userDetailsServiceDao.loadUserByUsername(primaryKeyUid);
        assertNotNull(userDetails);
        assertTrue(userDetails.isEnabled());
        assertNotNull(userDetails.getPassword());
        assertEquals(userDetails.getUsername(), primaryKeyUid);
        assertTrue(userDetails.getAuthorities().length > 0);
        UserProfile user = ((UserDetailsImplementation) userDetails).getCurrentUserProfile();
        assertNotNull(user.getLastName());
        assertEquals(user.getEmail(), primaryKeyUid);
    }

    public void setUserDetailsServiceDao(UserDetailsServiceDao userDetailsServiceDao) {
        this.userDetailsServiceDao = userDetailsServiceDao;
    }

    public void testEquals() {
        log.info("testEquals");
        UserDetails c1 = userDetailsServiceDao.loadUserByUsername("a@dmin.com");
        UserDetails c2 = userDetailsServiceDao.loadUserByUsername("tomcat@cmtek.com");
        assertTrue(!c1.equals(null));
        assertEquals(c1, c1);
        assertTrue(!c1.equals(c2));
    }

    public void testHashCode() {
        log.info("testHashCode");
        UserDetails c1 = userDetailsServiceDao.loadUserByUsername("a@dmin.com");
        UserDetails c2 = userDetailsServiceDao.loadUserByUsername("tomcat@cmtek.com");
        assertTrue(c1.equals(c1) && c1.hashCode() == c1.hashCode());
        assertTrue(!c1.equals(c2) && c1.hashCode() != c2.hashCode());
        assertTrue(c2.equals(c2) && c2.hashCode() == c2.hashCode());
    }

    public void testToString() {
        log.info("testToString");
        UserDetails userDetails = userDetailsServiceDao.loadUserByUsername("a@dmin.com");
        try {
            log.info("<<" + userDetails.toString() + ">>");
        } catch (Exception e) {
            fail("ToString test failed with the following reason: " + e.getMessage());
        }
    }
}
