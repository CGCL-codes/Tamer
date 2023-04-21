package gnu.testlet.java2.lang.reflect.Proxy;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** A basic test for the proxy mechanism that tests whether the arguments are delivered correctly.
 * 
 * @author Robert Schuster
 */
public class check13 implements Testlet, InvocationHandler, Serializable {

    transient Object proxy;

    transient Object[] args;

    transient Method method;

    public void test(TestHarness harness) {
        ActionListener al = (ActionListener) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { ActionListener.class }, this);
        Method expectedMethod = null;
        try {
            expectedMethod = ActionListener.class.getMethod("actionPerformed", new Class[] { ActionEvent.class });
        } catch (NoSuchMethodException nsme) {
            harness.fail("test setup failed");
        }
        ActionEvent event = new ActionEvent(this, 0, "GNU yourself!");
        al.actionPerformed(event);
        harness.check(proxy == al, "proxy method called");
        harness.check(method, expectedMethod);
        harness.check(args.length, 1);
        harness.check(args[0] == event);
        harness.checkPoint("serialization");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(proxy);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Proxy p = (Proxy) ois.readObject();
            harness.check(p.getClass() == proxy.getClass());
            harness.check(Proxy.getInvocationHandler(p).getClass() == Proxy.getInvocationHandler(proxy).getClass());
        } catch (Exception x) {
            harness.debug(x);
            harness.fail("Unexpected exception");
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        return null;
    }
}
