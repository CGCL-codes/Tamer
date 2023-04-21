package org.skycastle.scratchpad.junglegame;

import org.skycastle.kernel.EntityContainer;
import org.skycastle.kernel.impl.local.LocalEntityContainer;
import java.util.logging.Logger;

/**
 * A theatre view, room based jungle survival game to demonstrate NPC AI techniques, like learning different
 * effects of food ingredients, preparing and combining ingredients, testing the effects of compositions, and
 * communicating the knowledge of compositions and the whereabout of ingredients to others.
 * <p/>
 * Should also provide a human player with some entertainment (exploration, experimentation, survival, and
 * some NPC interaction).
 *
 * @author Hans Haggstrom
 */
public final class Junglegame {

    private static final Logger LOGGER = Logger.getLogger(Junglegame.class.getName());

    private final EntityContainer myContainer = new LocalEntityContainer(true, "JungleGame");

    /**
     * Main entry point.
     *
     * @param args command line arguments (not used atm.)
     */
    public static void main(String[] args) {
    }

    /**
     * Creates a new {@link org.skycastle.scratchpad.junglegame.Junglegame}.
     */
    public Junglegame() {
    }
}
