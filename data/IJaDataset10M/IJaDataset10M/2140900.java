package apollo.gui;

import javax.swing.BoundedRangeModel;

public abstract class ScoreCalculator {

    int xOrientation;

    protected int factor;

    public ScoreCalculator() {
        factor = 1;
    }

    public int getFactor() {
        return factor;
    }

    public int[] getYRange() {
        return new int[] { 0, 100 };
    }

    /** Get the score for a given position
   * 
   * @param position - position to get the score for
   * @return the score for the position
   */
    public double getScoreForPosition(int position) {
        throw new RuntimeException("ScoreCalculator.getScoreForPosition not implemented");
    }

    public abstract double[] getScoresForPositions(int[] positions);

    public abstract int[] getXRange();

    public void setXOrientation(int orient) {
        this.xOrientation = orient;
    }

    /** Whether getModel() is non null */
    public boolean hasModel() {
        return false;
    }

    /** By default returns null. Subclass(WindowScoreCalculator) overrides if has
      a BoundedRangeModel associated with it */
    public BoundedRangeModel getModel() {
        return null;
    }
}
