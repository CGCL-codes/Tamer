package org.gamegineer.table.internal.ui;

import javax.swing.Icon;
import org.easymock.EasyMock;
import org.gamegineer.table.core.CardPileBaseDesignId;
import org.gamegineer.table.ui.AbstractCardPileBaseDesignUITestCase;
import org.gamegineer.table.ui.ICardPileBaseDesignUI;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.ui.CardPileBaseDesignUI} class to ensure
 * it does not violate the contract of the
 * {@link org.gamegineer.table.ui.ICardPileBaseDesignUI} interface.
 */
public final class CardPileBaseDesignUIAsCardPileBaseDesignUITest extends AbstractCardPileBaseDesignUITestCase {

    /**
     * Initializes a new instance of the {@code
     * CardPileBaseDesignUIAsCardPileBaseDesignUITest} class.
     */
    public CardPileBaseDesignUIAsCardPileBaseDesignUITest() {
        super();
    }

    @Override
    protected ICardPileBaseDesignUI createCardPileBaseDesignUI() {
        return new CardPileBaseDesignUI(CardPileBaseDesignId.fromString("id"), "name", EasyMock.createMock(Icon.class));
    }
}
