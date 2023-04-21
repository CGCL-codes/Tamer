package es.seat131.viewerfree.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import es.seat131.viewerfree.command.admin.CreateUserCommand;
import es.seat131.viewerfree.common.ActionForward;
import es.seat131.viewerfree.common.ParamKey;
import es.seat131.viewerfree.config.Messages;
import es.seat131.viewerfree.dto.UserDto;
import es.seat131.viewerfree.dto.UserProfile;
import es.seat131.viewerfree.service.IUserService;
import es.seat131.viewerfree.service.ServiceException;

public class CreateUserCommandTest {

    private static final String PASSWORD = "PASSWORD";

    private static final String USER = "USER";

    private CreateUserCommand _createUserCommand;

    private IUserService _userService;

    private MockHttpServletRequest _httpServletRequestMock;

    private MockHttpServletResponse _httpServletResponseMock;

    @Before
    public void setUp() throws Exception {
        _userService = Mockito.mock(IUserService.class);
        _httpServletRequestMock = new MockHttpServletRequest();
        _httpServletResponseMock = new MockHttpServletResponse();
        _createUserCommand = new CreateUserCommand();
        _createUserCommand.setUserService(_userService);
        _httpServletRequestMock.getSession().setAttribute(ParamKey.LANGUAGE.toString(), Messages.LOCALE_ES.getLanguage());
    }

    @After
    public void tearDown() throws Exception {
        _createUserCommand = null;
        _httpServletResponseMock = null;
        _httpServletRequestMock = null;
        _userService = null;
    }

    @Test
    public void testExecute() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), USER);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.PROFILE.toString(), UserProfile.NORMAL.toString());
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        UserDto userDto = new UserDto(USER, PASSWORD);
        userDto.setProfile(UserProfile.NORMAL);
        Mockito.verify(_userService).createUser(userDto);
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "user.created.label"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecuteValidatePassword() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), USER);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD + "Different");
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        verifyUserNotCreated();
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "confirm.password.error"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecuteUserNull() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), (String) null);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        verifyUserNotCreated();
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "user.is.mandatory"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecuteUserEmpty() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), "");
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        verifyUserNotCreated();
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "user.is.mandatory"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecutePasswordNull() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), USER);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), (String) null);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        verifyUserNotCreated();
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "password.is.mandatory"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecutePasswordEmpty() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), USER);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), "");
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        assertEquals(ActionForward.CREATE_USER, _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock));
        verifyUserNotCreated();
        assertEquals(Messages.getString(Messages.LOCALE_ES.getLanguage(), "password.is.mandatory"), _httpServletRequestMock.getAttribute(ParamKey.MESSAGE.toString()));
    }

    @Test
    public void testExecuteException() throws Exception {
        _httpServletRequestMock.setParameter(ParamKey.USER.toString(), USER);
        _httpServletRequestMock.setParameter(ParamKey.PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.CONFIRM_PASSWORD.toString(), PASSWORD);
        _httpServletRequestMock.setParameter(ParamKey.PROFILE.toString(), UserProfile.NORMAL.toString());
        try {
            UserDto userDto = new UserDto(USER, PASSWORD);
            userDto.setProfile(UserProfile.NORMAL);
            Mockito.doThrow(new ServiceException()).when(_userService).createUser(userDto);
            _createUserCommand.execute(_httpServletRequestMock, _httpServletResponseMock);
            fail();
        } catch (CommandException e) {
            assertEquals("Error trying to create a new user", e.getMessage());
        }
    }

    private void verifyUserNotCreated() throws ServiceException {
        Mockito.verify(_userService, Mockito.times(0)).createUser(new UserDto(USER, PASSWORD));
    }
}
