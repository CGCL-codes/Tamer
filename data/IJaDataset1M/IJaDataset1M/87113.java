package br.unifor.g2cl.test;

import java.io.IOException;
import junit.framework.Assert;
import net.sf.jgcs.ClosedSessionException;
import net.sf.jgcs.ControlSession;
import net.sf.jgcs.JGCSException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import br.unifor.g2cl.hl.StateTransferDataSession;
import br.unifor.g2cl.hl.listener.StateListener;

public class StateTransferTest {

    @org.junit.Test
    public void transferState() throws IOException, InterruptedException {
        StateTransferTestObject member1 = new StateTransferTestObject(true);
        StateTransferTestObject member2 = new StateTransferTestObject(false);
        Assert.assertEquals("This is your state!", member2.getState());
        member1.close();
        member2.close();
    }
}

class StateTransferTestObject implements StateListener {

    ClassPathXmlApplicationContext context = null;

    StateTransferDataSession stateTransfer = null;

    ControlSession controlSession;

    boolean hasState;

    private Object lock_state = new Object();

    public String state = null;

    public StateTransferTestObject(boolean hasState) throws IOException, InterruptedException {
        if (hasState) {
            state = "This is your state!";
        }
        this.hasState = hasState;
        context = new ClassPathXmlApplicationContext(new String[] { "classpath:toolkit-config.xml", "classpath:g2cl-config.xml" });
        controlSession = (ControlSession) context.getBean("ControlSession");
        controlSession.join();
        stateTransfer = (StateTransferDataSession) context.getBean("StateTransfer");
        stateTransfer.setStateListener(this);
        if (!this.hasState) {
            stateTransfer.requestState();
            synchronized (lock_state) {
                while (!this.hasState) {
                    lock_state.wait();
                }
            }
        }
    }

    public void close() throws ClosedSessionException, JGCSException {
        stateTransfer.close();
        controlSession.leave();
        context.close();
    }

    public boolean hasState() {
        return hasState;
    }

    public String getState() {
        return "This is your state!";
    }

    public void stateIncoming(byte[] state) {
        this.state = new String(state);
        synchronized (lock_state) {
            hasState = true;
            lock_state.notifyAll();
        }
    }

    public byte[] stateOutgoing() {
        return state.getBytes();
    }
}
