package org.openymsg.context.session;

import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.openymsg.YahooStatus;
import org.openymsg.execute.Executor;
import org.openymsg.execute.write.Message;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SessionSessionImplTest {

    String username = "testuser";

    private Executor executor;

    private SessionSessionCallback callback;

    @BeforeMethod
    public void beforeMethod() {
        executor = Mockito.mock(Executor.class);
        callback = Mockito.mock(SessionSessionCallback.class);
    }

    @Test
    public void testSetStatus() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        YahooStatus status = YahooStatus.AVAILABLE;
        session.setStatus(status);
        Mockito.verify(executor).execute(argThat(new StatusChangeRequest(status)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Cannot set custom state without message")
    public void testSetStatusCustomFail() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        YahooStatus status = YahooStatus.CUSTOM;
        session.setStatus(status);
        Mockito.verify(executor).execute(argThat(new StatusChangeRequest(status)));
    }

    @Test
    public void testSetCustom() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        String message = "myMessage";
        boolean showBusyIcon = false;
        session.setCustomStatus(message, showBusyIcon);
        Mockito.verify(executor).execute(argThat(new StatusChangeRequest(YahooStatus.CUSTOM, message)));
    }

    @Test
    public void testLogout() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        session.loginComplete();
        session.logout();
        Mockito.verify(executor).execute(argThat(new LogoutMessage(username)));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "State is not logging in: LOGGING_IN")
    public void testLogoutFailedLogin() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        session.logout();
    }

    @Test
    public void testLogoutForced() {
        SessionSessionImpl session = new SessionSessionImpl(username, executor, callback);
        LogoutReason reason = LogoutReason.DUPLICATE_LOGIN1;
        session.receivedLogout(reason);
        Mockito.verify(callback).logoffForced(reason);
    }

    private Message argThat(Message message, String... excludeFields) {
        return (Message) Mockito.argThat(new ReflectionEquals(message, excludeFields));
    }
}
