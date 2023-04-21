package com.luca.blackjack.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.luca.blackjack.card.Card;
import com.luca.blackjack.card.Deck;

@RunWith(Parameterized.class)
public class StandardDeck {

    Deck deck;

    int deckNo;

    int seed;

    List<Object> results;

    public StandardDeck(List<Object> inputs, List<Object> results) {
        this.deckNo = (Integer) inputs.get(0);
        this.seed = (Integer) inputs.get(1);
        this.results = results;
    }

    @Before
    public final void setup() {
        deck = new com.luca.blackjack.card.StandardDeck(deckNo, seed);
        assertTrue(!deck.hasBurnCardFound());
    }

    @After
    public final void tearDown() {
        deck = null;
    }

    @Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object> set0 = new ArrayList<Object>();
        set0.addAll(Arrays.asList(8, 1982));
        List<Object> result0 = new ArrayList<Object>();
        result0.addAll(Arrays.asList(416));
        ArrayList<Object> set1 = new ArrayList<Object>();
        set1.addAll(Arrays.asList(7, -30));
        List<Object> result1 = new ArrayList<Object>();
        result1.addAll(Arrays.asList(364));
        ArrayList<Object> set2 = new ArrayList<Object>();
        set2.addAll(Arrays.asList(6, 32));
        List<Object> result2 = new ArrayList<Object>();
        result2.addAll(Arrays.asList(312));
        ArrayList<Object> set3 = new ArrayList<Object>();
        set3.addAll(Arrays.asList(5, -3));
        List<Object> result3 = new ArrayList<Object>();
        result3.addAll(Arrays.asList(260));
        ArrayList<Object> set4 = new ArrayList<Object>();
        set4.addAll(Arrays.asList(4, 6546546));
        List<Object> result4 = new ArrayList<Object>();
        result4.addAll(Arrays.asList(208));
        ArrayList<Object> set5 = new ArrayList<Object>();
        set5.addAll(Arrays.asList(3, -1221));
        List<Object> result5 = new ArrayList<Object>();
        result5.addAll(Arrays.asList(156));
        ArrayList<Object> set6 = new ArrayList<Object>();
        set6.addAll(Arrays.asList(2, 0));
        List<Object> result6 = new ArrayList<Object>();
        result6.addAll(Arrays.asList(104));
        ArrayList<Object> set7 = new ArrayList<Object>();
        set7.addAll(Arrays.asList(1, 97));
        List<Object> result7 = new ArrayList<Object>();
        result7.addAll(Arrays.asList(52));
        ArrayList<Object> set8 = new ArrayList<Object>();
        set8.addAll(Arrays.asList(9, 1942182));
        List<Object> result8 = new ArrayList<Object>();
        result8.addAll(Arrays.asList(468));
        ArrayList<Object> set9 = new ArrayList<Object>();
        set9.addAll(Arrays.asList(14, -33));
        List<Object> result9 = new ArrayList<Object>();
        result9.addAll(Arrays.asList(728));
        return Arrays.asList(new Object[][] { { set0, result0 }, { set1, result1 }, { set2, result2 }, { set3, result3 }, { set4, result4 }, { set5, result5 }, { set6, result6 }, { set7, result7 }, { set8, result8 }, { set9, result9 } });
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorDeckNoLowerThan0() {
        new com.luca.blackjack.card.StandardDeck(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorDeckNoEqualTo0() {
        new com.luca.blackjack.card.StandardDeck(0, 0);
    }

    @Test(expected = IllegalStateException.class, timeout = 1000)
    public final void deckEventuallyGetsEmpty() {
        while (true) deck.getCard();
    }

    @Test
    public final void numberOfCards() {
        int cardNo = 0;
        try {
            while (true) {
                deck.getCard();
                cardNo++;
            }
        } catch (IllegalStateException e) {
            assertEquals(results.get(0), cardNo);
        }
    }

    @Test
    public final void burnCardBeforeLast13thAnd26th() {
        while (!deck.hasBurnCardFound()) deck.getCard();
        int cardLeft = 0;
        try {
            while (true) {
                deck.getCard();
                cardLeft++;
            }
        } catch (IllegalStateException e) {
            assertTrue(cardLeft >= 13);
            assertTrue(cardLeft <= 26);
        }
    }

    @Test
    public final void regenerateResetsBurnCard() {
        while (!deck.hasBurnCardFound()) deck.getCard();
        deck.regenerate();
        assertTrue(!deck.hasBurnCardFound());
    }

    @Test
    public final void initialise() {
        List<Card> current = new ArrayList<Card>();
        try {
            while (true) current.add(deck.getCard());
        } catch (Exception e) {
        }
        deck.regenerate();
        List<Card> regenerated = new ArrayList<Card>();
        try {
            while (true) regenerated.add(deck.getCard());
        } catch (Exception e) {
        }
        assertEquals(regenerated.size(), current.size());
        boolean differentCard = false;
        try {
            for (int i = 0; !differentCard; i++) differentCard = current.get(i) == regenerated.get(i) ? true : false;
        } catch (Exception e) {
        }
        assertTrue(differentCard);
    }
}
