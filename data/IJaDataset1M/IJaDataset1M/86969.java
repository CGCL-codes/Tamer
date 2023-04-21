package cruise.umple.compiler;

import org.junit.*;

public class GuardTest {

    Guard guard;

    @Before
    public void setUp() {
        guard = new Guard("x");
    }

    @Test
    public void condition() {
        Assert.assertEquals("x", guard.getCondition());
    }

    @Test
    public void equalsOnlyCaresAboutCondition() {
        Guard g2 = new Guard("x");
        Assert.assertEquals(guard, g2);
        g2.setCondition("y");
        Assert.assertEquals(false, guard.equals(g2));
    }
}
