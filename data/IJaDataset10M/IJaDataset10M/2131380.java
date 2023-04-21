package org.wings.recorder;

/**
 * @author hengels
 */
public class POST extends Request {

    public POST(String resource) {
        super("POST", resource);
    }

    public POST addEvent(String name, String[] values) {
        events.add(new Event(name, values));
        return this;
    }

    public POST addHeader(String name, String header) {
        headers.add(new Header(name, header));
        return this;
    }
}
