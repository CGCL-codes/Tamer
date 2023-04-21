package ru.jnano.app.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleFormatter extends Formatter {

    @Override
    public String format(LogRecord arg0) {
        String except_message = "";
        if (arg0.getThrown() != null) except_message = arg0.getThrown().getMessage();
        return arg0.getMessage() + ": " + except_message + "\n";
    }
}
