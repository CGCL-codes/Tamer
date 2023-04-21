package org.tjuscs.sevenwonders.core;

/**
 * The Class ColorBasedIncomeAction.
 */
public class ColorBasedIncomeAction implements Action, DelayedAction {

	/** The color. */
	CardColor color;

	/** The income. */
	int income;

	/** The board. */
	Board board;

	/** The self count. */
	int leftCount, rightCount, selfCount;

	/**
	 * Instantiates a new color based income action.
	 * 
	 * @param clr
	 *            the clr
	 * @param inc
	 *            the inc
	 */
	public ColorBasedIncomeAction(CardColor clr, int inc) {
		color = clr;
		income = inc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.tjuscs.sevenwonders.core.Action#activate(org.tjuscs.sevenwonders.
	 * core.Board)
	 */
	public void activate(Board brd) {
		board = brd;
		GameManager.getManager().addEOTDelayedAction(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tjuscs.sevenwonders.core.DelayedAction#doDelayedAction()
	 */
	public void doDelayedAction() {
		int newLeftCount = board.getLeftNeighbor().getColorCount(color);
		int newRightCount = board.getRightNeighbor().getColorCount(color);
		int newSelfCount = board.getColorCount(color);
		int newStructures = 0;
		if (newLeftCount > leftCount) {
			newStructures = newLeftCount - leftCount;
			leftCount = newLeftCount;
		}
		if (newRightCount > rightCount) {
			newStructures += newRightCount - rightCount;
			rightCount = newRightCount;
		}
		if (newSelfCount > selfCount) {
			newStructures += newSelfCount - selfCount;
			selfCount = newSelfCount;
		}
		board.addToCoins(newStructures * income);

		// since this is only done once, remove the DelayedAction
		GameManager.getManager().removeEOTDelayedAction(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "coins based on neighbors " + color.toString() + " cards";
	}

}
