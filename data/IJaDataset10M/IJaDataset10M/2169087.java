package com.mudderman.mudutils.ui.card;

import com.mudderman.mudutils.event.EventInfo;
import com.mudderman.mudutils.event.EventManager;

public class CardFactory {

    /**
	 * Skapa ett kort ifr�n en befintlig klass.
	 * @param classToLoad
	 * @return
	 */
    public static final Card createCard(Class<?> classToLoad) {
        try {
            Object obj = classToLoad.newInstance();
            if (obj instanceof Card) {
                EventManager.INSTANCE.addObserver((Card) obj);
                return (Card) obj;
            }
            throw new RuntimeException(classToLoad + " is not a subclass of Card");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Card createDummyCard(String identifier) {
        Card card = new Card("A dummy card", "", identifier) {

            private static final long serialVersionUID = -6268347794906986165L;

            @Override
            public void resetCard() {
            }

            @Override
            public void saveCard() {
            }

            @Override
            public void update(EventInfo e) {
            }
        };
        return card;
    }
}
