package org.acegisecurity.userdetails.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import junit.framework.TestCase;
import org.acegisecurity.PopulatedDatabase;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Tests {@link JdbcDaoImpl}.
 *
 * @author Ben Alex
 * @version $Id: JdbcDaoTests.java,v 1.3 2005/12/24 10:03:18 benalex Exp $
 */
public class JdbcDaoTests extends TestCase {

    public JdbcDaoTests() {
        super();
    }

    public JdbcDaoTests(String arg0) {
        super(arg0);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(JdbcDaoTests.class);
    }

    public void testCheckDaoAccessUserSuccess() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        UserDetails user = dao.loadUserByUsername("marissa");
        assertEquals("marissa", user.getUsername());
        assertEquals("koala", user.getPassword());
        assertTrue(user.isEnabled());
        HashSet authorities = new HashSet(2);
        authorities.add(user.getAuthorities()[0].getAuthority());
        authorities.add(user.getAuthorities()[1].getAuthority());
        assertTrue(authorities.contains("ROLE_TELLER"));
        assertTrue(authorities.contains("ROLE_SUPERVISOR"));
    }

    public void testCheckDaoOnlyReturnsGrantedAuthoritiesGrantedToUser() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        UserDetails user = dao.loadUserByUsername("scott");
        assertEquals("ROLE_TELLER", user.getAuthorities()[0].getAuthority());
        assertEquals(1, user.getAuthorities().length);
    }

    public void testCheckDaoReturnsCorrectDisabledProperty() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        UserDetails user = dao.loadUserByUsername("peter");
        assertTrue(!user.isEnabled());
    }

    public void testGettersSetters() {
        JdbcDaoImpl dao = new JdbcDaoImpl();
        dao.setAuthoritiesByUsernameQuery("SELECT * FROM FOO");
        assertEquals("SELECT * FROM FOO", dao.getAuthoritiesByUsernameQuery());
        dao.setUsersByUsernameQuery("SELECT USERS FROM FOO");
        assertEquals("SELECT USERS FROM FOO", dao.getUsersByUsernameQuery());
    }

    public void testLookupFailsIfUserHasNoGrantedAuthorities() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        try {
            dao.loadUserByUsername("cooper");
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException expected) {
            assertEquals("User has no GrantedAuthority", expected.getMessage());
        }
    }

    public void testLookupFailsWithWrongUsername() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        try {
            dao.loadUserByUsername("UNKNOWN_USER");
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException expected) {
            assertTrue(true);
        }
    }

    public void testLookupSuccessWithMixedCase() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDao();
        assertEquals("koala", dao.loadUserByUsername("MaRiSSA").getPassword());
        assertEquals("wombat", dao.loadUserByUsername("ScOTt").getPassword());
    }

    public void testRolePrefixWorks() throws Exception {
        JdbcDaoImpl dao = makePopulatedJdbcDaoWithRolePrefix();
        assertEquals("ARBITRARY_PREFIX_", dao.getRolePrefix());
        UserDetails user = dao.loadUserByUsername("marissa");
        assertEquals("marissa", user.getUsername());
        assertEquals(2, user.getAuthorities().length);
        HashSet authorities = new HashSet(2);
        authorities.add(user.getAuthorities()[0].getAuthority());
        authorities.add(user.getAuthorities()[1].getAuthority());
        assertTrue(authorities.contains("ARBITRARY_PREFIX_ROLE_TELLER"));
        assertTrue(authorities.contains("ARBITRARY_PREFIX_ROLE_SUPERVISOR"));
    }

    public void testStartupFailsIfDataSourceNotSet() throws Exception {
        JdbcDaoImpl dao = new JdbcDaoImpl();
        try {
            dao.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testStartupFailsIfUserMapSetToNull() throws Exception {
        JdbcDaoImpl dao = new JdbcDaoImpl();
        try {
            dao.setDataSource(null);
            dao.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    private JdbcDaoImpl makePopulatedJdbcDao() throws Exception {
        JdbcDaoImpl dao = new JdbcDaoImpl();
        dao.setDataSource(PopulatedDatabase.getDataSource());
        dao.afterPropertiesSet();
        return dao;
    }

    private JdbcDaoImpl makePopulatedJdbcDaoWithRolePrefix() throws Exception {
        JdbcDaoImpl dao = new JdbcDaoImpl();
        dao.setDataSource(PopulatedDatabase.getDataSource());
        dao.setRolePrefix("ARBITRARY_PREFIX_");
        dao.afterPropertiesSet();
        return dao;
    }

    private class MockMappingSqlQuery extends MappingSqlQuery {

        protected Object mapRow(ResultSet arg0, int arg1) throws SQLException {
            return null;
        }
    }
}
