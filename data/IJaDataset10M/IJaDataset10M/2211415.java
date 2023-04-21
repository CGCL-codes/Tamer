package commons;

import org.makagiga.commons.MIcon;
import org.makagiga.test.AbstractTest;
import org.makagiga.test.Test;
import org.makagiga.test.TestMethod;

@Test(className = MIcon.class)
public final class TestMIcon extends AbstractTest {

    @Test(methods = { @TestMethod(name = "getDefaultSize") })
    public void test_getDefaultSize() {
        assert MIcon.getDefaultSize() == 24;
    }
}
