package colakao;

import java.util.*;
import javax.swing.*;
import taliav2.Player;

public class Dealer {

    /** Variable which stores Cards already used during play.*/
    protected ArrayList<Card> CardsUsed;

    /** Used to store Card ready to get when variable get>0 or when player wants to.*/
    protected ArrayList<Card> CardDeck;

    /** Card type variable storing Card laying on top.*/
    protected Card ActiveCard;

    protected char Colors[], demand;

    protected int carsdInDeck, cardsPerColor, jokers_amount, available, players, demandQueue, stopQueue, get;

    /** Storing path to imagefile of the specified reverse.*/
    protected String rewers;

    public Dealer(int amount, int jokers, int taliaI) {
        get = 0;
        CardDeck = new ArrayList<Card>();
        CardsUsed = new ArrayList<Card>();
        demand = '0';
        demandQueue = 0;
        stopQueue = 0;
        Colors = new char[] { 'H', 'D', 'S', 'C' };
        carsdInDeck = 52;
        available = 52;
        cardsPerColor = 13;
        jokers_amount = jokers;
        if (jokers > 0) {
            carsdInDeck += jokers;
            available += jokers;
            cardsPerColor++;
        }
        ShuffleDeck(taliaI);
    }

    private void ShuffleDeck(int taliaI) {
        int minus = 0;
        if (jokers_amount != 0) minus++;
        for (int i = 0; i < cardsPerColor - minus; i++) {
            for (int j = 0; j < 4; j++) {
                int tPunish = 0;
                char tRank = '0';
                tRank = Card.SetRank(i);
                tPunish = Card.SetPunish(tRank, Colors[j]);
                String ico = Card.ImagePath(Colors[j], tRank, taliaI);
                CardDeck.add(new Card(tRank, Colors[j], tPunish, ico, false));
                CardDeck.get(i).setIcon(new ImageIcon(getClass().getResource(ico)));
            }
        }
        if (jokers_amount != 0) {
            for (int i = 0; i < jokers_amount; i++) CardDeck.add(new Card('B', '\0', 0, Card.ImagePath('\0', 'B', taliaI), true));
        }
        Collections.shuffle(CardDeck);
        Collections.shuffle(CardDeck);
    }

    public void GiveOutDeck(Player tPlayer, int player_amount, int player, int perPlayer) {
        players = player_amount;
        int index = 1;
        for (int i = CardDeck.size() - player; index <= perPlayer; i -= player_amount, index++) {
            tPlayer.addCard(CardDeck.get(i));
            tPlayer.getCard(tPlayer.getPlayerCardsAmount() - 1).ind = tPlayer.getPlayerCardsAmount() - 1;
            available--;
        }
    }

    public void Begin() {
        CardDeck.subList(available, CardDeck.size()).clear();
        ActiveCard = new Card();
        available--;
        while (CardDeck.get(available).punish != 0 || CardDeck.get(available).rank == 'B') available--;
        ActiveCard = CardDeck.get(available);
        CardDeck.remove(available);
    }

    public int DeckSize() {
        return CardDeck.size();
    }

    public Card getActiveCard() {
        return ActiveCard;
    }

    public void setActiveCard(Card Active) {
        ActiveCard.color = Active.color;
        ActiveCard.rank = Active.rank;
        ActiveCard.punish = Active.punish;
        ActiveCard.pathAwers = Active.pathAwers;
        ActiveCard.joker = Active.joker;
    }

    public void removeFromDeck(int index) {
        CardDeck.remove(index);
    }

    protected Card getCard(int index) {
        return CardDeck.get(index);
    }

    public void addToUsedCards(Card Card1) {
        CardsUsed.add(Card1);
    }

    public void RemoveSubList(int min, int max) {
        for (int i = max; i >= min; i--) CardDeck.remove(i);
    }
}
