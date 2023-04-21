package com.ewansilver.raindrop.demo.httpserver;

import java.util.Date;
import com.ewansilver.raindrop.Handler;
import com.ewansilver.raindrop.HandlerImpl;

/**
 * This handler simply returns the system time.
 * @author ewan.silver @ gmail.com
 */
public class DateHandler extends HandlerImpl implements Handler {

    /**
	 * Constructor.
	 */
    public DateHandler() {
        super();
    }

    public void handle(Object aTask) {
        if (aTask instanceof HttpConnection) {
            HttpConnection request = (HttpConnection) aTask;
            Date now = new Date();
            String string = "<p>The current date is: " + now.toString() + "</p><p>As Unix epoch this is: <b>" + now.getTime() + "</b></p>";
            request.setResponse(string.getBytes(), "text/html");
            request.sendToClient();
        }
    }
}
