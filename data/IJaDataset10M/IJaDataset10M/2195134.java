package com.st.rrd;

import java.awt.Point;

class ImageBounds {

    private Point graphLeftTopPosition;

    private Point graphRightBottomPosition;

    private int graphHeight;

    private int graphWidth;

    private int width;

    private int height;

    private int titleX;

    private int titleY;

    public int getTitleX() {
        return titleX;
    }

    public int getTitleY() {
        return titleY;
    }

    public void setTitleX(int titleX) {
        this.titleX = titleX;
    }

    public void setTitleY(int titleY) {
        this.titleY = titleY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public Point getGraphLeftTopPosition() {
        return graphLeftTopPosition;
    }

    public void setGraphLeftTopPosition(Point graphLeftTopPosition) {
        this.graphLeftTopPosition = graphLeftTopPosition;
    }

    public Point getGraphRightBottomPosition() {
        return graphRightBottomPosition;
    }

    public void setGraphRightBottomPosition(Point graphRightBottomPosition) {
        this.graphRightBottomPosition = graphRightBottomPosition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ImageBounds [graphLeftTopPosition=").append(graphLeftTopPosition).append(", graphRightBottomPosition=").append(graphRightBottomPosition).append("]");
        return builder.toString();
    }
}
