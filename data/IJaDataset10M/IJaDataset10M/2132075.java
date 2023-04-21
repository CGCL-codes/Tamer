package eg.test.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: Jan 18, 2010
 * Time: 6:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Test
public class SomeUnitTest {

    private static final Logger log = LoggerFactory.getLogger(SomeUnitTest.class);

    public void checkSomething() {
        log.info("Check, one two three... unit test!");
    }
}
