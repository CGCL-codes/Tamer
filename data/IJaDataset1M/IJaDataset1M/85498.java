package org.gamegineer.table.ui;

import javax.swing.Icon;
import org.gamegineer.table.core.CardPileBaseDesignId;

/**
 * A card pile base design user interface.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ICardPileBaseDesignUI {

    public Icon getIcon();

    public CardPileBaseDesignId getId();

    public String getName();
}
