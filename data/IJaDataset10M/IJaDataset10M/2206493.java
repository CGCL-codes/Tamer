package com.att.echarts.monitor;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class LogEvent extends MachineMonitorEvent {

    public static final String MESSAGE = "MESSAGE";

    public static final String EXCEPTION = "EXCEPTION";

    public static final String STACK_TRACE = "STACK_TRACE";

    /**
	   For MachineMonitorEvent.readRaw().
	 */
    public LogEvent() {
    }

    public LogEvent(String message) {
        properties.setProperty(MESSAGE, message);
    }

    public LogEvent(Throwable e, String message) {
        properties.setProperty(EXCEPTION, e.toString());
        properties.setProperty(MESSAGE, message);
        CharArrayWriter trace = new CharArrayWriter();
        e.printStackTrace(new PrintWriter(trace));
        properties.setProperty(STACK_TRACE, trace.toString());
    }
}
