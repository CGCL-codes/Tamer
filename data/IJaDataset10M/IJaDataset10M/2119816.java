package mw.server.inet;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import junit.framework.Assert;
import mw.server.card.MWBaseTest;
import mw.server.model.Card;
import mw.server.model.CounterType;
import mw.server.model.SpellAbility;
import mw.server.model.MagicWarsModel.GameZone;
import mw.server.model.bean.CardBean;
import org.junit.Test;

public class MWGameStateObserverTest extends MWBaseTest {

    @Test
    public void testCheckForUpdates() {
        Card target = createCard("Khalni Heart Expedition", TEST_PLAYER_RED);
        addPermanent(target);
        assertTableCount(1);
        checkForUpdates(1);
        target.tap();
        assertEquals(target.isTapped(), true);
        game.checkChangesAndStateEffects();
        checkForUpdates(1);
        Card land = createCard("Swamp", TEST_PLAYER_RED);
        addPermanent(land);
        assertTableCount(2);
        checkForUpdates(1);
        assertEquals(game.getStack().size(), 1);
        SpellAbility sa = game.getStack().pop();
        resolveSpellAbility(sa);
        assertEquals(target.getCounters(CounterType.QUEST), 1);
        checkForUpdates(1);
        Card target2 = createCard("Air Elemental", TEST_PLAYER_RED);
        addPermanent(target2);
        assertTableCount(3);
        checkForUpdates(1);
        Card card = createCard("Shock", TEST_PLAYER_RED);
        assertEquals(1, card.getSpellAbilities().size());
        SpellAbility shock = card.getSpellAbility()[0];
        shock.setTargetCard(target2);
        resolveSpellAbility(shock);
        assertTableCount(3);
        checkForUpdates(2);
    }

    private void checkForUpdates(int size) {
        ArrayList<MWAtom> updateAtoms = game.getGameStateObserver().peekCurrentAtoms();
        log.info("size." + updateAtoms.size());
        for (MWAtom atom : updateAtoms) {
            log.info("type." + atom.changeType);
            log.info("card." + atom.object);
        }
        Assert.assertEquals(size, updateAtoms.size());
        game.getGameStateObserver().resetAtoms();
    }

    @Test
    public void testCheckForUpdateEquip() {
        Card sledge = createCard("Behemoth Sledge", TEST_PLAYER_RED);
        addPermanent(sledge);
        assertTableCount(1);
        Card elves = createCard("Llanowar Elves", TEST_PLAYER_RED);
        addPermanent(elves);
        assertTableCount(2);
        checkForUpdates(2);
        SpellAbility sa = sledge.getSpellAbility()[1];
        sa.setTargetCard(elves);
        resolveSpellAbility(sa);
        assertEquals(3, elves.getAttack());
        assertEquals(3, elves.getDefense());
        assertEquals(true, elves.getKeyword().contains("Trample"));
        assertEquals(true, elves.getKeyword().contains("Lifelink"));
        checkForUpdates(2);
    }

    @Test
    public void testCheckForUpdateAura() {
        Card creature = createCard("Bull Hippo", TEST_PLAYER_RED);
        addPermanent(creature);
        assertTableCount(1);
        checkForUpdates(1);
        Card land = createCard("Forest", TEST_PLAYER_GREEN);
        addPermanent(land);
        land = createCard("Swamp", TEST_PLAYER_GREEN);
        addPermanent(land);
        land = createCard("Swamp", TEST_PLAYER_GREEN);
        addPermanent(land);
        assertTableCount(4);
        checkForUpdates(3);
        Card aura = createCard("Exotic Curse", TEST_PLAYER_GREEN);
        SpellAbility sa = aura.getSpellAbility()[0];
        sa.setTargetCard(creature);
        resolveSpellAbility(sa);
        assertTableCount(5);
        assertStackCount(0);
        assertGraveCount(0);
        assertEquals(1, creature.getPower());
        assertEquals(1, creature.getToughness());
        checkForUpdates(1);
        land = createCard("Mountain", TEST_PLAYER_GREEN);
        addPermanent(land);
        acceptTopInvisible();
        assertTableCount(4);
        assertGraveCount(2);
        assertStackCount(0);
        checkForUpdates(5);
    }

    @Test
    public void testMultiDraw() {
        loadDeckAndDrawHand();
        game.createUpdateAtoms();
        game.getGameStateObserver().resetAtoms();
        int count = getTestPlayerRed().getLibrary().size();
        log.info("library count : " + count);
        game.moveToZone(GameZone.Hand, GameZone.Library, getTestPlayerRed().getHand().getCardList());
        game.createUpdateAtoms();
        ArrayList<MWAtom> updateAtoms = game.getGameStateObserver().peekCurrentAtoms();
        for (MWAtom atom : updateAtoms) {
            if (atom.changeType.equals(MWAtom.ChangeType.DECK_COUNT)) {
                count++;
                Assert.assertEquals(Integer.valueOf(count), (Integer) (atom.object));
            }
        }
    }

    /**
	 * Test card.getCopy() method that clones the card.
	 * This test reproduced the bug with "$this enters the battlefield tapped" for clones cards.
	 */
    @Test
    public void testCopy() {
        Card mire = createCard("Polluted Mire", TEST_PLAYER_RED);
        Card mireCopy = game.copyCard(mire);
        addPermanent(mireCopy);
        Assert.assertEquals(true, mireCopy.isTapped());
        addPermanent(mire);
        Assert.assertEquals(true, mire.isTapped());
    }

    @Test
    public void testRevealTop() {
        Card blackCard = createCard("Dark Ritual", TEST_PLAYER_RED);
        testPlayerRed.library.addFirst(blackCard);
        Card vampireNocturnus = createCard("Vampire Nocturnus", TEST_PLAYER_RED);
        addPermanent(vampireNocturnus);
        assertTableCount(1);
        game.checkChangesAndStateEffects();
        game.createUpdateAtoms();
        ArrayList<MWAtom> updateAtoms = game.getGameStateObserver().peekCurrentAtoms();
        boolean found = false;
        for (MWAtom atom : updateAtoms) {
            if (atom.changeType.equals(MWAtom.ChangeType.DECK_TOP)) {
                Assert.assertEquals(blackCard.getName(), ((CardBean) (atom.object)).getName());
                found = true;
            }
        }
        Assert.assertEquals("No DECK_TOP message.", true, found);
    }
}
