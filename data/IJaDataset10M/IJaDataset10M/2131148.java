package net.sourceforge.strategema.client;

import net.sourceforge.strategema.common.ThreeDPlacement;
import net.sourceforge.strategema.games.ApplicationInterface;
import net.sourceforge.strategema.games.Dice;
import net.sourceforge.strategema.ui.Interaction;

import java.awt.Container;

import javax.swing.JLabel;

public class TextDiceRenderer extends DiceRenderer<String> {

	private JLabel label;

	public TextDiceRenderer() {
		super();
	}

	@Override
	public void render(final Dice<?, ? extends String> dice, final ThreeDPlacement[] positions) {
		final int numDice = dice.getNumberOfDice();
		if (this.label == null) {
			for (int i = 0; i < numDice; i++) {
				if (DiceRenderer.getFaceUp(dice.getDie(i), positions[i]) == null) {
					return;
				}
			}
		}
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < numDice; i++) {
			final String dieStr = DiceRenderer.getFaceUp(dice.getDie(i), positions[i]);
			if (dieStr == null) {
				buf.append("?");
			} else {
				buf.append(dieStr);
			}
			if (i < numDice - 1) {
				buf.append(", ");
			}
		}
		this.output(buf.toString());
	}

	@Override
	public void addTo(final Container comp, final Object constraints, final int index, final Dice<?, ? extends String> dice) {
		if (this.label == null) {
			this.label = new JLabel();
			//Calculate dimensions
			...
		}
	}

	private void output(final String str) {
		final Interaction io = ApplicationInterface.getAPI().getInteraction();
		if (io.isGraphical()) {
			this.label.setText(str);
		} else {
			io.output("Dice roll " + str);
		}
	}
}
