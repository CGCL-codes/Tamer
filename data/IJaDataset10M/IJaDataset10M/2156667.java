package org.apache.axis2.engine;

import org.apache.axis2.description.Flow;
import javax.xml.namespace.QName;

public class MockFlow extends Flow {

    public MockFlow(String message, int length) {
        super();
        for (int i = 0; i < length; i++) {
            SpeakingHandler1 h1 = new SpeakingHandler1("Executing " + i + " inside " + message, new QName("SpeakingHandler" + i));
            h1.setName("SpeakingHandler" + i);
            this.addHandler(h1.getHandlerDescription());
        }
    }
}
