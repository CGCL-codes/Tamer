package com.google.gwt.maeglin89273.game.ashinyballonthecross.client.tutorial;

/**
 * @author Maeglin Liao
 *
 */
public class Step {

    public static final String TITLE_KEY = "title";

    public static final String CORNER_X_KEY = "x";

    public static final String CORNER_Y_KEY = "y";

    private final String title;

    private final int cornerX;

    private final int cornerY;

    private final TaskBuilder[] builders;

    private final TaskHandler handler;

    public Step(String title, int cornerX, int conerY, TaskBuilder[] builders, TaskHandler handler) {
        this.title = title;
        this.cornerX = cornerX;
        cornerY = conerY;
        this.builders = builders;
        this.handler = handler;
    }

    public int getBoardCornerX() {
        return cornerX;
    }

    public int getBoardCornerY() {
        return cornerY;
    }

    public String getTitle() {
        return title;
    }

    public TaskBuilder[] getTaskBuilder() {
        return builders;
    }

    public TaskHandler getTaskHandler() {
        return this.handler;
    }
}
