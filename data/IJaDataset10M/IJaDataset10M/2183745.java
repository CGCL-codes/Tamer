package org.jvprocess.shared.test;

import java.util.List;
import org.apache.log4j.Logger;

public class FooActivity {

    private static Logger logger = Logger.getLogger(FooActivity.class);

    public String[] execute(Object payload) {
        logger.debug("executing FooActivity");
        return new String[] { "A" };
    }
}
