package net.sourceforge.strategema.client;

import net.sourceforge.strategema.common.ThreeDPlacement;
import net.sourceforge.strategema.games.Dice;
import net.sourceforge.strategema.games.Die;
import java.awt.Container;

/**
 * Displays dice.
 * 
 * @author Lizzy
 * 
 * @param <V> The visual representation of the dice.
 */
public abstract class DiceRenderer<V> {

    /**
	 * Gets the visual for the die face that is currently facing upwards, or null if no faces are aligned upward.
	 * @param <V>
	 * @param die
	 * @param position
	 * @return
	 */
    public static <V> V getFaceUp(final Die<?, V> die, final ThreeDPlacement position) {
    }

    /**
	 * Adds the dice to another GUI element.
	 * @param comp The component to place the dice within.
	 * @param constraints The positioning constraints.
	 * @param index The position in the container's list at which to insert the dice. -1 means
	 * insert at the end.
	 * @param dice The dice that this dice renderer will render.
	 */
    public abstract void addTo(Container comp, Object constraints, int index, Dice<?, ? extends V> dice);

    /**
	 * Renders the dice onto the screen.
	 * @param dice The dice to display.
	 * @param positions The position and orientation of each die.
	 */
    public abstract void render(Dice<?, ? extends V> dice, ThreeDPlacement[] positions);
}
