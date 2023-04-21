package org.gamegineer.table.core;

/**
 * The interface that defines the behavior of all event objects fired by a card
 * pile.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ICardPileEvent {

    public ICardPile getCardPile();
}
