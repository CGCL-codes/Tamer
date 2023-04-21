package org.equanda.util;

import javolution.lang.TextBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple object for testing, allows white box verifications in testcases.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class MockObject {

    private List<String> list = new ArrayList();

    /**
     * Add a message which can be verified
     *
     * @param msg
     */
    public void add(String msg) {
        list.add(msg);
    }

    /**
     * Clear all the registered messages
     */
    public void clear() {
        list.clear();
    }

    /**
     * Verify whether the next message is as expected
     *
     * @param msg
     * @return true when equals
     */
    public boolean verify(String msg) {
        if (list.size() == 0) return false;
        return msg.equals(list.remove(0));
    }

    /**
     * Get the next message from the queue (also removes the message).
     *
     * @return next message
     */
    public String get() {
        if (list.size() == 0) return null;
        return list.remove(0);
    }

    /**
     * Get a string with all the messages in the queue, one on each line, does not change the queue.
     *
     * @return next message
     */
    public String getAll() {
        TextBuilder tb = TextBuilder.newInstance();
        for (String s : list) {
            tb.append(s);
            tb.append('\n');
        }
        return tb.toString();
    }
}
