package org.iqual.chaplin.example.basic.target;

import junit.framework.TestCase;
import org.iqual.chaplin.FromContext;
import static org.iqual.chaplin.DynaCastUtils.$;
import static org.iqual.chaplin.DynaCastUtils.$role;
import org.iqual.chaplin.msg.Message;
import org.iqual.chaplin.msg.SenderSelector;
import org.iqual.chaplin.msg.TargetSelector;

/**
 * @author Zbynek Slajchrt
 * @since 6.12.2009 18:03:35
 */
public class TargetSelectorTest extends TestCase {

    private static final String MSG1 = "msg1";

    private static final String MSG2 = "msg2";

    private static final String OK = "OK";

    public abstract static class Sender {

        private String message;

        private String reply;

        public Sender(String message) {
            this.message = message;
        }

        public void replyMessage(String reply) {
            this.reply = reply;
        }

        @FromContext
        abstract void message(String content);

        public void send() {
            message(message);
        }
    }

    public abstract static class Receiver {

        @FromContext(target = SenderSelector.class)
        abstract void replyMessage(String reply);

        public void message(String content) {
            replyMessage(OK);
        }
    }

    public void testTestSenderSelector() {
        Sender s1 = $(MSG1);
        Sender s2 = $(MSG2);
        Receiver rcv = $($role("s1", s1), $role("s2", s2));
        s1.send();
        assertEquals(OK, s1.reply);
        assertNull(s2.reply);
        s1.reply = null;
        s2.send();
        assertEquals(OK, s2.reply);
        assertNull(s1.reply);
    }

    public static class ComponentX {

        String reply;

        public void replyMessage(String reply) {
            this.reply = reply;
        }
    }

    public static class CustomTargetSelector implements TargetSelector {

        public boolean isTarget(Object component, Message message) {
            return component instanceof ComponentX;
        }
    }

    public abstract static class Receiver2 {

        @FromContext(target = CustomTargetSelector.class)
        abstract void replyMessage(String reply);

        public void message(String content) {
            replyMessage(OK);
        }
    }

    public void testTestCustomSelector() {
        Sender s1 = $(MSG1);
        Sender s2 = $(MSG2);
        ComponentX cx = $();
        Receiver2 rcv = $($role("s1", s1), $role("s2", s2), $role("cx", cx));
        s1.send();
        assertNull(s1.reply);
        assertNull(s2.reply);
        assertEquals(OK, cx.reply);
        cx.reply = null;
        s2.send();
        assertNull(s1.reply);
        assertNull(s2.reply);
        assertEquals(OK, cx.reply);
    }
}
