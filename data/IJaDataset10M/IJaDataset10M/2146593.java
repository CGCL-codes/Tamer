package cardgames.control.maumau.exceptions;

import cardgames.model.cards.MauMauCard;

/**
 * Thrown to indicate that a card provided is illegal.
 * <p>
 * This is a deadly exception. It indicates that the table considers the
 * {@link MauMauCard card} provided to be a fake-card not belonging to the
 * deck currently used at that table. Or the table receives a card from some
 * player but did not pass this card to the player. Both circumstances make
 * the table shut down.
 */
public class IllegalCard extends MauMauTableException {

    /**
     * ID used to make instances of this class transferable over the net.
     * <p>
     * @see <a href="http://dev.root1.de/wiki/simon/Sample_helloworld" target="_BLANK">SIMON-sample</a>
     */
    @SuppressWarnings("deprecation")
    static final long serialVersionUID = (new java.util.Date(2010, 6, 13)).getTime();

    public IllegalCard(String tableName, MauMauCard card) {
        super(card.getClass().getSimpleName() + ' ' + card + " is illegal at " + tableName);
    }
}
