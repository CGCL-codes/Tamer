package com.shoesobjects.bourre;

import com.shoesobjects.bourre.decisionstrategies.LooseDecisionStrategy;
import com.shoesobjects.bourre.decisionstrategies.TightDecisionStrategy;
import com.shoesobjects.bourre.drawstrategies.KeepOnlyTrumpDrawStrategy;
import com.shoesobjects.bourre.game.BourreGame;
import com.shoesobjects.bourre.game.BourreHand;
import com.shoesobjects.bourre.game.BourrePlayer;
import com.shoesobjects.bourre.game.Position;
import com.shoesobjects.deckofcards.Card;
import com.shoesobjects.deckofcards.CardSuit;
import com.shoesobjects.deckofcards.CardValue;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class BaseBourreGameTest extends TestCase {

    public void testBourreGameIsNotNull() {
        BourreGame bg = getBourreGame();
        assertNotNull(bg);
    }

    protected BourrePlayer getPlayer(String name) {
        return new BourrePlayer(name);
    }

    protected BourrePlayer getLooseDecisionKeepOnlyTrumpPlayer(String name) {
        BourrePlayer player = new BourrePlayer(name);
        player.setDecisionStrategy(new LooseDecisionStrategy());
        player.setDrawStrategy(new KeepOnlyTrumpDrawStrategy());
        return player;
    }

    protected BourrePlayer getTightDecisionKeepOnlyTrumpPlayer(String name) {
        BourrePlayer player = new BourrePlayer(name);
        player.setDecisionStrategy(new TightDecisionStrategy());
        player.setDrawStrategy(new KeepOnlyTrumpDrawStrategy());
        return player;
    }

    protected BourreGame getBourreGame() {
        BourreGame bg = new BourreGame();
        return bg;
    }

    protected BourreGame getValidOnePlayerBourreGame() {
        BourreGame bg = getBourreGame();
        bg.addPlayer(getPlayer("Mike"));
        bg.startGame();
        return bg;
    }

    protected Position getFirstToActOfSevenPlayersPosition() {
        return new Position(7, 0, 0, 0);
    }

    protected Position getFirstToActOfFivePlayersPosition() {
        return new Position(5, 0, 0, 0);
    }

    protected Position getSecondToActOfSevenPlayersOneInPosition() {
        return new Position(7, 1, 0, 0);
    }

    protected Position getLastToActOfSevenPlayersOneInPosition() {
        return new Position(7, 6, 0, 0);
    }

    protected BourreHand getTwoThroughSixOneSuit(CardSuit suit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.TWO));
        hand.addCard(new Card(suit, CardValue.THREE));
        hand.addCard(new Card(suit, CardValue.FOUR));
        hand.addCard(new Card(suit, CardValue.FIVE));
        hand.addCard(new Card(suit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getThreeThroughSixPlusAceOneSuit(CardSuit suit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.THREE));
        hand.addCard(new Card(suit, CardValue.FOUR));
        hand.addCard(new Card(suit, CardValue.FIVE));
        hand.addCard(new Card(suit, CardValue.SIX));
        hand.addCard(new Card(suit, CardValue.ACE));
        return hand;
    }

    protected BourreHand getOneTrumpCardTwoLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.TWO));
        hand.addCard(new Card(offSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getOneTrumpCardThreeLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardTwoAndThreeLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.TWO));
        hand.addCard(new Card(trumpSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getThreeTrumpCardTwoThroughFourLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.TWO));
        hand.addCard(new Card(trumpSuit, CardValue.THREE));
        hand.addCard(new Card(trumpSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getOneTrumpCardKingLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.KING));
        hand.addCard(new Card(offSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardThreeAndFourLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.THREE));
        hand.addCard(new Card(trumpSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardKingAndQueenLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.KING));
        hand.addCard(new Card(trumpSuit, CardValue.QUEEN));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardAceAndKingLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.KING));
        hand.addCard(new Card(trumpSuit, CardValue.ACE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardAceAndTwoLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.TWO));
        hand.addCard(new Card(trumpSuit, CardValue.ACE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getTwoTrumpCardKingAndTwoLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.KING));
        hand.addCard(new Card(trumpSuit, CardValue.TWO));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getOneTrumpCardAceLowOffSuit(CardSuit trumpSuit, CardSuit offSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.ACE));
        hand.addCard(new Card(offSuit, CardValue.THREE));
        hand.addCard(new Card(offSuit, CardValue.FOUR));
        hand.addCard(new Card(offSuit, CardValue.FIVE));
        hand.addCard(new Card(offSuit, CardValue.SIX));
        return hand;
    }

    protected BourreHand getThreeThroughSixOneSuit(CardSuit suit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.THREE));
        hand.addCard(new Card(suit, CardValue.FOUR));
        hand.addCard(new Card(suit, CardValue.FIVE));
        hand.addCard(new Card(suit, CardValue.SIX));
        hand.addCard(new Card(suit, CardValue.SEVEN));
        return hand;
    }

    protected BourreHand getNoTrumpHandAceThroughTen(CardSuit suit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.ACE));
        hand.addCard(new Card(suit, CardValue.KING));
        hand.addCard(new Card(suit, CardValue.QUEEN));
        hand.addCard(new Card(suit, CardValue.JACK));
        hand.addCard(new Card(suit, CardValue.TEN));
        return hand;
    }

    protected BourreHand getHandKingThroughNine(CardSuit suit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.KING));
        hand.addCard(new Card(suit, CardValue.QUEEN));
        hand.addCard(new Card(suit, CardValue.JACK));
        hand.addCard(new Card(suit, CardValue.TEN));
        hand.addCard(new Card(suit, CardValue.NINE));
        return hand;
    }

    protected BourreHand getOneTrumpHandAceThroughTenOfTrump(CardSuit suit, CardSuit trumpSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(suit, CardValue.ACE));
        hand.addCard(new Card(suit, CardValue.KING));
        hand.addCard(new Card(suit, CardValue.QUEEN));
        hand.addCard(new Card(suit, CardValue.JACK));
        hand.addCard(new Card(trumpSuit, CardValue.TEN));
        return hand;
    }

    protected BourreHand getOneTrumpHandAceOfTrumpThroughTen(CardSuit suit, CardSuit trumpSuit) {
        BourreHand hand = new BourreHand();
        hand.addCard(new Card(trumpSuit, CardValue.ACE));
        hand.addCard(new Card(suit, CardValue.KING));
        hand.addCard(new Card(suit, CardValue.QUEEN));
        hand.addCard(new Card(suit, CardValue.JACK));
        hand.addCard(new Card(suit, CardValue.TEN));
        return hand;
    }

    protected Card getTwoCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.TWO);
    }

    protected Card getFiveCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.FIVE);
    }

    protected Card getEightCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.EIGHT);
    }

    protected Card getKingCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.KING);
    }

    protected Card getNineCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.NINE);
    }

    protected Card getAceOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.ACE);
    }

    protected Card getThreeCardOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.THREE);
    }

    protected Card getJackOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.JACK);
    }

    protected Card getKingOfSuit(CardSuit suit) {
        return new Card(suit, CardValue.KING);
    }

    protected List getListOfPlayers(int numberOfPlayers) {
        List playerList = new ArrayList();
        for (int i = 0; i < numberOfPlayers; i++) {
            BourrePlayer p = getPlayer("Fred" + i);
            playerList.add(p);
        }
        return playerList;
    }
}
