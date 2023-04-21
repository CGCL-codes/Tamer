package de.molimo.session;

/**
 * @author Marcus Schiesser
 */
public class BrowserInfo implements IBrowserInfo {

    private final int numberOfAllItems;

    private final int startPositionOfView;

    private final int endPositionOfView;

    public BrowserInfo(int numberOfAllItems, int startPositionOfView, int endPositionOfView) {
        this.numberOfAllItems = numberOfAllItems;
        this.startPositionOfView = startPositionOfView;
        this.endPositionOfView = endPositionOfView;
    }

    public int getNumberOfAllItems() {
        return numberOfAllItems;
    }

    public int getStartPositionOfView() {
        return startPositionOfView;
    }

    public int getEndPositionOfView() {
        return endPositionOfView;
    }
}
