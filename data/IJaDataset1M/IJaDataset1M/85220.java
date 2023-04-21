package ua.in.say.cgfw.base;

import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import ua.in.say.cgfw.contract.*;
import ua.in.say.cgfw.contract.comparator.CardComparator;
import ua.in.say.cgfw.contract.factory.*;
import ua.in.say.cgfw.enums.Rank;
import ua.in.say.cgfw.enums.Suit;
import java.util.List;

/**
 * @author <a href="mailto:Yuriy.Lazarev@gmail.com">Yuriy.Lazarev@gmail.com</a>
 */
public abstract class ConfigurationBase extends DefaultPicoContainer implements Configuration {

    public ConfigurationBase() {
        as(Characteristics.SINGLE).addComponent(TableFactory.class, this);
        as(Characteristics.SINGLE).addComponent(ChairFactory.class, this);
        as(Characteristics.SINGLE).addComponent(TurnFactory.class, this);
        as(Characteristics.SINGLE).addComponent(DeckFactory.class, this);
        as(Characteristics.SINGLE).addComponent(PackFactory.class, this);
        as(Characteristics.SINGLE).addComponent(CardFactory.class, this);
        as(Characteristics.SINGLE).addComponent(GameFactory.class, this);
        as(Characteristics.SINGLE).addComponent(PlayerFactory.class, this);
        as(Characteristics.SINGLE).addComponent(CardComparsionFactory.class, this);
    }

    public Card createCard(Rank rank, Suit suit) {
        addComponent(Rank.class, rank);
        addComponent(Suit.class, suit);
        Card card = getComponent(Card.class);
        removeComponent(Rank.class);
        removeComponent(Suit.class);
        return card;
    }

    public Card createCard(Rank rank, Suit suit, boolean isOpen) {
        Card card = createCard(rank, suit);
        if (isOpen) {
            card.open();
        }
        return card;
    }

    public Pack createPack(List<Card> cards) {
        if (cards == null) throw new IllegalArgumentException("cards is null");
        Pack pack = getComponent(Pack.class);
        pack.addCards(cards);
        return pack;
    }

    public CardComparator createCardComparsion() {
        return getComponent(CardComparator.class);
    }

    public Player createPlayer() {
        return getComponent(Player.class);
    }

    public Table createTable() {
        return getComponent(Table.class);
    }

    public Chair createChair() {
        return getComponent(Chair.class);
    }

    public Deck createDeck() {
        return getComponent(Deck.class);
    }

    public Turn createTurn() {
        return getComponent(Turn.class);
    }

    public Pack createPack() {
        return getComponent(Pack.class);
    }
}
