package net.sourceforge.simpleworklog.client.gwt.presenter;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import net.sourceforge.simpleworklog.client.gwt.adapter.HasEnabledHandlers;
import net.sourceforge.simpleworklog.client.gwt.display.LoginDisplayMock;
import net.sourceforge.simpleworklog.client.gwt.event.LoginFailureEvent;
import net.sourceforge.simpleworklog.client.gwt.event.LoginSuccessEvent;
import net.sourceforge.simpleworklog.client.gwt.service.CommandExecutorServiceAsync;
import net.sourceforge.simpleworklog.client.mocks.MockClickable;
import net.sourceforge.simpleworklog.client.mocks.MockHasWidgets;
import net.sourceforge.simpleworklog.client.mocks.event.MockKeyDownEvent;
import net.sourceforge.simpleworklog.client.mocks.event.MockKeyUpEvent;
import net.sourceforge.simpleworklog.client.mocks.service.CommandExecutorServiceAsyncMock;
import net.sourceforge.simpleworklog.client.mocks.service.CommandWasteServiceAsyncMock;
import net.sourceforge.simpleworklog.shared.action.LoginResponse;
import net.sourceforge.simpleworklog.shared.entity.Role;
import net.sourceforge.simpleworklog.shared.entity.UserDetails;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author: Ignat Alexeyenko
 * Date: 14.01.2010
 */
public class LoginPresenterTest {

    private LoginPresenter loginPresenter;

    private LoginDisplayMock loginDisplay;

    private CommandExecutorServiceAsync service;

    private EventBus handlerManager;

    private MockLoginSuccessHandler loginSuccessHandler;

    private MockLoginFailureHandler loginFailureHandler;

    @Before
    public void init() {
        loginPresenter = new LoginPresenter();
        handlerManager = new SimpleEventBus();
        loginDisplay = new LoginDisplayMock();
        loginPresenter.setEventBus(handlerManager);
        loginPresenter.bindDisplay(loginDisplay);
        loginSuccessHandler = new MockLoginSuccessHandler();
        loginFailureHandler = new MockLoginFailureHandler();
        handlerManager.addHandler(LoginSuccessEvent.TYPE, loginSuccessHandler);
        handlerManager.addHandler(LoginFailureEvent.TYPE, loginFailureHandler);
    }

    @Test
    public void testSuccessfulLogin() {
        loginPresenter.go(new MockHasWidgets());
        Set<Role> roles = new HashSet<Role>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        service = new CommandExecutorServiceAsyncMock(new LoginResponse(new UserDetails(1L, "Bender", "Rodriguez", true, roles)));
        loginPresenter.setService(service);
        assertFalse(loginSuccessHandler.isCalled());
        assertFalse(loginFailureHandler.isCalled());
        loginDisplay.getClickableSubmitButton().click();
        assertTrue(loginSuccessHandler.isCalled());
        assertFalse(loginFailureHandler.isCalled());
    }

    @Test
    public void testAsyncRequestFailed() {
        loginPresenter.go(new MockHasWidgets());
        service = new CommandWasteServiceAsyncMock(new IllegalStateException("Test Error"));
        loginPresenter.setService(service);
        assertFalse(loginSuccessHandler.isCalled());
        assertFalse(loginFailureHandler.isCalled());
        loginDisplay.getClickableSubmitButton().click();
        assertFalse(loginSuccessHandler.isCalled());
        assertTrue(loginFailureHandler.isCalled());
    }

    @Test
    public void testTypingInLoginAndInPasswordWillEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a", true);
        loginDisplay.getPassword().setValue("b", true);
        assertTrue(submitButton.isEnabled());
    }

    @Test
    public void testTypingInLoginOnlyWontEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a", true);
        assertFalse(submitButton.isEnabled());
    }

    @Test
    public void testTypingInPasswordOnlyWontEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getPassword().setValue("b", true);
        assertFalse(submitButton.isEnabled());
    }

    @Test
    public void testKeyUpOnLoginEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a");
        loginDisplay.getPassword().setValue("b");
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLoginTextboxAsKeyUp().fireEvent(new MockKeyUpEvent(42));
        assertTrue(submitButton.isEnabled());
    }

    @Test
    public void testKeyUpOnPasswordEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a");
        loginDisplay.getPassword().setValue("b");
        assertFalse(submitButton.isEnabled());
        loginDisplay.getPasswordTextboxAsKeyUp().fireEvent(new MockKeyUpEvent(42));
        assertTrue(submitButton.isEnabled());
    }

    @Test
    public void testKeyDownOnLoginEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        service = new CommandWasteServiceAsyncMock(new IllegalStateException("Test Error"));
        loginPresenter.setService(service);
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        MockClickable submitClickable = loginDisplay.getClickableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a");
        loginDisplay.getPassword().setValue("b");
        assertFalse(submitClickable.isClicked());
        loginDisplay.getLoginTextboxAsKeyDown().fireEvent(new MockKeyDownEvent(42));
        assertFalse(submitClickable.isClicked());
        loginDisplay.getLoginTextboxAsKeyDown().fireEvent(new MockKeyDownEvent(KeyCodes.KEY_ENTER));
        assertTrue(submitClickable.isClicked());
    }

    @Test
    public void testKeyDownOnPasswordEnableSubmitButton() {
        loginPresenter.go(new MockHasWidgets());
        service = new CommandWasteServiceAsyncMock(new IllegalStateException("Test Error"));
        loginPresenter.setService(service);
        HasEnabledHandlers submitButton = loginDisplay.getEnableableSubmitButton();
        MockClickable submitClickable = loginDisplay.getClickableSubmitButton();
        assertFalse(submitButton.isEnabled());
        loginDisplay.getLogin().setValue("a");
        loginDisplay.getPassword().setValue("b");
        assertFalse(submitClickable.isClicked());
        loginDisplay.getPasswordTextboxAsKeyDown().fireEvent(new MockKeyDownEvent(42));
        assertFalse(submitClickable.isClicked());
        loginDisplay.getPasswordTextboxAsKeyDown().fireEvent(new MockKeyDownEvent(KeyCodes.KEY_ENTER));
        assertTrue(submitClickable.isClicked());
    }
}
