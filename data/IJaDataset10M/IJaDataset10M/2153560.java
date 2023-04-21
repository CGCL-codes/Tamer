package org.jogre.dominoes.client;

import java.util.Vector;
import junit.framework.TestCase;
import nanoxml.XMLElement;
import org.jogre.dominoes.common.CommDominoesPlayDomino;
import org.jogre.dominoes.common.Domino;

/**
 * Test case for game model for the dominoes game.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
public class DominoesModelTest extends TestCase {

    /**
	 * Test the model.
	 */
    public void testBasic() throws Exception {
        DominoesTestModel model = new DominoesTestModel(3, 5, 4, 2, 2);
        testValidMoves(model);
        saveRestoreTest(model);
    }

    private void setupModel(DominoesTestModel model) {
        model.reset();
        int numPlayers = model.getNumOfPlayers();
        int initialHandSize = model.getInitialHandSize();
        for (int p = 0; p < numPlayers; p++) {
            model.addToHandSize(p, initialHandSize);
        }
    }

    /**
	 * Test that some valid & invalid moves are managed properly by the
	 * model.
	 */
    private void testValidMoves(DominoesTestModel model) throws Exception {
        setupModel(model);
        int initialHandSize = model.getInitialHandSize();
        int maxDominoValue = model.getMaxDominoValue();
        Domino anchor = new Domino(3, 3);
        model.playDomino(0, anchor, CommDominoesPlayDomino.AS_ANCHOR_DOMINO);
        assertTrue(model.getAnchorDomino().isSameAs(anchor));
        for (int i = 0; i < maxDominoValue; i++) {
            assertEquals(((i == 3) ? 0 : -1), model.tryToPlace(i));
        }
        model.playDomino(1, new Domino(3, 4), 0);
        assertEquals(1, model.getDominoChain(0).size());
        assertEquals(0, model.getDominoChain(1).size());
        assertEquals(initialHandSize, model.getPlayerHandSize(0));
        assertEquals(initialHandSize - 1, model.getPlayerHandSize(1));
        assertEquals(-1, model.tryToPlace(0));
        assertEquals(-1, model.tryToPlace(1));
        assertEquals(-1, model.tryToPlace(2));
        assertEquals(1, model.tryToPlace(3));
        assertEquals(0, model.tryToPlace(4));
        model.playDomino(0, new Domino(0, 4), 0);
        assertEquals(2, model.getDominoChain(0).size());
        assertEquals(0, model.getDominoChain(1).size());
        assertEquals(initialHandSize - 1, model.getPlayerHandSize(0));
        assertEquals(initialHandSize - 1, model.getPlayerHandSize(1));
        assertEquals(0, model.tryToPlace(0));
        assertEquals(-1, model.tryToPlace(1));
        assertEquals(-1, model.tryToPlace(2));
        assertEquals(1, model.tryToPlace(3));
        assertEquals(-1, model.tryToPlace(4));
        model.playDomino(1, new Domino(3, 2), 1);
        assertEquals(2, model.getDominoChain(0).size());
        assertEquals(1, model.getDominoChain(1).size());
        assertEquals(initialHandSize - 1, model.getPlayerHandSize(0));
        assertEquals(initialHandSize - 2, model.getPlayerHandSize(1));
        assertEquals(0, model.tryToPlace(0));
        assertEquals(-1, model.tryToPlace(1));
        assertEquals(1, model.tryToPlace(2));
        assertEquals(-1, model.tryToPlace(3));
        assertEquals(-1, model.tryToPlace(4));
    }

    /**
	 * Test saving the state of the model and restoring it in another model.
	 * This is what happens when a client connects in the middle of a game.
	 *
	 * This will flatten the source model provided and restore it into a new
	 * model and then compare the two.
	 */
    private void saveRestoreTest(DominoesTestModel srcModel) throws Exception {
        int numPlayers = srcModel.getNumOfPlayers();
        int maxDominoValue = srcModel.getMaxDominoValue();
        int initialHandSize = srcModel.getInitialHandSize();
        int minBoneyardSize = srcModel.getMinBoneyardSize();
        int drawLimit = srcModel.getDrawLimit();
        XMLElement elm = srcModel.flatten();
        DominoesTestModel model = new DominoesTestModel(numPlayers, maxDominoValue, initialHandSize, minBoneyardSize, drawLimit);
        model.setState(elm);
        assertTrue(srcModel.getAnchorDomino().isSameAs(model.getAnchorDomino()));
        verifySameChain(srcModel.getDominoChain(0), model.getDominoChain(0));
        verifySameChain(srcModel.getDominoChain(1), model.getDominoChain(1));
        for (int i = 0; i < numPlayers; i++) {
            assertEquals(srcModel.getPlayerHandSize(i), model.getPlayerHandSize(i));
            assertEquals(srcModel.getLastAddSize(i), model.getLastAddSize(i));
        }
        assertEquals(srcModel.getBoneyardSize(), model.getBoneyardSize());
    }

    /**
	 * Verify that the two given chains contain the same dominoes in the same
	 * order.
	 * 
	 * @param a   The first chain of dominoes
	 * @param b   The second chain of dominoes
	 */
    private void verifySameChain(Vector<Domino> a, Vector<Domino> b) throws Exception {
        assertEquals(a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            Domino ad = a.get(i);
            Domino bd = b.get(i);
            assertTrue(ad.isSameAs(bd));
        }
    }
}
