package com.luca.blackjack.test.unit;

import static org.junit.Assert.assertEquals;
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
import com.luca.blackjack.game.Rules;

@RunWith(Parameterized.class)
public class StandardRules {

    Rules rules;

    boolean soft17;

    int deckNo;

    boolean earlySurrender;

    int resplit;

    boolean resplitSplitAces;

    boolean hitSplitAces;

    boolean doubleSplitAces;

    boolean noDoubleAfterSplit;

    boolean renoRule;

    boolean renoRuleEuropean;

    boolean noHoleCard;

    boolean obo;

    String blackJackPayout;

    boolean dealerWinTies;

    String winPayout;

    List<Object> results;

    public StandardRules(List<Object> inputs, List<Object> results) {
        this.soft17 = (Boolean) inputs.get(0);
        this.deckNo = (Integer) inputs.get(1);
        this.earlySurrender = (Boolean) inputs.get(2);
        this.resplit = (Integer) inputs.get(3);
        this.resplitSplitAces = (Boolean) inputs.get(4);
        this.hitSplitAces = (Boolean) inputs.get(5);
        this.doubleSplitAces = (Boolean) inputs.get(6);
        this.noDoubleAfterSplit = (Boolean) inputs.get(7);
        this.renoRule = (Boolean) inputs.get(8);
        this.renoRuleEuropean = (Boolean) inputs.get(9);
        this.noHoleCard = (Boolean) inputs.get(10);
        this.obo = (Boolean) inputs.get(11);
        this.blackJackPayout = (String) inputs.get(12);
        this.dealerWinTies = (Boolean) inputs.get(13);
        this.winPayout = (String) inputs.get(14);
        this.results = results;
    }

    @Before
    public final void setup() {
        rules = new com.luca.blackjack.game.StandardRules(soft17, deckNo, earlySurrender, resplit, resplitSplitAces, hitSplitAces, doubleSplitAces, noDoubleAfterSplit, renoRule, renoRuleEuropean, noHoleCard, obo, blackJackPayout, dealerWinTies, winPayout);
    }

    @After
    public final void tearDown() {
        rules = null;
    }

    @Parameters
    @SuppressWarnings("unchecked")
    public static Collection<Object[]> data() {
        ArrayList<Object> set0 = new ArrayList<Object>();
        set0.addAll(Arrays.asList(false, 8, false, 2, false, false, false, false, false, false, false, false, "3:2", false, "2:1"));
        List<Object> result0 = new ArrayList<Object>();
        result0.addAll(Arrays.asList(1.5, 2.0));
        ArrayList<Object> set1 = new ArrayList<Object>();
        set1.addAll(Arrays.asList(false, 8, false, 2, false, false, false, false, false, true, false, false, "15:9", false, "8:7"));
        List<Object> result1 = new ArrayList<Object>();
        result1.addAll(Arrays.asList(1.666, 1.142));
        return Arrays.asList(new Object[][] { { set0, result0 }, { set1, result1 } });
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorNoDeck0() {
        new com.luca.blackjack.game.StandardRules(false, 0, false, 2, false, false, false, false, true, false, false, false, "3:2", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorWrongNoReSplit() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, -2, false, false, false, false, true, true, false, false, "3:2", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorRenoRuleAndEuropeanRenoRule() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, 2, false, false, false, false, true, true, false, false, "3:2", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorWrongBlackJackPayout1() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, 2, false, false, false, false, true, true, false, false, "32", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorWrongBlackJackPayout2() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, 2, false, false, false, false, true, true, false, false, "32:", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorWrongBlackJackPayout3() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, 2, false, false, false, false, true, true, false, false, ":32", false, "2:1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void constructorWrongWinPayout() {
        new com.luca.blackjack.game.StandardRules(false, 8, false, 2, false, false, false, false, true, true, false, false, "3:2", false, "21");
    }

    @Test
    public final void getBlackJackPayout() {
        double actual = rules.getBlackJackPayout();
        double expected = (Double) results.get(0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public final void getWinPayout() {
        double actual = rules.getWinPayout();
        double expected = (Double) results.get(1);
        assertEquals(expected, actual, 0.001);
    }
}
