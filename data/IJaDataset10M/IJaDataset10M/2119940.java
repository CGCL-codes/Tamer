package Galaxy.Tree.Workflow;

public class Position {

    private final int unitsFromLeftMargin;

    private final int unitsFromTopMargin;

    public Position(int unitsFromLeftMargin, int unitsFromTopMargin) {
        this.unitsFromLeftMargin = unitsFromLeftMargin;
        this.unitsFromTopMargin = unitsFromTopMargin;
    }

    public Integer getFromLeft() {
        return this.unitsFromLeftMargin;
    }

    public Integer getFromTop() {
        return this.unitsFromTopMargin;
    }
}
