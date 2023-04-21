package com.raimcomputing.pickforme.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import com.raimcomputing.pickforme.common.exception.EmailAddressInUseException;
import com.raimcomputing.pickforme.common.exception.InvalidPasswordException;
import com.raimcomputing.pickforme.common.exception.NoSuchUserException;
import com.raimcomputing.pickforme.common.utility.VoFactory;
import com.raimcomputing.pickforme.common.vo.UserVo;
import com.raimcomputing.pickforme.domain.dao.UserDao;
import com.raimcomputing.pickforme.service.impl.UserImpl;
import com.raimcomputing.pickforme.service.utility.PasswordUtility;

public class UserTest {

    private UserImpl user;

    private UserDao mockUserDao;

    private PasswordUtility mockPasswordUtility;

    @Before
    public void setUp() {
        user = new UserImpl();
        mockUserDao = createMock(UserDao.class);
        mockPasswordUtility = createMock(PasswordUtility.class);
        user.setUserDao(mockUserDao);
        user.setPasswordUtility(mockPasswordUtility);
    }

    @Test
    public void testCreateValidUser() throws Exception {
        UserVo testUser = (UserVo) VoFactory.getVo(UserVo.class);
        UserVo savedUser = (UserVo) VoFactory.getVo(UserVo.class);
        String testEmail = "test@email.com";
        String testPassword = "test-password";
        String salt = "IG^FYV";
        byte[] digest = new byte[] { 1, 2, 3, 4, 5, 6, 7 };
        testUser.setEmail(testEmail);
        savedUser.setEmail(testEmail);
        savedUser.setId(new Long(10));
        savedUser.setSalt(salt);
        savedUser.setDigest(digest);
        expect(mockUserDao.loadUserByEmail(testEmail)).andThrow(new NoSuchUserException());
        expect(mockPasswordUtility.generateSalt()).andReturn(salt);
        expect(mockPasswordUtility.createNewPassword(testPassword, salt)).andReturn(digest);
        mockUserDao.save(testUser);
        expect(mockUserDao.loadUserByEmail(testEmail)).andReturn(savedUser);
        replay(mockUserDao);
        replay(mockPasswordUtility);
        UserVo validated = user.createUser(testUser, testPassword);
        assertNotNull(validated);
        assertNotNull(validated.getId());
        assertEquals(testEmail, validated.getEmail());
        assertEquals(salt, validated.getSalt());
        assertEquals(digest, validated.getDigest());
        verify(mockUserDao);
        verify(mockPasswordUtility);
    }

    @Test
    public void testCreateUserWithExistingEmail() throws Exception {
        UserVo testUser = (UserVo) VoFactory.getVo(UserVo.class);
        String testEmail = "test@email.com";
        testUser.setEmail(testEmail);
        expect(mockUserDao.loadUserByEmail(testEmail)).andReturn(testUser);
        replay(mockUserDao);
        replay(mockPasswordUtility);
        try {
            user.createUser(testUser, "password");
            fail("Expected exception not thrown");
        } catch (EmailAddressInUseException e) {
            assertNotNull(e);
        }
        verify(mockUserDao);
        verify(mockPasswordUtility);
    }

    @Test
    @SuppressWarnings("all")
    public void testValidateBadUser() throws Exception {
        String testEmail = "test@eamil.com";
        String testPassword = "password";
        expect(mockUserDao.loadUserByEmail(testEmail)).andThrow(new NoSuchUserException());
        replay(mockUserDao);
        replay(mockPasswordUtility);
        try {
            UserVo result = user.validateUser(testEmail, testPassword);
            fail("Expected expception not thrown");
        } catch (NoSuchUserException e) {
            assertNotNull(e);
        }
        verify(mockUserDao);
        verify(mockPasswordUtility);
    }

    @Test
    public void testValidateUserWithCorrectPassword() throws Exception {
        UserVo validUser = (UserVo) VoFactory.getVo(UserVo.class);
        String testEmail = "test@eamil.com";
        String testPassword = "password";
        String salt = "IG^FYV";
        byte[] digest = new byte[] { 1, 2, 3, 4, 5, 6, 7 };
        validUser.setId(new Long(-1));
        validUser.setDigest(digest);
        validUser.setSalt(salt);
        validUser.setEmail(testEmail);
        expect(mockUserDao.loadUserByEmail(testEmail)).andReturn(validUser);
        expect(mockPasswordUtility.verifyPassphrase(validUser, testPassword)).andReturn(true);
        replay(mockUserDao);
        replay(mockPasswordUtility);
        UserVo result = user.validateUser(testEmail, testPassword);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(digest, result.getDigest());
        assertEquals(salt, result.getSalt());
        assertEquals(testEmail, validUser.getEmail());
        verify(mockUserDao);
        verify(mockPasswordUtility);
    }

    @Test
    @SuppressWarnings("all")
    public void testValidateUserWithIncorrectPassword() throws Exception {
        UserVo validUser = (UserVo) VoFactory.getVo(UserVo.class);
        String testEmail = "test@eamil.com";
        String testPassword = "password";
        String salt = "IG^FYV";
        byte[] digest = new byte[] { 1, 2, 3, 4, 5, 6, 7 };
        validUser.setId(new Long(-1));
        validUser.setDigest(digest);
        validUser.setSalt(salt);
        validUser.setEmail(testEmail);
        expect(mockUserDao.loadUserByEmail(testEmail)).andReturn(validUser);
        expect(mockPasswordUtility.verifyPassphrase(validUser, testPassword)).andReturn(false);
        replay(mockUserDao);
        replay(mockPasswordUtility);
        try {
            UserVo result = user.validateUser(testEmail, testPassword);
            fail("Expected expception not thrown");
        } catch (InvalidPasswordException e) {
            assertNotNull(e);
        }
        verify(mockUserDao);
        verify(mockPasswordUtility);
    }
}
