package infrastructure.configuration;

import static org.junit.Assert.fail;
import infrastructure.exceptions.BaseServerRuntimeException;
import infrastructure.exceptions.ServerFataError;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import remote.ConnectionManager;
import remote.RemoteInvocation;
import core.ServerFacade;
import dal.User;

public class ConnectionTest {

    public static class TestReadClass {

        public void testReadMethod() {
        }

        public void testReadMethod2(Integer a) {
        }
    }

    @Before
    public void doBefore() {
        try {
            ConnectionManager.getInstance().startManager();
            Thread.sleep(1000);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @After
    public void doAfter() {
        try {
            ConnectionManager.getInstance().stopManager();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testConnectivity() {
        try {
            Socket client = new Socket("localhost", 1777);
            RemoteInvocation invoker = new RemoteInvocation(TestReadClass.class, TestReadClass.class.getMethod("testReadMethod", new Class<?>[] {}), -1, new Serializable[] {});
            invoke(invoker, client);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testLogin() {
        try {
            Socket client = new Socket("localhost", 1777);
            User usr = new User("sashas", "pwd");
            RemoteInvocation invoker = new RemoteInvocation(ServerFacade.class, ServerFacade.class.getMethod("logon", new Class<?>[] { User.class }), -1, new Serializable[] { usr });
            invoke(invoker, client);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public <T> T invoke(RemoteInvocation invocation, Socket sock) throws BaseServerRuntimeException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
            out.writeObject(invocation);
            out.flush();
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            Object result = in.readObject();
            if (result instanceof BaseServerRuntimeException) {
                throw (BaseServerRuntimeException) result;
            } else if (result instanceof Throwable) {
                throw new ServerFataError("Unexpected exception", (Throwable) result);
            } else {
                try {
                    T resultObject = (T) result;
                    return resultObject;
                } catch (ClassCastException ex) {
                    throw new ServerFataError("Unexpected result", ex);
                }
            }
        } catch (Exception ex) {
            throw new ServerFataError("Unable to invoke", ex);
        }
    }
}
