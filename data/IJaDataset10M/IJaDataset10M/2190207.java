package net.liveseeds.eye.domination;

import junit.framework.TestCase;
import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.base.liveseed.DefaultLiveSeed;
import net.liveseeds.base.world.DefaultWorld;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class Amount2LiveSeedTest extends TestCase {

    private static int amount;

    private static LiveSeed liveSeed;

    private Amount2LiveSeed amount2LiveSeed;

    public Amount2LiveSeedTest() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        amount = 111;
        liveSeed = new DefaultLiveSeed(new DefaultWorld());
        amount2LiveSeed = new Amount2LiveSeed(amount, liveSeed);
    }

    public void testGetAmount() throws Exception {
        assertEquals(amount, amount2LiveSeed.getAmount());
    }

    public void testGetLiveSeed() throws Exception {
        assertEquals(liveSeed, amount2LiveSeed.getLiveSeed());
    }
}
