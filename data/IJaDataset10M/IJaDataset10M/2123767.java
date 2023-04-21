package org.ironrhino.core.message;

import java.io.IOException;
import java.io.Serializable;

public interface Topic<T extends Serializable> {

    public void subscribe(T message);

    public void publish(T message) throws IOException;
}
