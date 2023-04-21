package miscel;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StateTest extends TestCase {

    public void test() {
        final State<String> s = new State<String>("sleep");
        s.addPossibleState("wake up");
        s.addPossibleState("eat");
        s.addPossibleState("fall asleep");
        s.addPossibleTransition("sleep", "wake up");
        s.addPossibleTransition("wake up", "eat");
        s.addPossibleTransition("eat", "fall asleep");
        s.addPossibleTransition("fall asleep", "sleep");
        s.addPossibleTransition("wake up", "fall asleep");
        Assert.assertEquals(s.getCurrentState(), "sleep");
        try {
            s.SetState("wake up");
        } catch (final Exception e) {
            fail("should not throw");
        }
        Assert.assertEquals(s.getCurrentState(), "wake up");
        try {
            s.SetState("sleep");
            fail("should throw");
        } catch (final Exception e) {
        }
        Assert.assertEquals(s.getCurrentState(), "wake up");
        try {
            s.SetState("eat");
        } catch (final Exception e) {
            fail("should not throw");
        }
        try {
            s.SetState("wake up");
            fail("should throw");
        } catch (final Exception e) {
        }
    }
}
