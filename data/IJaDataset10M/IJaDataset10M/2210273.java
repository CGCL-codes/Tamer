package org.jeuron.jlightning.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeuron.jlightning.message.MockMessageObject;

/**
 *
 * @author mikekarrys
 */
public class MockService implements Service {

    protected final Log logger = LogFactory.getLog(getClass());

    private int i = 0;

    public MockService() {
        if (logger.isDebugEnabled()) {
            logger.debug("mockService: created.");
        }
    }

    public int getI() {
        return this.i;
    }

    public Object process(Object o) {
        int j = 0;
        if (o instanceof MockMessageObject) {
            MockMessageObject mockMessageObject = (MockMessageObject) o;
            if (logger.isDebugEnabled()) {
                logger.debug("mockService: set i to " + mockMessageObject.getI());
            }
            j = i;
            this.i = mockMessageObject.getI();
            mockMessageObject.setI(j);
            o = mockMessageObject;
        }
        return o;
    }
}
