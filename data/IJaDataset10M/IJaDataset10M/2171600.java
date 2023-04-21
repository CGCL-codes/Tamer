package com.marquisx.tzdice.model;

import com.marquisx.tzdice.Constants.DiceType;

public class DiceAction {

    public static final int NO_THRESHOLD = 0;

    public static final int NO_DICE = -1;

    public static final int NO_PICTURE = -1;

    /** The id of the action */
    private long id;

    /** The name of the action */
    private String name;

    /** The description of the action */
    private String description;

    /** The thumb of the action */
    private int pictureId = NO_PICTURE;

    /** The dice number of the action */
    private int dice = NO_DICE;

    /** The enum of dice type */
    private DiceType diceType = DiceType.NO_DICE;

    /** The rolling times of the action */
    private int times;

    /** The offset of the action */
    private int offset;

    /** Whether or not the action has a threshold */
    private boolean hasThreshold;

    /** The value of the threshold. */
    private int threshold;

    public DiceAction() {
        this("", "", NO_PICTURE, NO_DICE, 1, 0, false, NO_THRESHOLD);
    }

    public DiceAction(int pictureId) {
        this("", "", pictureId, NO_DICE, 1, 0, false, NO_THRESHOLD);
    }

    public DiceAction(int dice, int times, int offset) {
        this("", "", -1, dice, times, offset, false, NO_THRESHOLD);
    }

    public DiceAction(String name, String description, int pictureId, int dice, int times, int offset, boolean hasThreshold, int threshold) {
        this.name = name;
        this.description = description;
        this.pictureId = pictureId;
        this.dice = dice;
        this.diceType = DiceType.getDiceType(dice);
        this.times = times;
        this.offset = offset;
        this.hasThreshold = hasThreshold;
        this.threshold = threshold;
    }

    public String getFormattedThreshold(boolean withBrackets) {
        String thresholdStr = "";
        if (hasThreshold) {
            thresholdStr = "" + threshold;
        } else {
            return "";
        }
        return withBrackets ? "<" + thresholdStr + ">" : thresholdStr;
    }

    public String getFormattedOffset(boolean withBrackets) {
        String offsetStr = "";
        if (offset > 0) {
            offsetStr = "+" + offset;
        } else if (offset < 0) {
            offsetStr = offset + "";
        } else {
            return "";
        }
        return withBrackets ? "(" + offsetStr + ")" : offsetStr;
    }

    public void copy(DiceAction diceAction) {
        this.name = diceAction.getName();
        this.description = diceAction.getDescription();
        this.pictureId = diceAction.getPictureId();
        this.dice = diceAction.getDice();
        this.diceType = diceAction.getDiceType();
        this.times = diceAction.getTimes();
        this.offset = diceAction.getOffset();
        this.hasThreshold = diceAction.isHasThreshold();
        this.threshold = diceAction.getThreshold();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDice() {
        return dice;
    }

    public void setDice(int dice) {
        this.dice = dice;
        this.diceType = DiceType.getDiceType(dice);
    }

    public DiceType getDiceType() {
        return this.diceType;
    }

    public void setDiceType(DiceType diceType) {
        this.dice = diceType.getDiceNumber();
        this.diceType = diceType;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isHasThreshold() {
        return hasThreshold;
    }

    public void setHasThreshold(boolean hasThreshold) {
        this.hasThreshold = hasThreshold;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getStatus() {
        return times + diceType.getDiceName() + getFormattedOffset(true) + " " + getFormattedThreshold(true);
    }

    public String toString() {
        return getStatus() + " " + name;
    }
}
