package mw.server.card.white;

import junit.framework.Assert;
import mw.server.card.MWBaseTest;
import mw.server.model.Card;
import mw.server.model.CounterType;
import mw.server.model.MagicWarsModel;
import mw.server.model.SpellAbility;
import mw.server.model.cost.Cost;
import mw.server.model.cost.LoyaltyCost;
import org.apache.log4j.Logger;
import org.junit.Test;

public class GideonJuraTest extends MWBaseTest {

    public static Logger log = Logger.getLogger(GideonJuraTest.class);

    /**
	 * Test spell ability fizzle when not tapped creature is targeted.
	 * Test tapped creature destroying.
	 */
    @Test
    public void testSecondAbility() {
        Card gideonJura = createCard("Gideon Jura", TEST_PLAYER_RED);
        Assert.assertEquals(4, gideonJura.getSpellAbilities().size());
        addPermanent(gideonJura);
        assertTableCount(1);
        Assert.assertEquals(6, gideonJura.getLoyaltyCounters());
        Card apexHawks = createCard("Apex Hawks", TEST_PLAYER_GREEN);
        addPermanent(apexHawks);
        assertTableCount(2);
        SpellAbility sa = gideonJura.getSpellAbilities().get(2);
        sa.setTargetCard(apexHawks);
        resolveSpellAbility(sa);
        assertTableCount(2);
        apexHawks.tap();
        resolveSpellAbility(sa);
        assertTableCount(1);
        Assert.assertEquals(4, gideonJura.getLoyaltyCounters());
    }

    /**
	 * Tests loyalty counters only.
	 */
    @Test
    public void testFirstAbility() {
        Card gideonJura = createCard("Gideon Jura", TEST_PLAYER_RED);
        Assert.assertEquals(4, gideonJura.getSpellAbilities().size());
        addPermanent(gideonJura);
        assertTableCount(1);
        Assert.assertEquals(6, gideonJura.getLoyaltyCounters());
        SpellAbility sa = gideonJura.getSpellAbilities().get(1);
        resolveSpellAbility(sa);
        Assert.assertEquals(8, gideonJura.getLoyaltyCounters());
    }
}
