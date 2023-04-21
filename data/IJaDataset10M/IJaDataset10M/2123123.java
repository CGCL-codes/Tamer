package org.jdiagnose.library.logging;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.jdiagnose.remote.file.Emitter;

/**
 * @author jmccrindle
 */
public class Log4jEmitter implements Emitter {

    Category log = null;

    Priority priority = Priority.DEBUG;

    public Log4jEmitter() {
        log = Category.getInstance(Log4jEmitter.class);
    }

    public Log4jEmitter(String category) {
        log = Category.getInstance(category);
    }

    public void emit(StringBuffer value) {
        log.log(priority, value);
    }

    public void setPriority(String priority) {
        this.priority = Priority.toPriority(priority, Priority.DEBUG);
    }
}
