package com.novatialabs.qttt.engine.impl;

import com.novatialabs.qttt.engine.Qt3MoveNotInSquareException;
import com.novatialabs.qttt.engine.Qt3MoveNotInTangleException;
import com.novatialabs.qttt.engine.Qt3Player;

class Qt3Resolution extends Qt3Step {

    public static final int Resolution_UNDECIDED = 0;

    public static final int Resolution_FIRST_SQUARE = 1;

    public static final int Resolution_SECOND_SQUARE = 2;

    public static final int Resolution_NEEDED = 3;

    public static final int Resolution_NONE = 4;

    /**
    * The state of the resolution.
    */
    private int resolution;

    Qt3Resolution(int moveNum, Qt3Step prevStep) {
        super(moveNum, prevStep);
    }

    /**
    * Clears this resolution object to an initial state.
    */
    protected void clearStep() {
        this.resolution = Resolution_UNDECIDED;
    }

    void appendAsString(StringBuffer buf) {
        switch(this.resolution) {
            case Resolution_FIRST_SQUARE:
                buf.append('<');
                break;
            case Resolution_SECOND_SQUARE:
                buf.append('>');
                break;
            case Resolution_NONE:
                buf.append('-');
                break;
            case Resolution_NEEDED:
                buf.append('?');
                break;
            case Resolution_UNDECIDED:
            default:
                buf.append(' ');
                break;
        }
    }

    /**
    * Returns the player who decides this resolution.
    */
    public Qt3Player getPlayer() {
        return (((this.getMoveNumber() % 2) == 1) ? Qt3Player.O : Qt3Player.X);
    }

    boolean isRedoable() {
        switch(this.resolution) {
            case Resolution_NONE:
            case Resolution_FIRST_SQUARE:
            case Resolution_SECOND_SQUARE:
                return true;
            default:
            case Resolution_UNDECIDED:
            case Resolution_NEEDED:
                return false;
        }
    }

    boolean isAutomatic() {
        if (this.resolution == Resolution_UNDECIDED) {
            System.err.println("Resolution is undecided...");
            if (this.getPreviousStep().getState().hasSelfRefEntanglement()) {
                System.err.println("... resolution needed.");
                this.resolution = Resolution_NEEDED;
            } else {
                System.err.println("... resolution none.");
                this.resolution = Resolution_NONE;
            }
        }
        return (this.resolution == Resolution_NONE);
    }

    /**
    * Returns the resolution of this move.
    * The resolution will be one of the constant Resolution_* constants.
    */
    public int getResolution() {
        return this.resolution;
    }

    void setResolution(boolean resolveToFirstSquare) {
        if (this.resolution != Resolution_NEEDED) {
            throw new IllegalStateException();
        }
        if (resolveToFirstSquare) {
            this.resolution = Resolution_FIRST_SQUARE;
        } else {
            this.resolution = Resolution_SECOND_SQUARE;
        }
    }

    boolean normalizeResolution(int squareIndex, int moveNumber) throws Qt3MoveNotInTangleException, Qt3MoveNotInSquareException {
        final Qt3Tangle tangle = this.getPreviousStep().getState().getTangle();
        return tangle.normalizeResolution(squareIndex, moveNumber);
    }

    /**
    * Sets the resolution for this move.
    *
    * @param	resolveToFirstSquare	whether resolution is to first
    *					square (<code>true</code>) or
    *					second square (<code>false</code>).
    */
    protected Qt3State transformState(Qt3State prevState) {
        switch(this.resolution) {
            case Resolution_UNDECIDED:
            case Resolution_NEEDED:
            default:
            case Resolution_NONE:
                return prevState;
            case Resolution_FIRST_SQUARE:
                {
                    Qt3State newState = new Qt3State(prevState, true);
                    return newState;
                }
            case Resolution_SECOND_SQUARE:
                {
                    Qt3State newState = new Qt3State(prevState, false);
                    return newState;
                }
        }
    }
}
