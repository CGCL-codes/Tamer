package org.gamegineer.table.internal.core;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
final class Messages extends NLS {

    /** The card listener is already registered. */
    public static String Card_addCardListener_listener_registered;

    /**
     * An unexpected exception was thrown from
     * ICardListener.cardLocationChanged().
     */
    public static String Card_cardLocationChanged_unexpectedException;

    /**
     * An unexpected exception was thrown from
     * ICardListener.cardOrientationChanged().
     */
    public static String Card_cardOrientationChanged_unexpectedException;

    /** The face design size is not equal to the back design size. */
    public static String Card_ctor_faceDesign_sizeNotEqual;

    /** The card listener is not registered. */
    public static String Card_removeCardListener_listener_notRegistered;

    /** The card pile listener is already registered. */
    public static String CardPile_addCardPileListener_listener_registered;

    /** The card collection contains a {@code null} element. */
    public static String CardPile_addCards_cards_containsNullElement;

    /** An unexpected exception was thrown from ICardPileListener.cardAdded(). */
    public static String CardPile_cardAdded_unexpectedException;

    /**
     * An unexpected exception was thrown from
     * ICardPileListener.cardPileBoundsChanged().
     */
    public static String CardPile_cardPileBoundsChanged_unexpectedException;

    /** An unexpected exception was thrown from ICardPileListener.cardRemoved(). */
    public static String CardPile_cardRemoved_unexpectedException;

    /** An unknown layout is active. */
    public static String CardPile_getCardOffsetAt_unknownLayout;

    /** The card pile listener is not registered. */
    public static String CardPile_removeCardPileListener_listener_notRegistered;

    /** The card pile base design height must not be negative. */
    public static String CardPileBaseDesign_ctor_height_negative;

    /** The card pile base design width must not be negative. */
    public static String CardPileBaseDesign_ctor_width_negative;

    /** The card surface design height must not be negative. */
    public static String CardSurfaceDesign_ctor_height_negative;

    /** The card surface design width must not be negative. */
    public static String CardSurfaceDesign_ctor_width_negative;

    /** The table listener is already registered. */
    public static String Table_addTableListener_listener_registered;

    /** An unexpected exception was thrown from ITableListener.cardPileAdded(). */
    public static String Table_cardPileAdded_unexpectedException;

    /**
     * An unexpected exception was thrown from ITableListener.cardPileRemoved().
     */
    public static String Table_cardPileRemoved_unexpectedException;

    /** The table listener is not registered. */
    public static String Table_removeTableListener_listener_notRegistered;

    /**
     * Initializes the {@code Messages} class.
     */
    static {
        NLS.initializeMessages(Messages.class.getName(), Messages.class);
    }

    /**
     * Initializes a new instance of the {@code Messages} class.
     */
    private Messages() {
        super();
    }
}
