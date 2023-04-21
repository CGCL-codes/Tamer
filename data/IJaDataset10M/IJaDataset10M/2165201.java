package org.gamegineer.table.core;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * A fixture for testing the {@link org.gamegineer.table.core.CardFactory}
 * class.
 */
public final class CardFactoryTest {

    /**
     * Initializes a new instance of the {@code CardFactoryTest} class.
     */
    public CardFactoryTest() {
        super();
    }

    /**
     * Ensures the {@code createCard(ICardSurfaceDesign, ICardSurfaceDesign)}
     * method throws an exception when passed a {@code null} back design.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCardFromBackDesignAndFaceDesign_BackDesign_Null() {
        CardFactory.createCard(null, CardSurfaceDesigns.createUniqueCardSurfaceDesign());
    }

    /**
     * Ensures the {@code createCard(ICardSurfaceDesign, ICardSurfaceDesign)}
     * method throws an exception when passed a {@code null} face design.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCardFromBackDesignAndFaceDesign_FaceDesign_Null() {
        CardFactory.createCard(CardSurfaceDesigns.createUniqueCardSurfaceDesign(), null);
    }

    /**
     * Ensures the {@code createCard(ICardSurfaceDesign, ICardSurfaceDesign)}
     * method throws an exception when passed a face design that has a size
     * different from the back design.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCardFromBackDesignAndFaceDesign_FaceDesign_SizeNotEqual() {
        final int width = 10;
        final int height = 20;
        final ICardSurfaceDesign backDesign = CardSurfaceDesigns.createUniqueCardSurfaceDesign(width, height);
        final ICardSurfaceDesign faceDesign = CardSurfaceDesigns.createUniqueCardSurfaceDesign(2 * width, 2 * height);
        CardFactory.createCard(backDesign, faceDesign);
    }

    /**
     * Ensures the {@code createCard(ICardSurfaceDesign, ICardSurfaceDesign)}
     * method does not return {@code null}.
     */
    @Test
    public void testCreateCardFromBackDesignAndFaceDesign_ReturnValue_NonNull() {
        assertNotNull(CardFactory.createCard(CardSurfaceDesigns.createUniqueCardSurfaceDesign(), CardSurfaceDesigns.createUniqueCardSurfaceDesign()));
    }

    /**
     * Ensures the {@code createCard(IMemento)} method throws an exception when
     * passed a {@code null} memento.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCardFromMemento_Memento_Null() throws Exception {
        CardFactory.createCard(null);
    }
}
